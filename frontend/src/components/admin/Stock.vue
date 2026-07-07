<template>
  <el-card>
    <template #header>
      <span>库存管理</span>
    </template>

    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="商品名称">
        <el-input v-model="searchForm.productName" placeholder="请输入商品名称" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadData">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="tableData" stripe v-loading="loading">
      <el-table-column prop="productName" label="商品名称" width="180"></el-table-column>
      <el-table-column prop="categoryName" label="分类" width="120"></el-table-column>
      <el-table-column prop="price" label="单价" width="100">
        <template #default="scope">￥{{ scope.row.price }}</template>
      </el-table-column>
      <el-table-column prop="stockQuantity" label="当前库存" width="120">
        <template #default="scope">
          <span :style="{ color: scope.row.stockQuantity < scope.row.stockThreshold ? 'red' : 'inherit', fontWeight: 'bold' }">
            {{ scope.row.stockQuantity }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="stockThreshold" label="预警阈值" width="100"></el-table-column>
      <el-table-column label="操作" width="250">
        <template #default="scope">
          <el-button size="small" type="success" @click="openStockDialog(scope.row, 'in')">入库</el-button>
          <el-button size="small" type="warning" @click="openStockDialog(scope.row, 'out')">出库</el-button>
          <el-button size="small" @click="viewLog(scope.row)">日志</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
        @current-change="handleCurrentChange"
        v-model:current-page="pageNum"
        :page-size="pageSize"
        :total="total"
        layout="total, prev, pager, next"
        class="pagination">
    </el-pagination>

    <!-- 出入库对话框 -->
    <el-dialog v-model="stockDialogVisible" :title="stockDialogTitle" width="400px">
      <el-form :model="stockForm" :rules="stockRules" ref="stockFormRef" label-width="80px">
        <el-form-item label="商品名称">
          <el-input :value="stockForm.productName" disabled></el-input>
        </el-form-item>
        <el-form-item label="当前库存">
          <el-input :value="stockForm.currentStock" disabled></el-input>
        </el-form-item>
        <el-form-item label="数量" prop="quantity">
          <el-input-number v-model="stockForm.quantity" :min="1" :max="stockForm.operationType === 'out' ? stockForm.currentStock : 9999"></el-input-number>
        </el-form-item>
        <el-form-item label="备注">
          <el-input type="textarea" v-model="stockForm.remark"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="stockDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="onStockSubmit">确认</el-button>
      </template>
    </el-dialog>

    <!-- 库存日志对话框 -->
    <el-dialog v-model="logDialogVisible" title="库存日志" width="700px">
      <el-table :data="stockLogs" stripe>
        <el-table-column prop="operationType" label="类型" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.operationType === 0 ? 'success' : 'warning'">
              {{ scope.row.operationType === 0 ? "入库" : "出库" }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="quantity" label="数量" width="80"></el-table-column>
        <el-table-column prop="operator" label="操作人" width="120"></el-table-column>
        <el-table-column prop="remark" label="备注"></el-table-column>
        <el-table-column prop="operationTime" label="操作时间" width="180"></el-table-column>
      </el-table>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { reactive, ref, onMounted } from "vue";
import { ElMessage } from "element-plus";
import http from "@/util/http.js";

const loading = ref(false);
const tableData = ref([]);
const pageNum = ref(1);
const pageSize = ref(10);
const total = ref(0);

const searchForm = reactive({ productName: "" });

const stockDialogVisible = ref(false);
const stockDialogTitle = ref("");
const stockFormRef = ref();
const stockForm = reactive({
  productId: null,
  productName: "",
  currentStock: 0,
  quantity: 1,
  operationType: "in",
  remark: ""
});

const stockRules = {
  quantity: [{ required: true, message: "请输入数量", trigger: "blur" }]
};

const logDialogVisible = ref(false);
const stockLogs = ref([]);

const loadData = () => {
  loading.value = true;
  const params = {
    pageNum: pageNum.value,
    pageSize: pageSize.value,
    productName: searchForm.productName || null
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
    console.log("库存数据加载成功，共", tableData.value.length, "条");
  }).catch(() => {
    tableData.value = [];
    loading.value = false;
  });
};

const resetSearch = () => {
  searchForm.productName = "";
  pageNum.value = 1;
  loadData();
};

const handleCurrentChange = (val) => {
  pageNum.value = val;
  loadData();
};

const openStockDialog = (row, type) => {
  stockDialogTitle.value = type === "in" ? "商品入库" : "商品出库";
  stockForm.productId = row.productId;
  stockForm.productName = row.productName;
  stockForm.currentStock = row.stockQuantity;
  stockForm.quantity = 1;
  stockForm.operationType = type;
  stockForm.remark = "";
  stockDialogVisible.value = true;
};

const onStockSubmit = async () => {
  await stockFormRef.value.validate();
  const url = stockForm.operationType === "in" ? "/product/stock/in" : "/product/stock/out";
  http.post(url, {
    productId: stockForm.productId,
    quantity: stockForm.quantity,
    remark: stockForm.remark
  }).then(res => {
    if (res.code === "200") {
      ElMessage.success(stockForm.operationType === "in" ? "入库成功" : "出库成功");
      stockDialogVisible.value = false;
      loadData();
    }
  });
};

const viewLog = (row) => {
  http.get(`/stock-log/product/${row.productId}`).then(res => {
    let logs = res;
    if (logs.data && logs.data.data) {
      logs = logs.data.data;
    } else if (logs.data) {
      logs = logs.data;
    }
    stockLogs.value = logs || [];
    logDialogVisible.value = true;
  });
};

onMounted(() => {
  loadData();
});
</script>

<style scoped>
.search-form {
  margin-bottom: 20px;
}
.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>