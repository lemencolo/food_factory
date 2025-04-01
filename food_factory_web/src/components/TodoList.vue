<template>
  
  <div class="todo-list-container" style="display: flex; flex-direction: column; height: 100%; width: 100%;">
    <!-- 修改为 flex 布局，让容器占据整个空间 -->
    <div class="todo-list" style="flex: 1; overflow-y: auto;"> <!-- 添加 overflow-y: auto -->
      <h2 class="todo-list-title">称重列表</h2>
      <transition-group name="fade" tag="ul">
        <li v-for="(todo, index) in visibleTodos" :key="todo.id" :class="{ completed: todo.completed }" class="todo-item">
          <div @click="toggleComplete(todo, index)" id="todo-item">
            <span>{{ todo.ingredientName }}</span>
            <span>{{ todo.weight}} KG</span>
          </div>
        </li>
      </transition-group>
      <!-- 添加显示任务数量的按钮 -->
    </div>
    <div class="task-container">
      <!-- 未完成任务列表 -->
      <div class="sudo-management">
        <div class="task-button-list">
          <!-- 只显示第一个未完成任务 -->
          <button
            v-if="firstIncompleteTask"
            @click="handleTaskClick(firstIncompleteTask)"
            class="task-button incomplete-task"
          >
            {{ firstIncompleteTask.taskName }}
            <span class="task-count">总共 {{ firstIncompleteTask.taskCount }} 车任务</span>
            <span v-if="zhuangtai">当前任务处于 未完成 状态</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, watch, inject } from 'vue';
import { ElMessage } from 'element-plus'

