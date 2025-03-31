<template>
  <div class="sudo-management">
    <div class="table-header">
      <el-date-picker
        v-model="selectedDate"
        type="date"
        placeholder="选择日期"
        format="YYYY-MM-DD"
        value-format="YYYY-MM-DD"
        @change="handleDateChange"
      />
    </div>
    <el-table :data="tableData" :header-cell-style="{ background: '#f2f2f2' }" border>
      <el-table-column prop="id" label="ID" width="140" />
      <el-table-column prop="taskName" label="任务名称" />
      <el-table-column prop="taskCount" label="任务数量" />
      <el-table-column prop="completedCount" label="完成数量" />
      <el-table-column prop="taskIndex" label="任务次序">
        <template #default="scope">
          <el-tag
            :type="scope.row.isActive === true ? 'primary' : 'success'"
            disable-transitions
          >
            {{ scope.row.isActive === true ? '可用' : '不可用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="completed" label="是否完成">
        <template #default="scope">
          <el-tag
            :type="scope.row.completed === true ? 'primary' : 'success'"
            disable-transitions
          >
            {{ scope.row.completed === true ? '已完成' : '未完成' }}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>
    <el-button type="primary" @click="manageTable">Add New Task</el-button>
  </div>
  <el-dialog v-model="dialogFormVisible" :title="dialogTitle" width="50vw">
    <el-form :model="form" label-width="auto" style="max-width: 600px" :label-position="labelPosition">
      <el-form-item label="Task name" required>
        <el-select v-model="form.region" placeholder="please select your zone">
          <el-option
            v-for="item in regionOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="Task number" required style="width: 250px;">
        <el-input-number v-model="form.weight" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="confirmData">Confirm</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted, computed, inject } from 'vue';
import { ElMessage } from 'element-plus';
import moment from 'moment';

interface Request {
  get(url: string): Promise<any>;
  post(url: string, data: any): Promise<any>;
}

const request = inject('request') as Request;
const tableData = ref([] as any[]);
const selectedDate = ref(moment().format('YYYY-MM-DD'));
const regionList = ref([] as string[]);
const dialogFormVisible = ref(false);
const dialogTitle = 'Add New Task';
const labelPosition = 'right';

const manageTable = async () => {
  dialogFormVisible.value = true;
  try {
    const response = await request.get('/flavourIngredients/distinct-names');
    if (response.code === 200) {
      regionList.value = response.data;
    } else {
      ElMessage.error(response.msg);
    }
  } catch (error) {
    ElMessage.error('Failed to fetch regions');
  }
};

const regionOptions = computed(() => {
  return regionList.value.map(value => ({
    value,
    label: value
  }));
});

const postUserList = async () => {
  try {
    const response = await request.post('/tableTemplate/listPageC', {
      date: selectedDate.value
    });
    if (response.code === 200) {
      tableData.value = response.data;
    } else {
      ElMessage.error(response.msg);
    }
  } catch (error) {
    ElMessage.error('Failed to fetch user list');
  }
};

const form = reactive({
  region: '',
  weight: 1,
});

const handleDateChange = () => {
  postUserList();
};

onMounted(async () => {
  await postUserList();
});

const confirmData = async () => {
  try {
    const response = await request.post('/tableManage/manage', {
      taskName: form.region,
      taskCount: form.weight,
    });
    if (response.code === 200) {
      dialogFormVisible.value = false;
      postUserList();
      ElMessage.success('添加成功');
    } else {
      ElMessage.error(response.msg);
    }
  } catch (error) {
    ElMessage.error('Failed to add task');
  }
};
</script>

<style scoped>
.el-form-con {
  display: flex;
  justify-content: space-between;
  flex-direction: row;
}
.pagination-container {
  display: flex;
  justify-content: flex-end; /* 让分页栏靠右对齐 */
  margin-top: 12px;
}
</style>


