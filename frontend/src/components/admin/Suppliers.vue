<template>
  <el-card>
    <template #header>
      <div class="header-toolbar">
        <span>供应商管理</span>
        <el-button type="primary" size="small" @click="handleAdd">添加供应商</el-button>
      </div>
    </template>

    <!-- 搜索栏 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="供应商名称">
        <el-input v-model="searchForm.supplierName" placeholder="请输入供应商名称" clearable style="width: 180px"></el-input>
      </el-form-item>
      <el-form-item label="供货品类">
        <el-input v-model="searchForm.supplyCategory" placeholder="请输入供货品类" clearable style="width: 150px"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadData">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格 -->
    <el-table :data="tableData" stripe v-loading="loading" border style="width: 100%">
      <el-table-column prop="supplierName" label="供应商名称" width="150" align="center"></el-table-column>
      <el-table-column prop="contactPerson" label="联系人" width="100" align="center"></el-table-column>
      <el-table-column prop="contactPhone" label="联系电话" width="130" align="center"></el-table-column>
      <el-table-column prop="supplyCategory" label="供货品类" width="120" align="center"></el-table-column>
      <el-table-column prop="cooperationStatus" label="合作状态" width="100" align="center">
        <template #default="scope">
          <el-tag v-if="scope.row.cooperationStatus === 0" type="success" size="small">合作中</el-tag>
          <el-tag v-else type="danger" size="small">已暂停</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="210" align="center" fixed="right">
        <template #default="scope">
          <el-button size="small" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="small" :type="scope.row.cooperationStatus === 0 ? 'warning' : 'success'" plain @click="toggleStatus(scope.row)">
            {{ scope.row.cooperationStatus === 0 ? "暂停" : "恢复" }}
          </el-button>
          <el-button size="small" type="danger" plain @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
        @current-change="handleCurrentChange"
        v-model:current-page="pageNum"
        :page-size="pageSize"
        :total="total"
        layout="total, prev, pager, next"
        class="pagination">
    </el-pagination>

    <!-- 添加/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="100px">
        <el-form-item label="供应商名称" prop="supplierName">
          <el-input v-model="formData.supplierName"></el-input>
        </el-form-item>
        <el-form-item label="联系人" prop="contactPerson">
          <el-input v-model="formData.contactPerson"></el-input>
        </el-form-item>
        <el-form-item label="联系电话" prop="contactPhone">
          <el-input v-model="formData.contactPhone"></el-input>
        </el-form-item>
        <el-form-item label="供货品类" prop="supplyCategory">
          <el-input v-model="formData.supplyCategory"></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="formData.address"></el-input>
        </el-form-item>
        <el-form-item label="备注">
          <el-input type="textarea" v-model="formData.remark"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { reactive, ref, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import http from "@/util/http.js";

const loading = ref(false);
const tableData = ref([]);
const pageNum = ref(1);
const pageSize = ref(10);
const total = ref(0);
const dialogVisible = ref(false);
const dialogTitle = ref("");
const formRef = ref();

const searchForm = reactive({
  supplierName: "",
  supplyCategory: ""
});

const formData = reactive({
  supplierId: null,
  supplierName: "",
  contactPerson: "",
  contactPhone: "",
  supplyCategory: "",
  address: "",
  remark: "",
  cooperationStatus: 0
});

const formRules = {
  supplierName: [{ required: true, message: "请输入供应商名称", trigger: "blur" }]
};

const loadData = () => {
  loading.value = true;
  const params = {
    pageNum: pageNum.value,
    pageSize: pageSize.value,
    supplierName: searchForm.supplierName || null,
    supplyCategory: searchForm.supplyCategory || null
  };
  http.get("/supplier/list", { params }).then(res => {
    let result = res;
    if (result.data && result.data.data) {
      result = result.data.data;
    } else if (result.data) {
      result = result.data;
    }
    tableData.value = result?.list || [];
    total.value = result?.totalSize || 0;
    loading.value = false;
  }).catch(() => {
    tableData.value = [];
    loading.value = false;
  });
};

const resetSearch = () => {
  searchForm.supplierName = "";
  searchForm.supplyCategory = "";
  pageNum.value = 1;
  loadData();
};

const handleCurrentChange = (val) => {
  pageNum.value = val;
  loadData();
};

const handleAdd = () => {
  dialogTitle.value = "添加供应商";
  Object.assign(formData, {
    supplierId: null,
    supplierName: "",
    contactPerson: "",
    contactPhone: "",
    supplyCategory: "",
    address: "",
    remark: "",
    cooperationStatus: 0
  });
  dialogVisible.value = true;
};

const handleEdit = (row) => {
  dialogTitle.value = "编辑供应商";
  Object.assign(formData, row);
  dialogVisible.value = true;
};

const handleSave = async () => {
  try {
    await formRef.value.validate();
    if (formData.supplierId) {
      http.put("/supplier/edit", formData).then(res => {
        if (res.code === "200") {
          ElMessage.success("修改成功");
          dialogVisible.value = false;
          loadData();
        }
      });
    } else {
      http.post("/supplier/add", formData).then(res => {
        if (res.code === "200") {
          ElMessage.success("添加成功");
          dialogVisible.value = false;
          loadData();
        }
      });
    }
  } catch (error) {
    console.error("表单验证失败", error);
  }
};

const handleDelete = (row) => {
  ElMessageBox.confirm("确定删除该供应商吗？", "提示", { type: "warning" }).then(() => {
    http.delete(`/supplier/delete/${row.supplierId}`).then(res => {
      if (res.code === "200") {
        ElMessage.success("删除成功");
        loadData();
      }
    });
  }).catch(() => {});
};

const toggleStatus = (row) => {
  const newStatus = row.cooperationStatus === 0 ? 1 : 0;
  const updated = { ...row, cooperationStatus: newStatus };
  http.put("/supplier/edit", updated).then(res => {
    if (res.code === "200") {
      ElMessage.success(newStatus === 0 ? "已恢复合作" : "已暂停合作");
      loadData();
    }
  });
};

onMounted(() => {
  loadData();
});
</script>

<style scoped>
.header-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.search-form {
  margin-bottom: 20px;
}
.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>