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
    <!-- 任务列表 -->
    <div class="task-button-list">
      <!-- 未完成任务按钮 -->
      <button
        v-for="task in incompleteTasks"
        :key="task.id"
        @click="handleTaskClick(task)"
        class="task-button incomplete-task"
      >
        {{ task.taskName }}
        <span class="task-count">总共 {{ task.taskCount }} 车任务</span>
        <span class="task-count">完成 {{ task.completed_count }} 车任务</span>
        <span v-if="zhuangtai">任务处于 未完成 状态</span>
      </button>

      <!-- 已完成任务按钮 -->
      <button
        v-for="task in completedTasks"
        :key="task.id"
        @click="handleTaskClick(task)"
        class="task-button"
      >
        {{ task.taskName }}
        <span class="task-count">总共 {{ task.taskCount }} 车任务</span>
        <span v-if="zhuangtai">当前任务处于 完成 状态</span>
      </button>
    </div>
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
        <el-input-number
          v-model="form.weight"
        />
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
import { computed, inject, onMounted, reactive, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import moment from 'moment';

interface Request {
  get(url: string, params?: any): Promise<any>;
  post(url: string, data?: any): Promise<any>;
}

interface Task {
  id: number;
  taskName: string;
  taskCount: number;
  completed: boolean;
  completed_count: number;
}

const request = inject<Request>('request');
const tableData = ref<Task[]>([]);
const selectedDate = ref(moment().format('YYYY-MM-DD'));
const region_list = ref<string[]>([]);
const dialogFormVisible = ref(false);
const zhuangtai = ref(true);
const labelPosition = 'right';

const manageTable = async () => {
  if (!request) {
    console.error('Request is not defined');
    return;
  }
  dialogFormVisible.value = true;
  try {
    const response = await request.get('/flavourIngredients/distinct-names');
    console.log(response);

    if (response.code === 200) {
      region_list.value = response.data;
    } else {
      ElMessage.error(response.msg);
    }
  } catch (error) {
    console.error('Error fetching distinct names:', error);
  }
};

const regionOptions = computed(() => {
  return region_list.value.map(value => ({
    value: value,
    label: value
  }));
});

const postUserList = async () => {
  if (!request) {
    console.error('Request is not defined');
    return;
  }
  try {
    const response = await request.post('/tableTemplate/listPageC', {
      date: selectedDate.value
    });
    console.log(response);
    if (response.code === 200) {
      tableData.value = response.data;
    } else {
      ElMessage.error(response.msg);
    }
  } catch (err) {
    console.error('Error posting user list:', err);
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
  if (!request) {
    console.error('Request is not defined');
    return;
  }
  try {
    const response = await request.post('/tableManage/manage', {
      "taskName": form.region,
      "taskCount": form.weight,
    });
    console.log(response);
    if (response.code === 200) {
      dialogFormVisible.value = false;
      postUserList();
      ElMessage.success('添加成功');
    } else {
      ElMessage.error(response.msg);
    }
  } catch (err) {
    console.error('Error confirming data:', err);
  }
};

const handleTaskClick = (task: Task) => {
  // 处理任务点击事件
  console.log('Clicked task:', task);
};

// 新增计算属性
const incompleteTasks = computed(() => {
  return tableData.value.filter(task => !task.completed);
});

const completedTasks = computed(() => {
  return tableData.value.filter(task => task.completed);
});
</script>

<style scoped>
.task-button-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 20px;
}

/* 任务按钮样式 */
.task-button {
  padding: 15px; /* 增加内边距，让按钮更饱满 */
  background-color: #80a5ad; /* 蓝色背景，更醒目 */
  color: white; /* 白色文字 */
  border: none;
  border-radius: 5px;
  cursor: pointer;
  text-align: left; /* 文字左对齐 */
  transition: background-color 0.3s ease;
  width: 100%; /* 宽度占据整个板块 */
  font-size: 14px; /* 增大字体 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 添加阴影，增加立体感 */
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* 未完成任务按钮的特殊样式 */
.incomplete-task {
  padding: 30px; /* 增加内边距，让按钮更大 */
  font-size: 16px; /* 增大字体 */
  background-color: #ff6347; /* 更醒目的红色背景 */
}

.task-button:hover {
  background-color: #0056b3; /* 鼠标悬停时颜色变深 */
}

.task-count {
  margin-left: 10px;
  font-weight: bold;
}
</style>


