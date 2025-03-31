<!-- Task.vue -->
<template>
  <div class="task-container">
    <!-- 标题 -->
    <h1 class="task-title">任务列表</h1>

    <!-- 未完成任务列表 -->
    <div class="sudo-management">
      <div class="task-button-list">
        <!-- 未完成任务按钮 -->
        <button
          v-for="task in incompleteTasks"
          :key="task.id"
          @click="handleTaskClick(task)"
          class="task-button incomplete-task"
        >
          {{ task.taskName }}
          <span class="task-count">完成 {{ task.completed_count }} 车任务</span>
          <span class="task-count">共 {{ task.taskCount }} 车任务</span>
          <span v-if="zhuangtai"> 未完成 </span>
        </button>

        <!-- 已完成任务按钮 -->
        <button
          v-for="task in completedTasks"
          :key="task.id"
          @click="handleTaskClick(task)"
          class="task-button"
        >
          {{ task.taskName }}
          <span class="task-count">完成 {{ task.completed_count }} 车任务</span>
          <span class="task-count">共 {{ task.taskCount }} 车任务</span>
          <span v-if="zhuangtai"> 完成 </span>
        </button>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { computed, inject, onMounted, ref } from 'vue';
import { ElMessage } from 'element-plus';

interface Request {
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
const zhuangtai = ref(true);
const dialogFormVisible = ref(false);
const dialogTitle = ref(''); // 定义 dialogTitle

// 获取任务列表的函数
const fetchTasks = async () => {
  try {
    if (!request) {
      console.error('Request is not defined');
      return;
    }
    // 实现获取任务列表的逻辑
  } catch (error) {
    console.error('获取任务列表失败:', error);
  }
};

const handleTaskClick = (task: Task) => {
  // 处理任务点击事件
  console.log('Clicked task:', task);
  dialogTitle.value = task.taskName; // 设置 dialogTitle
  dialogFormVisible.value = true; // 显示对话框
};

let intervalId;

onMounted(async () => {
  await fetchTasks();
  await postUserList();

  // 每 1 秒更新一次任务状态
  intervalId = setInterval(async () => {
    await fetchTasks();
    await postUserList();
  }, 1000);
});

// 新增计算属性
const incompleteTasks = computed(() => {
  return tableData.value.filter(task => !task.completed);
});

const completedTasks = computed(() => {
  return tableData.value.filter(task => task.completed);
});

const postUserList = async () => {
  try {
    if (!request) {
      console.error('Request is not defined');
      return;
    }
    const response = await request.post('/tableTemplate/listPageC');
    console.log('接口返回的完整数据:', response.data); // 打印接口返回的完整数据
    if (response.code === 200) {
      tableData.value = response.data;
    } else {
      ElMessage.error(response.msg);
    }
  } catch (err) {
    // 处理错误
  }
};
</script>

<style scoped>
/* 标题样式 */
.task-title {
  text-align: center;
  color: #333;
  margin-bottom: 20px;
}

/* 按钮组样式 */
.button-group {
  display: flex;
  flex-direction: column;
  gap: 10px; /* 按钮之间的间距 */
  margin-bottom: 20px;
}

/* 获取任务列表按钮样式 */
.fetch-button {
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

/* 任务项样式 */
.task-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px;
  border-bottom: 1px solid #eee;
}

.task-button-list {
  margin-bottom: 20px;
  display: flex;
  gap: 5px;
  flex-wrap: wrap;
  height: 80%;
  width: 100%;
  max-width: 1000px;
  max-height: 280px;
  margin: 4% auto;
  padding: 20px;
  padding-top: 20px;
  border-radius: 8px;

  box-sizing: border-box;
  overflow: auto;
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
  font-size: 20px; /* 增大字体 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 添加阴影，增加立体感 */
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* 未完成任务按钮的特殊样式 */
.incomplete-task {
  padding: 30px; /* 增加内边距，让按钮更大 */
  font-size: 22px; /* 增大字体 */
  background-color: #f35b5b; /* 更醒目的红色背景 */
}

.task-button:hover {
  background-color: #b4c0e4; /* 鼠标悬停时颜色变深 */
}

.task-count {
  margin-left: 10px;
  font-weight: bold;
}

/* 新增样式，用于让按钮内文字对齐 */
.task-button span {
  display: flex; 
  align-items: center; 
}
</style>