export default {
  props: ['current_seasoning', 'current_weight'],
  data() {
    return {
      newTodo: '',
      todos: [
      ],
      current_index: 1,
      max_index:100,
      pollInterval: null, // 添加此行用于存储定时器ID
      takePhoto_1s:false,
      takePhoto_1s_false:false,
      isTakePhotoDisabled: false, // 标志位
      // isTaskCompleted:false,
    };
  },
  created() {
    this.request = inject('request');
    this.ocrRequest = inject('ocrRequest');
    this.$emit('takePhoto_1s', this.takePhoto_1s);
  },
  computed: {
    visibleTodos() {
      return this.todos.filter(todo => !todo.completed);
    },
    isTaskCompleted() {
      console.log('isTaskCompleted:', this.todos.every(todo => todo.completed));
      return this.todos.every(todo => todo.completed);
    }
  },
  watch: {
    current_seasoning(newVal) {
      console.log('current_seasoning_20:', newVal);
      this.checkAndToggle(newVal, this.current_weight);
    },
    current_weight(newVal) {
      console.log('current_weight_20:', newVal);
      this.checkAndToggle(this.current_seasoning, newVal);
    },
    // 判断 完成单一任务 还是所有任务
    isTaskCompleted(newValue) {
      console.log("isTaskCompleted__?:", newValue);
      if (newValue) {
        this.takePhoto_1s_false = !this.takePhoto_1s_false;
        const currentId = this.current_index;
        console.log("max_index__?:" ,this.max_index);
        console.log('currentId__?:', currentId);    
        this.request.post('/tableManage/updateTaskStatus?task_id=' + currentId).then(res=>{
          console.log(res)
          if(res.code==200){
              // ElMessage.success('更新成功！');
          }else{
              ElMessage.error('更新失败！');
          }
          });
        // 首先显示完成的消息        这里是完成任务的提示 这里应当向后端发送一个请求 得到false 然后就关摄像头
        if (this.current_index < this.max_index) {
          
          const currentId = this.current_index;
          ElMessage({
            message: '完成当前任务.',
            type: 'success',
          });
          // 然后设置一个定时器，在5秒后执行后续操作
          this.isAllCompletedTimer = setTimeout(() => {
            this.current_index += 1;
            this.loadTodos();
          }, 5000); // 5秒延迟

        } else {       
          ElMessage({
            message: '所有任务已完成.',
            type: 'success',
          });
          clearTimeout(this.isAllCompletedTimer);
          // 在10秒后执行的操作
          
          setTimeout(() => {
            // 这里可以添加更多操作，比如重置current_index或todos等
          }, 10000); // 10秒后执行
        }
      }
    },
    takePhoto_1s(newVal){
      if (this.isTakePhotoDisabled) {
        console.log('Request blocked due to higher priority.');
        return;
      }
      this.$emit('takePhoto_1s', true);
      if (newVal) {
        console.log('takePhoto_1s:', newVal);
        // 如果为真的话 就像后端发送请求 
      }
    },
    takePhoto_1s_false(newVal){
      this.$emit('takePhoto_1s', false);
      if (newVal) {
        console.log('takePhoto_1s_false:', newVal);
        this.isTakePhotoDisabled = true; // 设置标志位
        // 可以在这里执行其他操作
        setTimeout(() => {
          this.isTakePhotoDisabled = false; // 重置标志位
        }, 5000); // 假设延迟5秒后重置标志位
      }
    }
  },
  methods: {
    addTodo() {
      if (this.newTodo.trim()) {
        this.todos.push({ text: this.newTodo, counts: '50', completed: false });
        this.newTodo = '';
      }
    },
    removeTodo(index) {
      this.todos.splice(index, 1);  // 从index删除第一个元素
    },
    // Toggle the completed status of a todo 切换待办事项的已完成状态
    toggleComplete(todo, index) {
      console.log("toggleComplete");
      this.takePhoto_1s = !this.takePhoto_1s;
      const updatedTodo = { ...todo, completed: !todo.completed };
      const currentId = todo.id;
      console.log('Current ID:', currentId);
      console.log('Current Index:', index);
      
      this.request.get('/tableTemplate/updateStatus?task_id=' + currentId).then(res=>{
          console.log(res)
          if(res.code==200){
              // ElMessage.success('更新成功！');
          }else{
              ElMessage.error('更新失败！');
          }
      });
      // console.log('updatedTodo:', updatedTodo);
      // console.log('index:', index);
      // Remove the todo from its current position
      this.todos.splice(index, 1);

      if (updatedTodo.completed) {
        // If the todo is marked as completed, push it to the end of the list
        //如果待办事项被标记为已完成，则将其推到列表末尾
        this.todos.push(updatedTodo);
      } else {
        // If the todo is unmarked, reinsert it at its original position
        //如果待办事项没有标记，则重新插入到原始位置
        this.todos.splice(index, 0, updatedTodo);
      }

    },
    checkAndToggle(seasoning, weight) {
      const firstTodo = this.todos[0];
      console.log('firstTodo:', firstTodo.orderIndex);
      // 将 firstTodo 的 counts 转换为两位小数的字符串形式
      const countsFloat = parseFloat(firstTodo.weight).toFixed(2);
      // 将传入的 weight 转换为两位小数的字符串形式
      const weightFloat = parseFloat(weight).toFixed(2);

      console.log('firstTodo.counts (formatted):', countsFloat);
      console.log('weight (formatted):', weightFloat);
      console.log('firstTodo.ingredientName === seasoning:', firstTodo.ingredientName === seasoning);
      console.log('countsFloat === weightFloat:', countsFloat === weightFloat);
      if(firstTodo){
        this.request.post('/compare/compare', {
          "taskName":firstTodo.ingredientName,
          "ocrName":seasoning,
          "taskCount":countsFloat,
          "comCount":weightFloat
        }).then(res=>{
            console.log(res)
            if(res.code==200){
              console.log('Match found:', firstTodo.text);             
              // 这里是匹配成功的提示 应当向后端发送一个请求 并将orderIndex(firstTodo.orderIndex)传入 如果为1的话，后端应当返回true
              // 之后进行每1s拍照一次，直到拍照成功  
              
              this.toggleComplete(firstTodo, 0);
                // ElMessage.success('更新成功！');
            }else{
                ElMessage.error('更新失败！');
            }
        });
      }

      // // 比较 ingredientName 和 weight（两位小数形式）
      // if (
      //   firstTodo &&
      //   firstTodo.ingredientName === seasoning &&
      //   countsFloat === weightFloat
      // ) {
      //   console.log('Match found:', firstTodo.text);
      //   this.toggleComplete(firstTodo, 0);
      // }
    },
    loadTodos() {
      // console.log('current_index:', this.current_index);
      
      // this.request.get('/tableTemplate/listPageC2?task_id=' + this.current_index).then(response => {
      //   this.todos = response;
      //   console.log('todos:', this.todos);
      // });
      console.log('current_index:', this.current_index);
      
      this.request.get('/tableTemplate/listPageC4').then(response => {
        let taskKeys = Object.keys(response);
        if (taskKeys.length === 0) {
          console.warn("No tasks found in the response.");
          this.todos = [];
          return;
        }

        let minTaskKey = Math.min(...taskKeys.map(key => parseInt(key, 10)));
        this.current_index = minTaskKey;

        const todosForMinTaskKey = response[minTaskKey.toString()];
        
        if (todosForMinTaskKey) {
          this.todos = todosForMinTaskKey;
        } else {
          this.todos = [];
        }
        console.log('todos:', this.todos);
      }).catch(error => {
        console.error("Error fetching todos:", error);
        this.todos = [];
      });
    },
    startPolling() {
      this.pollInterval = setInterval(() => {
        this.loadTodos();
      }, 5000); // 每5秒请求一次
    },
    stopPolling() {
      if (this.pollInterval) clearInterval(this.pollInterval);
    }
  },
  mounted() {
    // this.$nextTick(() => {
    //   this.request.get('/ingredients/list1').then(response => {
    //     this.todos = response;
    //     console.log('todos:', this.todos);
    //   });
    // });
    this.loadTodos(); // 初始加载
    this.startPolling(); // 开始轮询
    this.$nextTick(() => {
      // this.request.get('/tableTemplate/listPageC2?task_id='+this.current_index).then(response => {
      //   this.todos = response;
      //   console.log('todos:', this.todos);
      // });
      this.loadTodos();
      this.request.get('/tableTemplate/maxTaskId').then(response => {
        this.max_index = response;
        console.log('max_index:', this.max_index);
      });
    });
  },
  beforeDestroy() {
    this.stopPolling(); // 组件销毁前停止轮询
  }
}
</script>

