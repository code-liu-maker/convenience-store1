<template>
  <el-card>
    <template #header>
      <span>个人中心</span>
    </template>

    <div v-if="userInfo && userInfo.userId" class="profile-container">
      <div class="profile-avatar">
        <el-avatar :size="120" icon="UserFilled" />
        <h3>{{ userInfo.userName || "未设置" }}</h3>
        <p class="user-role">
          <el-tag v-if="userInfo.userType === 0" type="info">普通顾客</el-tag>
          <el-tag v-else-if="userInfo.userType === 1" type="warning">门店操作员</el-tag>
          <el-tag v-else-if="userInfo.userType === 2" type="danger">系统管理员</el-tag>
          <el-tag v-else type="info">未知角色</el-tag>
        </p>
      </div>

      <div class="profile-info">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="账号">{{ userInfo.loginId || "未设置" }}</el-descriptions-item>
          <el-descriptions-item label="姓名">{{ userInfo.userName || "未设置" }}</el-descriptions-item>
          <el-descriptions-item label="手机号">{{ userInfo.mobile || "未设置" }}</el-descriptions-item>
          <el-descriptions-item label="邮箱">{{ userInfo.email || "未设置" }}</el-descriptions-item>
        </el-descriptions>

        <div class="profile-actions">
          <el-button type="primary" @click="goToEdit">编辑个人信息</el-button>
          <el-button type="warning" @click="changePassword">修改密码</el-button>
        </div>
      </div>
    </div>

    <el-empty v-else description="请先登录" />

    <!-- 修改密码对话框 -->
    <el-dialog v-model="passwordDialogVisible" title="修改密码" width="400px">
      <el-form :model="passwordForm" :rules="passwordRules" ref="passwordFormRef" label-width="100px">
        <el-form-item label="原密码" prop="oldPassword">
          <el-input type="password" v-model="passwordForm.oldPassword" placeholder="请输入原密码"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input type="password" v-model="passwordForm.newPassword" placeholder="请输入新密码"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input type="password" v-model="passwordForm.confirmPassword" placeholder="请确认新密码"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="passwordDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="savePassword">确认</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { reactive, ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";

const router = useRouter();

// 用户信息
const userInfo = reactive({
  userId: null,
  loginId: "",
  userName: "",
  mobile: "",
  email: "",
  userType: 0
});

// 密码对话框
const passwordDialogVisible = ref(false);
const passwordFormRef = ref();

const passwordForm = reactive({
  oldPassword: "",
  newPassword: "",
  confirmPassword: ""
});

// 密码验证规则
const validateConfirm = (rule, value, callback) => {
  if (value !== passwordForm.newPassword) {
    callback(new Error("两次输入的密码不一致"));
  } else {
    callback();
  }
};

const passwordRules = {
  oldPassword: [{ required: true, message: "请输入原密码", trigger: "blur" }],
  newPassword: [{ required: true, message: "请输入新密码", trigger: "blur" }],
  confirmPassword: [
    { required: true, message: "请确认密码", trigger: "blur" },
    { validator: validateConfirm, trigger: "blur" }
  ]
};

// 加载用户信息
const loadUserInfo = () => {
  try {
    const info = sessionStorage.getItem("userInfo");
    if (info) {
      const parsed = JSON.parse(info);
      Object.assign(userInfo, parsed);
    }
  } catch (e) {
    console.error("加载用户信息失败", e);
  }
};

// 跳转到编辑页面
const goToEdit = () => {
  router.push("/app/userinfo");
};

// 修改密码
const changePassword = () => {
  passwordDialogVisible.value = true;
};

// 保存密码
const savePassword = async () => {
  try {
    await passwordFormRef.value.validate();
    ElMessage.success("密码修改成功");
    passwordDialogVisible.value = false;
    // 清空表单
    passwordForm.oldPassword = "";
    passwordForm.newPassword = "";
    passwordForm.confirmPassword = "";
  } catch (error) {
    console.error("密码验证失败", error);
  }
};

// 页面加载
onMounted(() => {
  loadUserInfo();
});
</script>

<style scoped>
.profile-container {
  display: flex;
  gap: 40px;
  padding: 20px;
}
.profile-avatar {
  text-align: center;
  flex-shrink: 0;
}
.profile-info {
  flex: 1;
}
.profile-actions {
  margin-top: 20px;
  display: flex;
  gap: 15px;
}
.user-role {
  margin-top: 10px;
}
</style>