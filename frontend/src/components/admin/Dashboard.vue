<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-icon"><el-icon><Goods /></el-icon></div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.productCount }}</div>
            <div class="stat-label">商品总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-icon"><el-icon><List /></el-icon></div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.orderCount }}</div>
            <div class="stat-label">订单总数</div>
          </div>
        </el-card>
      </el-col>
      <!-- 用户总数：仅管理员可见 -->
      <el-col v-if="isAdmin" :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-icon"><el-icon><User /></el-icon></div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.userCount }}</div>
            <div class="stat-label">用户总数</div>
          </div>
        </el-card>
      </el-col>
      <!-- 供应商数：仅管理员可见 -->
      <el-col v-if="isAdmin" :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-icon"><el-icon><Van /></el-icon></div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.supplierCount }}</div>
            <div class="stat-label">供应商数</div>
          </div>
        </el-card>
      </el-col>
      <!-- 如果操作员登录，用空列占位保持布局 -->
      <el-col v-if="!isAdmin" :span="12"></el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card>
          <template #header>
            <span>库存预警商品</span>
          </template>
          <el-table :data="lowStockProducts" stripe v-if="lowStockProducts.length > 0">
            <el-table-column prop="productName" label="商品名称"></el-table-column>
            <el-table-column prop="categoryName" label="分类"></el-table-column>
            <el-table-column prop="stockQuantity" label="当前库存" width="120">
              <template #default="scope">
                <span style="color: red; font-weight: bold">{{ scope.row.stockQuantity }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="stockThreshold" label="预警阈值" width="120"></el-table-column>
            <el-table-column label="操作" width="120">
              <template #default="scope">
                <el-button size="small" type="primary" @click="$router.push('/admin/stock')">处理</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div v-else class="empty-tip">暂无库存预警商品</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from "vue";
import http from "@/util/http.js";

const stats = reactive({
  productCount: 0,
  orderCount: 0,
  userCount: 0,
  supplierCount: 0
});
const lowStockProducts = ref([]);

// 判断当前用户是否是管理员
const userInfo = JSON.parse(sessionStorage.getItem('userInfo') || '{}');
const isAdmin = userInfo.userType === 2;

onMounted(async () => {
  try {
    // 获取商品总数（所有人都可以）
    const productRes = await http.get("/product/list?pageNum=1&pageSize=1");
    const productData = productRes.data?.data || productRes;
    stats.productCount = productData?.totalSize || 0;
  } catch (e) {
    console.log("获取商品数失败", e);
    stats.productCount = 0;
  }

  try {
    // 获取订单总数（所有人都可以）
    const orderRes = await http.get("/order/admin/list?pageNum=1&pageSize=1");
    const orderData = orderRes.data?.data || orderRes;
    stats.orderCount = orderData?.totalSize || 0;
  } catch (e) {
    console.log("获取订单数失败", e);
    stats.orderCount = 0;
  }

  // 只有管理员才获取用户总数和供应商总数
  if (isAdmin) {
    try {
      const userRes = await http.get("/admin/user/list?pageNum=1&pageSize=1");
      const userData = userRes.data?.data || userRes;
      stats.userCount = userData?.totalSize || 0;
    } catch (e) {
      console.log("获取用户数失败", e);
      stats.userCount = 0;
    }

    try {
      const supplierRes = await http.get("/supplier/list?pageNum=1&pageSize=1");
      const supplierData = supplierRes.data?.data || supplierRes;
      stats.supplierCount = supplierData?.totalSize || 0;
    } catch (e) {
      console.log("获取供应商数失败", e);
      stats.supplierCount = 0;
    }
  }

  try {
    // 获取库存预警商品（所有人都可以）
    const lowRes = await http.get("/product/low-stock");
    lowStockProducts.value = lowRes.data?.data || lowRes.data || [];
  } catch (e) {
    console.log("获取预警商品失败", e);
    lowStockProducts.value = [];
  }
});
</script>

<style scoped>
.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
  cursor: pointer;
}
.stat-icon {
  font-size: 48px;
  color: #409eff;
  margin-right: 20px;
}
.stat-info {
  flex: 1;
}
.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #333;
}
.stat-label {
  font-size: 14px;
  color: #999;
  margin-top: 5px;
}
.empty-tip {
  text-align: center;
  padding: 40px;
  color: #999;
}
</style>