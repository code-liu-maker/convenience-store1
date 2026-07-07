<template>
  <div class="register">
    <el-card class="register-card" shadow="always">
      <h2 class="title">用户注册</h2>
      <el-form ref="formRef" :model="form" :rules="rules">
        <el-form-item prop="loginId">
          <el-input v-model="form.loginId" placeholder="请输入账号" prefix-icon="User" size="large"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" prefix-icon="Lock" size="large" show-password></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input v-model="form.confirmPassword" type="password" placeholder="请确认密码" prefix-icon="Lock" size="large" show-password></el-input>
        </el-form-item>
        <el-form-item prop="userName">
          <el-input v-model="form.userName" placeholder="请输入姓名" prefix-icon="UserFilled" size="large"></el-input>
        </el-form-item>
        <el-form-item prop="mobile">
          <el-input v-model="form.mobile" placeholder="请输入手机号" prefix-icon="Iphone" size="large"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" @click="handleRegister" style="width: 100%">注册</el-button>
        </el-form-item>
        <el-form-item>
          <el-link type="primary" @click="$router.push('/login')" style="float: right">已有账号？立即登录</el-link>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import http from "@/util/http.js";

const router = useRouter();
const formRef = ref();

const form = reactive({
  loginId: "",
  password: "",
  confirmPassword: "",
  userName: "",
  mobile: ""
});

const validateConfirm = (rule, value, callback) => {
  if (value !== form.password) {
    callback(new Error("两次输入的密码不一致"));
  } else {
    callback();
  }
};

const rules = {
  loginId: [{ required: true, message: "请输入账号", trigger: "blur" }],
  password: [{ required: true, message: "请输入密码", trigger: "blur" }],
  confirmPassword: [{ required: true, message: "请确认密码", trigger: "blur" }, { validator: validateConfirm, trigger: "blur" }],
  userName: [{ required: true, message: "请输入姓名", trigger: "blur" }]
};

const handleRegister = async () => {
  await formRef.value.validate();
  http.post("/register", form).then(res => {
    if (res.code === "200") {
      ElMessage.success("注册成功，请登录");
      router.push("/login");
    }
  });
};
</script>

<style scoped>
.register {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  justify-content: center;
  align-items: center;
}
.register-card {
  width: 480px;
  padding: 30px;
}
.title {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}
</style>