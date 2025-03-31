<template>
  <div class="sudo-setting">
      <el-table :data="tableData" :header-cell-style="{background: '#f2f2f2'}" border>
          <el-table-column prop="id" label="ID" width="140" />
          <el-table-column prop="username" label="用户名" width="150" />
          <el-table-column prop="roleID" label="权限管理" width="150">
              <template #default="scope">
                  <el-tag
                      :type="getTagType(scope.row.roleID)"
                      disable-transitions
                  >
                      {{ getTagText(scope.row.roleID) }}
                  </el-tag>
              </template>
          </el-table-column>
          <el-table-column prop="isActive" label="是否可用" width="150">
              <template #default="scope">
                  <el-tag
                      :type="scope.row.isActive === true ? 'primary' : 'success'"
                      disable-transitions
                  >
                      {{ scope.row.isActive === true ? '可用' : '不可用' }}
                  </el-tag>
              </template>
          </el-table-column>
          <el-table-column prop="created_at" label="创建时间">
              <template #default="scope">
                  {{ formatDate(scope.row.created_at) }}
              </template>
          </el-table-column>
          <el-table-column prop="updated_at" label="更新时间">
              <template #default="scope">
                  {{ formatDate(scope.row.updated_at) }}
              </template>
          </el-table-column>
          <el-table-column prop="operate" label="Operate">
              <template #default="scope">
                  <el-popconfirm
                      confirm-button-text="Yes"
                      cancel-button-text="No"
                      :icon="InfoFilled"
                      icon-color="#626AEF"
                      title="Are you sure to toggle this?"
                      @confirm="toggleUserStatus(scope.row)"
                  >
                      <template #reference>
                          <el-button link type="primary" size="small">
                              {{ scope.row.isActive ? '禁用' : '启用' }}
                          </el-button>
                      </template>
                  </el-popconfirm>
              </template>
          </el-table-column>
      </el-table>
  </div>
</template>

<script lang="ts" setup>
import { computed, inject, onMounted, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { InfoFilled } from '@element-plus/icons-vue';
import moment from 'moment';

const tableData = ref([]);
const request = inject('request') as any;
const currentPage = ref(1);
const pageSize = ref(5);
const total = ref(100);

const getTagType = (roleID: number): string => {
  switch (roleID) {
      case 0:
          return 'danger'; // 超级管理员
      case 1:
          return 'warning'; // 管理员
      case 2:
          return 'info'; // 用户
      default:
          return 'info';
  }
};

const getTagText = (roleID: number): string => {
  switch (roleID) {
      case 0:
          return '超级管理员'; // 超级管理员
      case 1:
          return '管理员'; // 管理员
      case 2:
          return '用户'; // 用户
      default:
          return '未知角色';
  }
};

const formatDate = (dateString: string | undefined): string => {
  if (!dateString) return ''; // 处理空值
  return moment(dateString).format('YYYY-MM-DD HH:mm');
};

const postUserList = async () => {
  try {
      const response = await request.post('/users/listPage', {
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

const toggleUserStatus = async (row: any) => {
  try {
      const newStatus = !row.isActive;
      console.log(row.id, newStatus);

      await request.post(`/users/toggleStatus?userId=${row.id}`);

      row.isActive = newStatus;  // 更新本地状态
      ElMessage.success(`用户状态已更新为 ${newStatus ? '可用' : '不可用'}`);
  } catch (err) {
      console.error(err);
      ElMessage.error('更新用户状态失败');
  }
};

onMounted(async () => {
  await postUserList();
});
</script>

<style scoped>
/* 添加一些样式 */
.sudo-setting {
  padding: 20px;
}
</style>


