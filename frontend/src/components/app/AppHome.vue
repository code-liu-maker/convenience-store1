<template>
  <div>
    <el-carousel height="300px" class="carousel">
      <el-carousel-item v-for="(item, index) in banners" :key="index">
        <div class="banner" :style="{ backgroundImage: `url(${item})` }"></div>
      </el-carousel-item>
    </el-carousel>

    <el-row :gutter="20" class="features">
      <el-col :span="6" v-for="feature in features" :key="feature.title">
        <el-card class="feature-card" shadow="hover">
          <div class="feature-icon"><el-icon :size="48"><component :is="feature.icon" /></el-icon></div>
          <div class="feature-title">{{ feature.title }}</div>
          <div class="feature-desc">{{ feature.desc }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="hot-products">
      <template #header><span>热销商品推荐</span></template>
      <el-row :gutter="20">
        <el-col :span="6" v-for="item in hotProducts" :key="item.productId">
          <div class="product-card" @click="viewProduct(item.productId)">
            <div class="product-image">📦</div>
            <div class="product-name">{{ item.productName }}</div>
            <div class="product-price">￥{{ item.price }}</div>
            <el-button type="primary" size="small" @click.stop="addToCart(item)">加入购物车</el-button>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import http from "@/util/http.js";

const router = useRouter();
const banners = ref([
  "https://picsum.photos/1200/300?random=1",
  "https://picsum.photos/1200/300?random=2",
  "https://picsum.photos/1200/300?random=3"
]);

const features = ref([
  { icon: "ShoppingCart", title: "精选好物", desc: "品质保证，放心选购" },
  { icon: "Truck", title: "快速配送", desc: "极速送达，服务到家" },
  { icon: "Service", title: "贴心服务", desc: "7x24小时在线客服" },
  { icon: "Discount", title: "优惠多多", desc: "每日特价，惊喜不断" }
]);

const hotProducts = ref([]);

const viewProduct = (id) => {
  router.push({ path: "/app/products", query: { productId: id } });
};

const addToCart = (item) => {
  let cart = JSON.parse(sessionStorage.getItem("cart") || "[]");
  const exist = cart.find(c => c.productId === item.productId);
  if (exist) {
    exist.quantity++;
  } else {
    cart.push({ productId: item.productId, productName: item.productName, price: item.price, quantity: 1 });
  }
  sessionStorage.setItem("cart", JSON.stringify(cart));
  window.dispatchEvent(new Event("storage"));
  ElMessage.success("已加入购物车");
};

onMounted(() => {
  http.get("/product/list?pageNum=1&pageSize=8&status=0").then(res => {
    let result = res;
    if (result.data && result.data.data) result = result.data.data;
    else if (result.data) result = result.data;
    hotProducts.value = result?.list || [];
  }).catch(() => {
    hotProducts.value = [];
  });
});
</script>

<style scoped>
.carousel { margin-bottom: 30px; }
.banner { width: 100%; height: 100%; background-size: cover; background-position: center; }
.features { margin-bottom: 30px; }
.feature-card { text-align: center; padding: 20px; cursor: pointer; }
.feature-icon { margin-bottom: 15px; color: #409eff; }
.feature-title { font-size: 18px; font-weight: bold; margin-bottom: 10px; }
.feature-desc { color: #999; }
.product-card { text-align: center; padding: 20px; background: #fff; border-radius: 8px; cursor: pointer; transition: transform 0.3s; }
.product-card:hover { transform: translateY(-5px); box-shadow: 0 5px 20px rgba(0,0,0,0.1); }
.product-image { font-size: 60px; margin-bottom: 10px; }
.product-name { font-size: 16px; font-weight: bold; margin-bottom: 10px; }
.product-price { color: #f40; font-size: 18px; font-weight: bold; margin-bottom: 15px; }
</style>