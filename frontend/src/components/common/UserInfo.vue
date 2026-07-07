<template>
  <el-card>
    <template #header>
      <span>个人信息</span>
    </template>

    <el-form :model="formData" :rules="formRules" ref="formRef" label-width="100px" class="user-info-form">
      <el-form-item label="账号">
        <el-input v-model="formData.loginId" disabled></el-input>
      </el-form-item>
      <el-form-item label="姓名" prop="userName">
        <el-input v-model="formData.userName"></el-input>
      </el-form-item>
      <el-form-item label="手机号" prop="mobile">
        <el-input v-model="formData.mobile"></el-input>
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="formData.email"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="saveInfo">保存修改</el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup>
import { reactive, onMounted, ref } from "vue";
import { ElMessage } from "element-plus";
import http from "@/util/http.js";

const formRef = ref();

const formData = reactive({
  userId: null,
  loginId: "",
  userName: "",
  mobile: "",
  email: "",
  userType: 0,
  status: 0
});

const formRules = {
  userName: [{ required: true, message: "请输入姓名", trigger: "blur" }],
  mobile: [{ pattern: /^1[3-9]\d{9}$/, message: "请输入正确的手机号", trigger: "blur" }],
  email: [{ type: "email", message: "请输入正确的邮箱", trigger: "blur" }]
};

const loadUserInfo = () => {
  const info = sessionStorage.getItem("userInfo");
  if (info) {
    const user = JSON.parse(info);
    Object.assign(formData, user);
  }
};

const saveInfo = async () => {
  await formRef.value.validate();
  http.put("/admin/user/edit", formData).then(res => {
    if (res.code === "200") {
      sessionStorage.setItem("userInfo", JSON.stringify(formData));
      ElMessage.success("保存成功");
    }
  });
};

const resetForm = () => {
  loadUserInfo();
};

onMounted(() => {
  loadUserInfo();
});
</script>

<style scoped>
.user-info-form {
  max-width: 500px;
  margin: 0 auto;
}
</style>