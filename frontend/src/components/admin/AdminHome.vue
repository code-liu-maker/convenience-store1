<template>
  <div class="admin-home">
    <el-card>
      <h2>欢迎使用便利店后台管理系统</h2>
      <p>当前时间：{{ currentTime }}</p>
      <p>登录用户：{{ userInfo.userName }}</p>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from "vue";
import dayjs from "dayjs";

const currentTime = ref(dayjs().format("YYYY-MM-DD HH:mm:ss"));
const userInfo = reactive({ userName: "" });

let timer = null;

onMounted(() => {
  const info = sessionStorage.getItem("userInfo");
  if (info) {
    Object.assign(userInfo, JSON.parse(info));
  }
  timer = setInterval(() => {
    currentTime.value = dayjs().format("YYYY-MM-DD HH:mm:ss");
  }, 1000);
});

onUnmounted(() => {
  if (timer) clearInterval(timer);
});
</script>

<style scoped>
.admin-home {
  text-align: center;
  padding: 50px;
}
</style>