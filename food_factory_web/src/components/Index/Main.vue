<template>
  <el-main>
    <el-scrollbar>
      <el-table :data="tableData" :header-cell-style="{background: '#f2f2f2'}" border>
        <el-table-column prop="id" label="ID" width="140" />
        <el-table-column prop="flavourName" label="种类名" width="150" />
        <el-table-column prop="ingredientName" label="原料名" width="150" />
        <el-table-column prop="weight" label="数量" width="150"/>
        <el-table-column prop="completed" label="是否完成">
          <template #default="scope">
            <el-tag
                :type="scope.row.completed === true ? 'primary' : 'success'"
                disable-transitions
            >{{ scope.row.completed === true ? '完成' : '未完成' }}</el-tag>
          </template>
        </el-table-column>
        <!-- 
        <el-table-column prop="createTime" label="创建时间">
          <template #default="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="completedTime" label="完成时间">
          <template #default="scope">
            {{ formatDate(scope.row.completedTime) }}
          </template>
        </el-table-column> 
        -->
      </el-table>
    </el-scrollbar>
  </el-main>
</template>

<script lang="ts" setup>
import { computed, inject, onMounted, ref } from 'vue';
import moment from 'moment';
import { ElMessage } from 'element-plus';

interface Request {
  post(url: string, data: any): Promise<any>;
}

const tableData = ref([]);
const request = inject<Request>('request')!;
const currentPage = ref(1);
const pageSize = ref(100);
const total = ref(100);

const formatDate = (dateString: string | undefined): string => {
  if (!dateString) return ''; // 处理空值
  return moment(dateString).format('YYYY-MM-DD HH:mm');
};

const postUserList = async () => {
  try {
    const response = await request.post('/tableTemplate/listPage', {
      "pageSize": pageSize.value,
      "pageNum": currentPage.value,
    });
    console.log(response);
    if (response.code === 200) {
      tableData.value = response.data;
      total.value = response.total;
    } else {
      ElMessage.error(response.msg);
    }
  } catch (err) {
    console.error(err);
    ElMessage.error('An unexpected error occurred.');
  }
};

onMounted(async () => {
  await postUserList();
});
</script>

<style>
.el-main {
  padding: 0;
}
</style>


