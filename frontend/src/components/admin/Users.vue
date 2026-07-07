<template>
  <el-card>
    <template #header>
      <div class="header-toolbar">
        <span>账号管理</span>
        <el-button type="primary" size="small" @click="handleAdd">添加账号</el-button>
      </div>
    </template>

    <el-table :data="tableData" stripe v-loading="loading" border style="width: 100%">
      <el-table-column prop="loginId" label="账号" width="120" align="center"></el-table-column>
      <el-table-column prop="userName" label="姓名" width="120" align="center"></el-table-column>
      <el-table-column prop="mobile" label="手机号" width="130" align="center"></el-table-column>
      <el-table-column prop="email" label="邮箱" width="160" align="center">
        <template #default="scope">
          <span v-if="scope.row.email">{{ scope.row.email }}</span>
          <span v-else style="color: #ccc;">未设置</span>
        </template>
      </el-table-column>
      <el-table-column prop="userType" label="角色" width="90" align="center">
        <template #default="scope">
          <el-tag v-if="scope.row.userType === 2" type="danger" size="small">管理员</el-tag>
          <el-tag v-else-if="scope.row.userType === 1" type="warning" size="small">操作员</el-tag>
          <el-tag v-else type="info" size="small">顾客</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="80" align="center">
        <template #default="scope">
          <el-tag v-if="scope.row.status === 0" type="success" size="small">正常</el-tag>
          <el-tag v-else type="danger" size="small">禁用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="280" align="center" fixed="right">
        <template #default="scope">
          <el-button size="small" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="small" :type="scope.row.status === 0 ? 'warning' : 'success'" plain @click="toggleStatus(scope.row)">
            {{ scope.row.status === 0 ? "禁用" : "启用" }}
          </el-button>
          <el-button size="small" type="info" plain @click="resetPassword(scope.row)">重置密码</el-button>
          <el-button size="small" type="danger" plain @click="handleDelete(scope.row)">删除</el-button>
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

    <!-- 添加/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="80px">
        <el-form-item label="账号" prop="loginId" v-if="!formData.userId">
          <el-input v-model="formData.loginId" placeholder="请输入账号"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!formData.userId">
          <el-input type="password" v-model="formData.password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="userName">
          <el-input v-model="formData.userName" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="formData.mobile" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="formData.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item label="角色" prop="userType">
          <el-radio-group v-model="formData.userType">
            <el-radio :label="0">顾客</el-radio>
            <el-radio :label="1">操作员</el-radio>
            <el-radio :label="2">管理员</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio :label="0">正常</el-radio>
            <el-radio :label="1">禁用</el-radio>
          </el-radio-group>
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

const formData = reactive({
  userId: null,
  loginId: "",
  password: "",
  userName: "",
  mobile: "",
  email: "",
  userType: 0,
  status: 0
});

const formRules = {
  loginId: [{ required: true, message: "请输入账号", trigger: "blur" }],
  password: [{ required: true, message: "请输入密码", trigger: "blur" }],
  userName: [{ required: true, message: "请输入姓名", trigger: "blur" }]
};

const loadData = () => {
  loading.value = true;
  http.get(`/admin/user/list?pageNum=${pageNum.value}&pageSize=${pageSize.value}`).then(res => {
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

const handleCurrentChange = (val) => {
  pageNum.value = val;
  loadData();
};

const handleAdd = () => {
  dialogTitle.value = "添加账号";
  Object.assign(formData, {
    userId: null,
    loginId: "",
    password: "",
    userName: "",
    mobile: "",
    email: "",
    userType: 0,
    status: 0
  });
  dialogVisible.value = true;
};

const handleEdit = (row) => {
  dialogTitle.value = "编辑账号";
  Object.assign(formData, {
    userId: row.userId,
    loginId: row.loginId,
    password: "",
    userName: row.userName,
    mobile: row.mobile,
    email: row.email,
    userType: row.userType,
    status: row.status
  });
  dialogVisible.value = true;
};

const handleSave = async () => {
  try {
    await formRef.value.validate();
    if (formData.userId) {
      const updateData = {
        userId: formData.userId,
        userName: formData.userName,
        mobile: formData.mobile,
        email: formData.email,
        userType: formData.userType,
        status: formData.status
      };
      http.put("/admin/user/edit", updateData).then(res => {
        if (res.code === "200") {
          ElMessage.success("修改成功");
          dialogVisible.value = false;
          loadData();
        }
      });
    } else {
      http.post("/admin/user/add", formData).then(res => {
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
  ElMessageBox.confirm(`确定删除账号"${row.loginId}"吗？`, "提示", { type: "warning" }).then(() => {
    http.delete(`/admin/user/delete/${row.userId}`).then(res => {
      if (res.code === "200") {
        ElMessage.success("删除成功");
        loadData();
      }
    });
  }).catch(() => {});
};

const toggleStatus = (row) => {
  const newStatus = row.status === 0 ? 1 : 0;
  const updated = { ...row, status: newStatus };
  http.put("/admin/user/edit", updated).then(res => {
    if (res.code === "200") {
      ElMessage.success(newStatus === 0 ? "已启用" : "已禁用");
      loadData();
    }
  });
};

const resetPassword = (row) => {
  ElMessageBox.confirm(`确定重置账号"${row.loginId}"的密码为123456吗？`, "提示", { type: "warning" }).then(() => {
    http.put(`/admin/user/reset-password?userId=${row.userId}`).then(res => {
      if (res.code === "200") {
        ElMessage.success("密码已重置为123456");
      }
    });
  }).catch(() => {});
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
.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>