<template>
  <el-card>
    <template #header>
      <div class="header-toolbar">
        <span>商品管理</span>
        <el-button type="primary" size="small" @click="handleAdd">添加商品</el-button>
      </div>
    </template>

    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="商品名称">
        <el-input v-model="searchForm.productName" placeholder="请输入商品名称" clearable></el-input>
      </el-form-item>


      <el-form-item label="商品分类" prop="categoryId">
        <el-input v-model="searchForm.categoryId" placeholder="请输入分类ID"></el-input>
      </el-form-item>

      <el-form-item label="商品状态">
        <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
          <el-option label="上架" :value="0" />
          <el-option label="下架" :value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadData">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="tableData" stripe v-loading="loading">
      <el-table-column prop="productName" label="商品名称" width="150"></el-table-column>
      <el-table-column prop="categoryName" label="分类" width="100"></el-table-column>
      <el-table-column prop="price" label="单价" width="100">
        <template #default="scope">￥{{ scope.row.price }}</template>
      </el-table-column>
      <el-table-column prop="specification" label="规格" width="100"></el-table-column>
      <el-table-column prop="stockQuantity" label="库存" width="100"></el-table-column>
      <el-table-column prop="supplierName" label="供应商" width="120"></el-table-column>
      <el-table-column prop="status" label="状态" width="80">
        <template #default="scope">
          <el-tag v-if="scope.row.status === 0" type="success">上架</el-tag>
          <el-tag v-else type="danger">下架</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="scope">
          <el-button size="small" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="small" :type="scope.row.status === 0 ? 'warning' : 'success'" @click="toggleStatus(scope.row)">
            {{ scope.row.status === 0 ? '下架' : '上架' }}
          </el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
        @current-change="handleCurrentChange"
        v-model:current-page="pageNum"
        :page-size="pageSize"
        :total="total"
        layout="total, prev, pager, next"
        class="pagination">
    </el-pagination>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="100px">
        <el-form-item label="商品名称" prop="productName">
          <el-input v-model="formData.productName"></el-input>
        </el-form-item>
<!--        <el-form-item label="商品分类" prop="categoryId">-->
<!--          <el-select v-model="formData.categoryId" placeholder="请选择分类">-->
<!--            <el-option-->
<!--                v-for="item in categories"-->
<!--                :key="item.categoryId"-->
<!--                :label="item.categoryName"-->
<!--                :value="item.categoryId"-->
<!--                v-if="item && item.categoryId != null"-->
<!--            />-->
<!--          </el-select>-->
<!--        </el-form-item>-->

        <el-form-item label="商品分类" prop="categoryId">
          <el-input v-model="formData.categoryId" placeholder="请输入分类ID"></el-input>
        </el-form-item>

<!--        <el-form-item label="供应商" prop="supplierId">-->
<!--          <el-select v-model="formData.supplierId" placeholder="请选择供应商">-->
<!--            <el-option-->
<!--                v-for="item in suppliers"-->
<!--                :key="item.supplierId"-->
<!--                :label="item.supplierName"-->
<!--                :value="item.supplierId"-->
<!--                v-if="item && item.supplierId != null"-->
<!--            />-->
<!--          </el-select>-->
<!--        </el-form-item>-->

        <!-- 改成这样 -->
        <el-form-item label="供应商" prop="supplierId">
          <el-input v-model="formData.supplierId" placeholder="请输入供应商ID"></el-input>
        </el-form-item>

        <el-form-item label="单价" prop="price">
          <el-input-number v-model="formData.price" :precision="2" :min="0"></el-input-number>
        </el-form-item>
        <el-form-item label="规格" prop="specification">
          <el-input v-model="formData.specification"></el-input>
        </el-form-item>
<!--        <el-form-item label="库存" prop="stockQuantity">
          <el-input-number v-model="formData.stockQuantity" :min="0"></el-input-number>
        </el-form-item>-->
        <el-form-item label="库存" prop="stockQuantity">
          <el-input-number v-model="formData.stockQuantity" :min="0" disabled></el-input-number>
        </el-form-item>

        <el-form-item label="库存阈值" prop="stockThreshold">
          <el-input-number v-model="formData.stockThreshold" :min="1"></el-input-number>
        </el-form-item>
        <el-form-item label="产地" prop="origin">
          <el-input v-model="formData.origin"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
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

// 响应式数据
const loading = ref(false);
const tableData = ref([]);
const pageNum = ref(1);
const pageSize = ref(10);
const total = ref(0);
const categories = ref([]);
const suppliers = ref([]);
const dialogVisible = ref(false);
const dialogTitle = ref("");
const formRef = ref();

// 搜索表单
const searchForm = reactive({
  productName: "",
  categoryId: null,
  status: null
});

// 表单数据
const formData = reactive({
  productId: null,
  productName: "",
  categoryId: null,
  supplierId: null,
  price: 0,
  specification: "",
  stockQuantity: 0,
  stockThreshold: 10,
  origin: "",
  remark: "",
  status: 0
});

