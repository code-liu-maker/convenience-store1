<template>
  <el-container class="app-container">
    <el-header class="app-header">
      <div class="logo-area">
        <img src="../../assets/logo.png" class="logo" alt="logo" />
        <span class="title">便利店商城</span>
      </div>
      <div class="user-area">
        <span>欢迎，{{ userInfo.userName }}</span>
        <el-badge :value="cartCount" :hidden="cartCount === 0" class="cart-badge">
          <el-button type="primary" :icon="ShoppingCart" circle @click="goToCart"></el-button>
        </el-badge>
        <el-dropdown @command="handleCommand">
          <el-avatar :size="32" icon="UserFilled" />
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">个人信息</el-dropdown-item>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>
    <el-container>
      <el-aside class="app-aside" width="200px">
        <el-menu background-color="#fff" active-text-color="#409EFF" router>
          <el-menu-item index="/app/home">
            <el-icon><HomeFilled /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="/app/products">
            <el-icon><Goods /></el-icon>
            <span>商品浏览</span>
          </el-menu-item>
          <el-menu-item index="/app/orders">
            <el-icon><List /></el-icon>
            <span>我的订单</span>
          </el-menu-item>
          <el-menu-item index="/app/profile">
            <el-icon><User /></el-icon>
            <span>个人中心</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main class="app-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { ShoppingCart } from "@element-plus/icons-vue";

const router = useRouter();
const cartCount = ref(0);
const userInfo = reactive({ userName: "" });

const updateCartCount = () => {
  const cart = JSON.parse(sessionStorage.getItem("cart") || "[]");
  cartCount.value = cart.reduce((sum, item) => sum + item.quantity, 0);
};

const goToCart = () => {
  router.push("/app/cart");
};

const handleCommand = (command) => {
  if (command === "profile") {
    router.push("/app/userinfo");
  } else if (command === "logout") {
    sessionStorage.removeItem("userInfo");
    sessionStorage.removeItem("cart");
    ElMessage.success("已退出登录");
    router.push("/login");
  }
};

onMounted(() => {
  const info = sessionStorage.getItem("userInfo");
  if (info) {
    Object.assign(userInfo, JSON.parse(info));
  }
  updateCartCount();
  window.addEventListener("storage", updateCartCount);
});

// 监听购物车变化
watch(cartCount, () => {});
</script>

<style scoped>
.app-container {
  width: 100%;
  height: 100%;
}
.app-header {
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}
.logo-area {
  display: flex;
  align-items: center;
}
.logo {
  width: 40px;
  height: 40px;
  margin-right: 10px;
}
.title {
  font-size: 20px;
  font-weight: bold;
  color: #409eff;
}
.user-area {
  display: flex;
  align-items: center;
  gap: 15px;
}
.cart-badge {
  margin-right: 10px;
}
.app-aside {
  background: #fff;
  border-right: 1px solid #e6e6e6;
}
.app-main {
  background: #f5f5f5;
  padding: 20px;
}
.el-menu {
  border-right: none;
}
</style>