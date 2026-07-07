<template>
  <el-container class="admin-container">
    <el-header class="admin-header">
      <div class="logo-area">
        <img src="../../assets/logo.png" class="logo" alt="logo" />
        <span class="title">便利店管理系统</span>
      </div>
      <div class="user-area">
        <span>{{ userInfo.userName }}</span>
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
      <el-aside class="admin-aside" :width="isCollapse ? '64px' : '220px'">
        <div class="collapse-btn" @click="toggleCollapse">
          <el-icon><Fold /></el-icon>
        </div>
        <el-menu :collapse="isCollapse" :collapse-transition="false" background-color="#304156"
                 text-color="#bfcbd9" active-text-color="#409EFF" router>

          <!-- 数据看板：所有人可见 -->
          <el-menu-item index="/admin/dashboard">
            <el-icon><DataLine /></el-icon>
            <span>数据看板</span>
          </el-menu-item>

          <!-- 商品管理：操作员 + 管理员可见 -->
          <el-sub-menu v-if="userInfo.userType === 1 || userInfo.userType === 2" index="product">
            <template #title>
              <el-icon><Goods /></el-icon>
              <span>商品管理</span>
            </template>
            <el-menu-item index="/admin/products">商品列表</el-menu-item>
            <el-menu-item index="/admin/categories">商品分类</el-menu-item>
          </el-sub-menu>

          <!-- 订单管理：操作员 + 管理员可见 -->
          <el-menu-item v-if="userInfo.userType === 1 || userInfo.userType === 2" index="/admin/orders">
            <el-icon><List /></el-icon>
            <span>订单管理</span>
          </el-menu-item>

          <!-- 库存管理：操作员 + 管理员可见 -->
          <el-sub-menu v-if="userInfo.userType === 1 || userInfo.userType === 2" index="stock">
            <template #title>
              <el-icon><Box /></el-icon>
              <span>库存管理</span>
            </template>
            <el-menu-item index="/admin/stock">库存查询</el-menu-item>
            <!-- 库存日志：仅管理员可见 -->
            <el-menu-item v-if="userInfo.userType === 2" index="/admin/stocklog">库存日志</el-menu-item>
          </el-sub-menu>

          <!-- 供应商管理：仅管理员可见 -->
          <el-menu-item v-if="userInfo.userType === 2" index="/admin/suppliers">
            <el-icon><Van /></el-icon>
            <span>供应商管理</span>
          </el-menu-item>

          <!-- 账号管理：仅管理员可见 -->
          <el-menu-item v-if="userInfo.userType === 2" index="/admin/users">
            <el-icon><User /></el-icon>
            <span>账号管理</span>
          </el-menu-item>

          <!-- 个人信息：所有人可见 -->
          <el-menu-item index="/admin/userinfo">
            <el-icon><Avatar /></el-icon>
            <span>个人信息</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main class="admin-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";

const router = useRouter();
const isCollapse = ref(false);

const userInfo = reactive({
  userName: "",
  userType: 0
});

onMounted(() => {
  const info = sessionStorage.getItem("userInfo");
  if (info) {
    Object.assign(userInfo, JSON.parse(info));
  }
});

const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value;
};

const handleCommand = (command) => {
  if (command === "profile") {
    router.push("/admin/userinfo");
  } else if (command === "logout") {
    sessionStorage.removeItem("userInfo");
    ElMessage.success("已退出登录");
    router.push("/login");
  }
};
</script>

<style scoped>
.admin-container {
  width: 100%;
  height: 100%;
}
.admin-header {
  background: #304156;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  height: 60px;
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
  color: #fff;
}
.user-area {
  display: flex;
  align-items: center;
  gap: 15px;
  color: #fff;
}
.admin-aside {
  background: #304156;
  transition: width 0.3s;
  overflow-x: hidden;
}
.collapse-btn {
  text-align: center;
  color: #fff;
  padding: 15px 0;
  cursor: pointer;
  border-bottom: 1px solid #40556b;
}
.el-menu {
  border-right: none;
}
.admin-main {
  background: #f0f2f5;
  padding: 20px;
}
</style>