<style scoped>
.todo-list-container {
  /* 确保容器填满整个父元素 */
  height: 90%;
  width: 90%;
  padding: 0px; /* 添加内边距 */
  background-color:  #e0e0e0;/* 添加背景颜色 */
  display: flex; /* 使用 flex 布局 */
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  border-radius: 10px; /* 添加圆角 */
}

.todo-list {
  /* 使用 flex 布局让列表占据剩余空间 */
  flex: 1;
  display: flex;
  flex-direction: column;
  width: 90%; /* 设置列表宽度 */
  max-width: 800px; /* 设置最大宽度 */
  margin: 0;
  overflow-y: auto; /* 确保内容超出高度时显示滚动条 */
  border-radius: 8px; /* 添加圆角 */
  padding: 20px; /* 添加内边距 */

}

.todo-list-title {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  padding-top: -40px;
  margin-bottom: 30px; /* 添加底部边距 */
  text-align: center; /* 文本居中 */
}

ul {
  list-style-type: none;
  padding: 0;
  /* 让列表可以滚动 */
  overflow-y: auto;
  flex: 1;
}

ul li:first-child {
  background-color:  #f35b5b;  /* 轻蓝色背景 */
  border: 2px solid #add8e6; /* 浅蓝色边框 */
  height: 30%;
  font-size: 100px;
  border-radius: 8px; /* 添加圆角 */
  color: white; /* 新增：设置文字颜色为白色 */
}
.todo-item {
  padding-left: 5px;
  padding: 20px; /* 增加内边距 */
  margin-bottom: 15px; /* 增加底部边距 */
  background-color: #fff;
  border-radius: 8px; /* 增加圆角 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-around;
  flex-direction: column;

}

.todo-item:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 鼠标悬停时增加阴影 */
  transform: translateY(-2px); /* 鼠标悬停时向上移动 */
}

#todo-item {
  display: flex;
  justify-content: space-between;
}

#todo-item span {
  text-align: center;
  align-items: center;
  display: flex;
}

.todo-item.completed {
  text-decoration: line-through;
  color: gray;
}

.all-completed-message {
  font-size: 16px;
  color: green;
  text-align: center;
  margin-top: 10px;
}

.task-container {
  margin-top: 20px; /* 添加顶部边距 */
}

.task-button {
  padding: 12px 20px; /* 增加内边距 */
  border: none;
  border-radius: 8px; /* 增加圆角 */
  background-color: #f35b5b; /* 红色背景 */
  color: #fff; /* 白色文字 */
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s ease; /* 添加过渡效果 */
}

.task-button:hover {
  background-color: #e24a4a; /* 鼠标悬停时颜色变深 */
  transform: translateY(-2px); /* 鼠标悬停时向上移动 */
}

.task-count {
  margin-left: 10px;
  font-weight: bold;
}
</style>