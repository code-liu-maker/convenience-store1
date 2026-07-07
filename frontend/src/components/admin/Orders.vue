<template>
  <el-card>
    <template #header>
      <span>订单管理</span>
    </template>

    <!-- 搜索栏 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="订单状态">
        <el-select v-model="searchForm.orderStatus" placeholder="全部" clearable>
          <el-option label="待接单" :value="0"></el-option>
          <el-option label="已完成" :value="1"></el-option>
          <el-option label="已取消" :value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="下单时间">
        <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadData">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格 -->
    <el-table :data="tableData" stripe v-loading="loading" border style="width: 100%">
      <el-table-column prop="orderNo" label="订单编号" width="200" align="center"></el-table-column>
      <el-table-column prop="userName" label="下单用户" width="120" align="center"></el-table-column>
      <el-table-column prop="totalAmount" label="订单金额" width="120" align="center">
        <template #default="scope">￥{{ scope.row.totalAmount }}</template>
      </el-table-column>
      <el-table-column prop="orderStatus" label="订单状态" width="120" align="center">
        <template #default="scope">
          <el-tag v-if="scope.row.orderStatus === 0" type="warning">待接单</el-tag>
          <el-tag v-else-if="scope.row.orderStatus === 1" type="success">已完成</el-tag>
          <el-tag v-else type="danger">已取消</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="orderTime" label="下单时间" width="180" align="center"></el-table-column>
      <el-table-column label="操作" width="200" align="center" fixed="right">
        <template #default="scope">
          <el-button size="small" type="primary" plain @click="viewDetail(scope.row)">详情</el-button>
          <el-button
              v-if="scope.row.orderStatus === 0"
              size="small"
              type="success"
              plain
              @click="updateStatus(scope.row, 1)"
          >接单</el-button>
          <el-button
              v-if="scope.row.orderStatus === 0"
              size="small"
              type="danger"
              plain
              @click="updateStatus(scope.row, 2)"
          >取消</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-empty v-if="tableData.length === 0 && !loading" description="暂无订单数据" />

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

    <!-- 订单详情对话框 -->
    <el-dialog v-model="detailVisible" title="订单详情" width="600px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="订单编号">{{ currentOrder.orderNo || "-" }}</el-descriptions-item>
        <el-descriptions-item label="下单用户">{{ currentOrder.userName || "-" }}</el-descriptions-item>
        <el-descriptions-item label="订单金额">￥{{ currentOrder.totalAmount || 0 }}</el-descriptions-item>
        <el-descriptions-item label="订单状态">
          <el-tag v-if="currentOrder.orderStatus === 0" type="warning">待接单</el-tag>
          <el-tag v-else-if="currentOrder.orderStatus === 1" type="success">已完成</el-tag>
          <el-tag v-else-if="currentOrder.orderStatus === 2" type="danger">已取消</el-tag>
          <el-tag v-else type="info">未知</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="下单时间" :span="2">{{ currentOrder.orderTime || "-" }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ currentOrder.remark || "无" }}</el-descriptions-item>
      </el-descriptions>
      <el-divider>商品明细</el-divider>
      <el-table :data="currentOrder.items || []" stripe border>
        <el-table-column prop="productName" label="商品名称"></el-table-column>
        <el-table-column prop="price" label="单价" width="100" align="center">
          <template #default="scope">￥{{ scope.row.price }}</template>
        </el-table-column>
        <el-table-column prop="quantity" label="数量" width="80" align="center"></el-table-column>
        <el-table-column prop="subtotal" label="小计" width="100" align="center">
          <template #default="scope">￥{{ scope.row.subtotal }}</template>
        </el-table-column>
      </el-table>
      <el-empty v-if="!currentOrder.items || currentOrder.items.length === 0" description="暂无商品明细" />
    </el-dialog>
  </el-card>
</template>

<script setup>
import { reactive, ref, onMounted } from "vue";
import { ElMessage } from "element-plus";
import http from "@/util/http.js";

// ========== 数据定义 ==========
const loading = ref(false);
const tableData = ref([]);
const pageNum = ref(1);
const pageSize = ref(10);
const total = ref(0);
const detailVisible = ref(false);
const currentOrder = ref({});

const searchForm = reactive({
  orderStatus: null
});
const dateRange = ref([]);

// ========== 加载订单列表 ==========
const loadData = () => {
  loading.value = true;
  const params = {
    pageNum: pageNum.value,
    pageSize: pageSize.value,
    orderStatus: searchForm.orderStatus,
    startTime: dateRange.value?.[0] || null,
    endTime: dateRange.value?.[1] || null
  };

  http.get("/order/admin/list", { params })
      .then(res => {
        let result = res;
        if (result.data && result.data.data) {
          result = result.data.data;
        } else if (result.data) {
          result = result.data;
        }
        tableData.value = result?.list || [];
        total.value = result?.totalSize || 0;
        loading.value = false;
        console.log("订单数据加载成功，共", tableData.value.length, "条");
      })
      .catch(() => {
        tableData.value = [];
        total.value = 0;
        loading.value = false;
      });
};

// ========== 重置搜索 ==========
const resetSearch = () => {
  searchForm.orderStatus = null;
  dateRange.value = [];
  pageNum.value = 1;
  loadData();
};

// ========== 分页切换 ==========
const handleCurrentChange = (val) => {
  pageNum.value = val;
  loadData();
};

// ========== 查看订单详情 ==========
const viewDetail = async (row) => {
  try {
    const res = await http.get(`/order/${row.orderId}`);
    let order = res;
    if (order.data && order.data.data) {
      order = order.data.data;
    } else if (order.data) {
      order = order.data;
    }
    currentOrder.value = order || {};
    detailVisible.value = true;
  } catch (error) {
    ElMessage.error("获取订单详情失败");
  }
};

// ========== 更新订单状态 ==========
const updateStatus = (row, status) => {
  const statusMap = { 1: "接单", 2: "取消" };
  http
      .put(`/order/status?orderId=${row.orderId}&orderStatus=${status}`)
      .then(res => {
        if (res.code === "200") {
          ElMessage.success(`订单${statusMap[status] || "更新"}成功`);
          loadData();
        } else {
          ElMessage.error(res.message || "状态更新失败");
        }
      })
      .catch(() => {
        ElMessage.error("状态更新失败");
      });
};

// ========== 页面加载 ==========
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