// 表单验证规则
const formRules = {
  productName: [{ required: true, message: "请输入商品名称", trigger: "blur" }],
  categoryId: [{ required: true, message: "请选择分类", trigger: "change" }],
  price: [{ required: true, message: "请输入单价", trigger: "blur" }]
};

// 加载商品列表
const loadData = () => {

  loading.value = true;

  const params = {
    pageNum: pageNum.value,
    pageSize: pageSize.value,
    productName: searchForm.productName || null,
    categoryId: searchForm.categoryId || null,
    status: searchForm.status
  };
  http.get("/product/list", { params }).then(res => {
    // 处理嵌套两层 data 的情况
    let result = res;
    if (result.data && result.data.data) {
      // 两层 data 的情况：{code, message, data: { data: { list, totalSize } }}
      result = result.data.data;
    } else if (result.data) {
      // 一层 data 的情况：{code, message, data: { list, totalSize }}
      result = result.data;
    }
    tableData.value = result?.list || [];
    total.value = result?.totalSize || 0;
    loading.value = false;
    console.log("商品数据加载成功，共", tableData.value.length, "条");
  }).catch((err) => {
    console.error("加载商品失败", err);
    tableData.value = [];
    loading.value = false;
  });
};

// 加载分类列表
const loadCategories = () => {
  http.get("/category/enabled").then(res => {
    let data = res;
    if (res.data && res.data.data) {
      data = res.data.data;
    } else if (res.data) {
      data = res.data;
    }
    if (Array.isArray(data)) {
      categories.value = data.filter(item => item && item.categoryId != null);
    } else {
      categories.value = [];
    }
    console.log("分类数据加载完成，共", categories.value.length, "条");
  }).catch(() => {
    categories.value = [];
  });
};

// 加载供应商列表
const loadSuppliers = () => {
  http.get("/supplier/all").then(res => {
    let data = res;
    if (res.data && res.data.data) {
      data = res.data.data;
    } else if (res.data) {
      data = res.data;
    }
    if (Array.isArray(data)) {
      suppliers.value = data.filter(item => item && item.supplierId != null);
    } else {
      suppliers.value = [];
    }
    console.log("供应商数据加载完成，共", suppliers.value.length, "条");
  }).catch(() => {
    suppliers.value = [];
  });
};

// 重置搜索
const resetSearch = () => {
  searchForm.productName = "";
  searchForm.categoryId = null;
  searchForm.status = null;
  pageNum.value = 1;
  loadData();
};

// 分页切换
const handleCurrentChange = (val) => {
  pageNum.value = val;
  loadData();
};

// 添加商品
const handleAdd = () => {
  dialogTitle.value = "添加商品";
  Object.assign(formData, {
    productId: null,
    productName: "",
    categoryId: null,
    supplierId: null,
    price: 0,
    specification: "",
    stockQuantity: 0,
    stockThreshold: 10,
    origin: "",
    remark: "",
    status: 0
  });
  dialogVisible.value = true;
};

// 编辑商品
const handleEdit = (row) => {
  dialogTitle.value = "编辑商品";
  Object.assign(formData, row);
  dialogVisible.value = true;
};

// 保存商品
const handleSave = async () => {
  try {
    await formRef.value.validate();

    if (formData.productId) {
      http.put("/product/edit", formData).then(res => {
        if (res.code === "200") {
          ElMessage.success("修改成功");
          dialogVisible.value = false;
          loadData();
        } else {
          ElMessage.error(res.message || "修改失败");
        }
      });
    } else {
      http.post("/product/add", formData).then(res => {
        if (res.code === "200") {
          ElMessage.success("添加成功");
          dialogVisible.value = false;
          loadData();
        } else {
          ElMessage.error(res.message || "添加失败");
        }
      });
    }
  } catch (error) {
    console.error("表单验证失败", error);
  }
};

// 删除商品
const handleDelete = (row) => {
  ElMessageBox.confirm("确定删除该商品吗？", "提示", { type: "warning" }).then(() => {
    http.delete(`/product/delete/${row.productId}`).then(res => {
      if (res.code === "200") {
        ElMessage.success("删除成功");
        loadData();
      } else {
        ElMessage.error(res.message || "删除失败");
      }
    });
  }).catch(() => {});
};


// 切换上下架状态
const toggleStatus = (row) => {
  const newStatus = row.status === 0 ? 1 : 0;
  const updated = { ...row, status: newStatus };
  http.put("/product/edit", updated).then(res => {
    if (res.code === "200") {
      ElMessage.success(newStatus === 0 ? "已上架" : "已下架");
      loadData();
    } else {
      ElMessage.error(res.message || "操作失败");
    }
  });
};

// 页面加载
onMounted(() => {
  loadData();
  loadCategories();
  loadSuppliers();
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