<template>
  <el-card>
    <template #header>
      <span>库存日志</span>
    </template>

    <el-table :data="tableData" stripe v-loading="loading">
      <el-table-column prop="productName" label="商品名称" width="180"></el-table-column>
      <el-table-column prop="operationType" label="操作类型" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.operationType === 0 ? 'success' : 'warning'">
            {{ scope.row.operationType === 0 ? "入库" : "出库" }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="quantity" label="数量" width="100"></el-table-column>
      <el-table-column prop="operator" label="操作人" width="120"></el-table-column>
      <el-table-column prop="remark" label="备注" width="200"></el-table-column>
      <el-table-column prop="operationTime" label="操作时间" width="180"></el-table-column>
    </el-table>

    <el-pagination
        @current-change="handleCurrentChange"
        v-model:current-page="pageNum"
        :page-size="pageSize"
        :total="total"
        layout="total, prev, pager, next"
        class="pagination">
    </el-pagination>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from "vue";
import http from "@/util/http.js";

const loading = ref(false);
const tableData = ref([]);
const pageNum = ref(1);
const pageSize = ref(10);
const total = ref(0);

const loadData = () => {
  loading.value = true;
  http.get(`/stock-log/list?pageNum=${pageNum.value}&pageSize=${pageSize.value}`).then(res => {
    let result = res;
    if (result.data && result.data.data) {
      result = result.data.data;
    } else if (result.data) {
      result = result.data;
    }
    tableData.value = result?.list || [];
    total.value = result?.totalSize || 0;
    loading.value = false;
  }).catch(() => {
    tableData.value = [];
    loading.value = false;
  });
};

const handleCurrentChange = (val) => {
  pageNum.value = val;
  loadData();
};

onMounted(() => {
  loadData();
});
</script>

<style scoped>
.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>