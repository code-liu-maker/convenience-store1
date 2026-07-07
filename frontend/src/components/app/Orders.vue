<template>
  <el-card>
    <template #header><span>我的订单</span></template>

    <el-tabs v-model="activeTab" @tab-click="loadData">
      <el-tab-pane label="全部订单" name="all"></el-tab-pane>
      <el-tab-pane label="待接单" name="0"></el-tab-pane>
      <el-tab-pane label="已完成" name="1"></el-tab-pane>
      <el-tab-pane label="已取消" name="2"></el-tab-pane>
    </el-tabs>

    <div v-if="tableData && tableData.length > 0">
      <div v-for="order in tableData" :key="order.orderId" class="order-item">
        <div class="order-header">
          <span>订单号：{{ order.orderNo }}</span>
          <span>下单时间：{{ order.orderTime }}</span>
          <el-tag v-if="order.orderStatus === 0" type="warning">待接单</el-tag>
          <el-tag v-else-if="order.orderStatus === 1" type="success">已完成</el-tag>
          <el-tag v-else type="danger">已取消</el-tag>
        </div>
        <div class="order-items">
          <div v-for="item in (order.items || [])" :key="item.itemId" class="order-item-detail">
            <span>{{ item.productName }}</span>
            <span>×{{ item.quantity }}</span>
            <span>￥{{ item.subtotal }}</span>
          </div>
        </div>
        <div class="order-footer">
          <span>合计：￥{{ order.totalAmount }}</span>
          <el-button v-if="order.orderStatus === 0" size="small" type="danger" @click="cancelOrder(order)">取消订单</el-button>
          <el-button v-if="order.orderStatus === 1" size="small" type="primary" @click="reviewOrder(order)">再次购买</el-button>
        </div>
      </div>
    </div>
    <el-empty v-else description="暂无订单" />
  </el-card>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import http from "@/util/http.js";

const tableData = ref([]);
const activeTab = ref("all");

const loadData = () => {
  http.get("/order/my-list").then(res => {
    let orders = res;
    // 处理可能的数据嵌套
    if (orders.data && orders.data.data) {
      orders = orders.data.data;
    } else if (orders.data) {
      orders = orders.data;
    }
    // 确保是数组
    if (!Array.isArray(orders)) {
      orders = [];
    }
    if (activeTab.value !== "all") {
      orders = orders.filter(o => o.orderStatus === parseInt(activeTab.value));
    }
    tableData.value = orders;
    console.log("订单数据加载成功，共", tableData.value.length, "条");
  }).catch(() => {
    tableData.value = [];
  });
};

const cancelOrder = (order) => {
  ElMessageBox.confirm("确定取消该订单吗？", "提示", { type: "warning" }).then(() => {
    http.put(`/order/status?orderId=${order.orderId}&orderStatus=2`).then(res => {
      if (res.code === "200") {
        ElMessage.success("订单已取消");
        loadData();
      }
    });
  }).catch(() => {});
};

const reviewOrder = (order) => {
  const cart = order.items.map(item => ({
    productId: item.productId,
    productName: item.productName,
    price: item.price,
    quantity: item.quantity
  }));
  sessionStorage.setItem("cart", JSON.stringify(cart));
  window.dispatchEvent(new Event("storage"));
  ElMessage.success("已添加到购物车");
};

onMounted(() => {
  loadData();
});
</script>

<style scoped>
.order-item { border: 1px solid #eee; margin-bottom: 20px; border-radius: 8px; }
.order-header { display: flex; justify-content: space-between; padding: 15px 20px; background: #f5f5f5; border-bottom: 1px solid #eee; }
.order-items { padding: 15px 20px; border-bottom: 1px solid #eee; }
.order-item-detail { display: flex; justify-content: space-between; padding: 5px 0; }
.order-footer { display: flex; justify-content: flex-end; align-items: center; gap: 20px; padding: 15px 20px; background: #fafafa; }
</style>