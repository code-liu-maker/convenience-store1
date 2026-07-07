<template>
  <div class="login">
    <el-card class="login-card" shadow="always">
      <h2 class="title">便利店后台管理系统</h2>
      <el-form ref="formRef" :model="form" :rules="rules">
        <el-form-item prop="loginId">
          <el-input v-model="form.loginId" placeholder="请输入账号" prefix-icon="User" size="large"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" prefix-icon="Lock" size="large" show-password></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" @click="handleLogin" style="width: 100%">登录</el-button>
        </el-form-item>
        <el-form-item>
          <el-link type="primary" @click="$router.push('/register')" style="float: right">没有账号？立即注册</el-link>
        </el-form-item>
      </el-form>
      <div class="demo-tips">
        <p>测试账号：</p>
        <p>管理员：admin / 123456</p>
        <p>操作员：operator / 123456</p>
        <p>顾客：customer / 123456</p>
      </div>
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
  password: ""
});

const rules = {
  loginId: [{ required: true, message: "请输入账号", trigger: "blur" }],
  password: [{ required: true, message: "请输入密码", trigger: "blur" }]
};

const handleLogin = async () => {
  await formRef.value.validate();
  http.post("/login", form).then(res => {
    if (res.code === "200") {
      const userInfo = res.data.user;
      sessionStorage.setItem("userInfo", JSON.stringify(userInfo));
      ElMessage.success("登录成功");
      // 操作员和管理员都去后台
      if (userInfo.userType === 1 || userInfo.userType === 2) {
        router.push("/admin/dashboard");
      } else {
        router.push("/app/home");
      }
    }
  });
};
</script>

<style scoped>
.login {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  justify-content: center;
  align-items: center;
}
.login-card {
  width: 450px;
  padding: 30px;
}
.title {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}
.demo-tips {
  margin-top: 20px;
  padding: 10px;
  background: #f5f5f5;
  border-radius: 4px;
  font-size: 12px;
  color: #666;
}
.demo-tips p {
  margin: 5px 0;
}
</style>