<template>
  <el-card>
    <template #header>
      <div class="header-toolbar">
        <span>我的购物车</span>
        <el-button type="danger" size="small" @click="clearCart">清空购物车</el-button>
      </div>
    </template>

    <!-- 购物车表格 -->
    <el-table :data="cartItems" stripe v-if="cartItems.length > 0">
      <el-table-column prop="productName" label="商品名称" width="200"></el-table-column>
      <el-table-column prop="price" label="单价" width="120">
        <template #default="scope">￥{{ scope.row.price }}</template>
      </el-table-column>
      <el-table-column label="数量" width="150">
        <template #default="scope">
          <el-input-number v-model="scope.row.quantity" :min="1" :max="99" size="small" @change="saveCart"></el-input-number>
        </template>
      </el-table-column>
      <el-table-column label="小计" width="150">
        <template #default="scope">￥{{ (scope.row.price * scope.row.quantity).toFixed(2) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="100">
        <template #default="scope">
          <el-button size="small" type="danger" @click="removeItem(scope.$index)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-empty v-else description="购物车空空如也，快去逛逛吧~" />

    <!-- 底部操作栏 -->
    <div v-if="cartItems.length > 0" class="cart-footer">
      <span class="total">总金额：<span class="total-price">￥{{ totalAmount.toFixed(2) }}</span></span>
      <el-button type="primary" size="large" @click="submitOrder">提交订单</el-button>
    </div>
  </el-card>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from "vue";
import { useRouter } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
import http from "@/util/http.js";

const router = useRouter();
const cartItems = ref([]);

// 计算总金额
const totalAmount = computed(() => {
  return cartItems.value.reduce((sum, item) => sum + item.price * item.quantity, 0);
});

// 加载购物车
const loadCart = () => {
  try {
    const data = sessionStorage.getItem("cart");
    cartItems.value = data ? JSON.parse(data) : [];
  } catch (e) {
    cartItems.value = [];
  }
};

// 保存购物车
const saveCart = () => {
  try {
    sessionStorage.setItem("cart", JSON.stringify(cartItems.value));
    // 触发storage事件，通知其他组件更新
    window.dispatchEvent(new Event("storage"));
  } catch (e) {
    console.error("保存购物车失败", e);
  }
};

// 清空购物车
const clearCart = () => {
  ElMessageBox.confirm("确定清空购物车吗？", "提示", { type: "warning" })
      .then(() => {
        cartItems.value = [];
        saveCart();
        ElMessage.success("已清空");
      })
      .catch(() => {});
};

// 删除单个商品
const removeItem = (index) => {
  cartItems.value.splice(index, 1);
  saveCart();
  ElMessage.success("已删除");
};

// 提交订单
const submitOrder = () => {
  if (cartItems.value.length === 0) {
    ElMessage.warning("购物车为空");
    return;
  }

  const orderItems = cartItems.value.map(item => ({
    productId: item.productId,
    productName: item.productName,
    price: item.price,
    quantity: item.quantity
  }));

  http.post("/order/add", { items: orderItems })
      .then(res => {
        if (res.code === "200") {
          ElMessage.success("下单成功");
          cartItems.value = [];
          saveCart();
          router.push("/app/orders");
        } else {
          ElMessage.error(res.message || "下单失败");
        }
      })
      .catch(() => {
        ElMessage.error("下单失败，请重试");
      });
};

// 组件挂载时加载数据
onMounted(() => {
  loadCart();
});

// 组件卸载前保存数据（可选）
onBeforeUnmount(() => {
  saveCart();
});
</script>

<style scoped>
.header-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.cart-footer {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}
.total {
  font-size: 18px;
  margin-right: 20px;
}
.total-price {
  color: #f40;
  font-size: 24px;
  font-weight: bold;
}
</style>