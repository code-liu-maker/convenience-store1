<template>
  <div>
    <!-- 搜索栏 -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="商品名称">
          <el-input v-model="searchForm.productName" placeholder="请输入商品名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="商品分类">
          <el-select v-model="searchForm.categoryId" placeholder="请选择分类" clearable>
            <el-option
                v-for="item in categories"
                :key="item.categoryId"
                :label="item.categoryName"
                :value="item.categoryId"
                v-if="item && item.categoryId != null"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 商品列表 -->
    <el-row :gutter="20" class="product-list">
      <el-col :span="6" v-for="item in tableData" :key="item.productId">
        <div class="product-item" @click="viewDetail(item)">
          <div class="product-image">📦</div>
          <div class="product-name">{{ item.productName }}</div>
          <div class="product-price">￥{{ item.price }}</div>
          <div class="product-stock">库存：{{ item.stockQuantity }}</div>
          <el-button type="primary" size="small" @click.stop="addToCart(item)">加入购物车</el-button>
        </div>
      </el-col>
    </el-row>

    <el-empty v-if="tableData.length === 0 && !loading" description="暂无商品" />

    <!-- 分页 -->
    <el-pagination
        v-if="total > 0"
        @current-change="handleCurrentChange"
        v-model:current-page="pageNum"
        :page-size="pageSize"
        :total="total"
        layout="total, prev, pager, next"
        class="pagination"
    ></el-pagination>

    <!-- 商品详情对话框 -->
    <el-dialog v-model="detailVisible" :title="currentProduct.productName" width="500px">
      <div class="product-detail">
        <div class="detail-image">📦</div>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="商品名称">{{ currentProduct.productName }}</el-descriptions-item>
          <el-descriptions-item label="分类">{{ currentProduct.categoryName }}</el-descriptions-item>
          <el-descriptions-item label="单价">￥{{ currentProduct.price }}</el-descriptions-item>
          <el-descriptions-item label="规格">{{ currentProduct.specification || "标准" }}</el-descriptions-item>
          <el-descriptions-item label="产地">{{ currentProduct.origin || "中国" }}</el-descriptions-item>
          <el-descriptions-item label="库存">{{ currentProduct.stockQuantity }}</el-descriptions-item>
          <el-descriptions-item label="备注">{{ currentProduct.remark || "无" }}</el-descriptions-item>
        </el-descriptions>
        <div class="detail-actions">
          <el-input-number v-model="buyQuantity" :min="1" :max="currentProduct.stockQuantity"></el-input-number>
          <el-button type="primary" @click="buyNow">立即购买</el-button>
          <el-button type="success" @click="addToCart(currentProduct)">加入购物车</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import http from "@/util/http.js";

const route = useRoute();
const router = useRouter();
const loading = ref(false);
const tableData = ref([]);
const categories = ref([]);
const pageNum = ref(1);
const pageSize = ref(8);
const total = ref(0);
const detailVisible = ref(false);
const currentProduct = ref({});
const buyQuantity = ref(1);

const searchForm = reactive({
  productName: "",
  categoryId: null
});

// 加载分类列表
const loadCategories = () => {
  http.get("/category/enabled").then(res => {
    let data = res;
    if (data.data && data.data.data) data = data.data.data;
    else if (data.data) data = data.data;
    if (Array.isArray(data)) {
      categories.value = data.filter(item => item && item.categoryId != null);
    } else {
      categories.value = [];
    }
  }).catch(() => {
    categories.value = [];
  });
};

// 加载商品列表
const loadData = () => {
  loading.value = true;
  const params = {
    pageNum: pageNum.value,
    pageSize: pageSize.value,
    productName: searchForm.productName || null,
    categoryId: searchForm.categoryId || null,
    status: 0  // 只显示上架商品
  };
  http.get("/product/list", { params }).then(res => {
    // 处理两层 data 嵌套
    let result = res;
    if (result.data && result.data.data) {
      result = result.data.data;
    } else if (result.data) {
      result = result.data;
    }
    tableData.value = result?.list || [];
    total.value = result?.totalSize || 0;
    loading.value = false;
    console.log("商品数据加载成功，共", tableData.value.length, "条");
  }).catch(() => {
    tableData.value = [];
    loading.value = false;
  });
};

// 重置搜索
const resetSearch = () => {
  searchForm.productName = "";
  searchForm.categoryId = null;
  pageNum.value = 1;
  loadData();
};

// 分页切换
const handleCurrentChange = (val) => {
  pageNum.value = val;
  loadData();
};

// 查看商品详情
const viewDetail = async (item) => {
  const res = await http.get(`/product/${item.productId}`);
  let product = res;
  if (product.data && product.data.data) product = product.data.data;
  else if (product.data) product = product.data;
  currentProduct.value = product;
  buyQuantity.value = 1;
  detailVisible.value = true;
};

// 加入购物车
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
  detailVisible.value = false;
};

// 立即购买
const buyNow = () => {
  const item = currentProduct.value;
  const cart = [{ productId: item.productId, productName: item.productName, price: item.price, quantity: buyQuantity.value }];
  sessionStorage.setItem("cart", JSON.stringify(cart));
  window.dispatchEvent(new Event("storage"));
  router.push("/app/cart");
};

// 页面加载
onMounted(() => {
  loadCategories();
  loadData();

  // 如果有商品ID参数，直接打开详情
  if (route.query.productId) {
    http.get(`/product/${route.query.productId}`).then(res => {
      let product = res;
      if (product.data && product.data.data) product = product.data.data;
      else if (product.data) product = product.data;
      currentProduct.value = product;
      detailVisible.value = true;
    });
  }
});
</script>

<style scoped>
.search-card {
  margin-bottom: 20px;
}
.product-list {
  margin-top: 20px;
}
.product-item {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  text-align: center;
  margin-bottom: 20px;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
.product-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.15);
}
.product-image {
  font-size: 60px;
  margin-bottom: 10px;
}
.product-name {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 10px;
}
.product-price {
  color: #f40;
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 10px;
}
.product-stock {
  color: #999;
  font-size: 12px;
  margin-bottom: 15px;
}
.pagination {
  margin-top: 20px;
  text-align: center;
}
.product-detail {
  text-align: center;
}
.detail-image {
  font-size: 80px;
  margin-bottom: 20px;
}
.detail-actions {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  gap: 10px;
}
</style>