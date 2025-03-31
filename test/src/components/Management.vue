<template>
  <div class="container">
    <div class="left-container">
      <el-container id="container_chi1">
        <TodoList :current_seasoning="current_seasoning" :current_weight="current_weight"/>
      </el-container>å
    </div>
    <div class="right-container">
      <el-container id="container_chi2">
        <Counts_View 
          @photo-taken="updatePhotoSrc"
          @current_seasoning="updateCurrentSeasoning" 
          @current_weight="updateCurrentWeight"
        ></Counts_View>
      </el-container>
      <!-- 根据 showTask 状态决定显示 Task 还是 Video_View -->
      <el-container  v-if="showTask" id="container_chi4">
        <Video_View :photoSrcs="photoSrcs" />
      </el-container>
      <el-container v-else id="container_chi3">
        <Task />
      </el-container>
      
      <el-container id="button-container">
        <el-button @click="toggleSection">切换板块</el-button>
      </el-container>
    </div>
  </div>
</template>

<script>
import Counts_View from './Counts_View.Vue';
import TodoList from './TodoList.Vue';
import Video_View from './Video_View.Vue';
import Task from './Task.Vue';

export default {
  name: 'Management',
  components: {
    TodoList,
    Counts_View,
    Video_View,
    Task,
  },
  data() {
    return {
      photoSrcs: [], // 存储两张照片的 base64 数据
      current_seasoning: '',
      current_weight: '',
      takePhoto_1s: false,
      // 新增状态变量，用于控制显示 Task 还是 Video_View
      showTask: false 
    };
  },
  methods: {
    updatePhotoSrc(src) {
      if (!this.photoSrcs) {
        this.photoSrcs = []; // 如果 photoSrcs 未定义，则初始化为空数组
      }
      if (this.photoSrcs.length < 2) {
        this.photoSrcs.push(src);
      } else {
        this.photoSrcs = [src]; // 如果超过两张，替换第一张
      }
      console.log(this.photoSrcs);
    },
    updateCurrentSeasoning(seasoning) {
      this.current_seasoning = seasoning;
      console.log('onCurrent_seasoning_father:', this.current_seasoning);
    },
    updateCurrentWeight(weight) {
      this.current_weight = weight;
      console.log('current_weight_father :', this.current_weight);
    },
    updateTakePhoto_1s(boolean) {
      this.takePhoto_1s = boolean;
      console.log('takePhoto_1s :', this.takePhoto_1s);
    },
    ChangetakePhoto(boolean) {
      this.takePhoto_1s = boolean;
      console.log('takePhoto_1s :', this.takePhoto_1s);
    },
    // 切换显示状态的方法
    toggleSection() {
      this.showTask = !this.showTask;
    }
  }
};
</script>

<style>
.container {
  height: 90%;
  width: 100%;
  background-color: #fff;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: row;
  box-shadow: 0 5px 10px rgba(0, 0, 0, 0.08);
}

.left-container {
  width: 50%; /* 占满左侧，宽度不变 */
  display: flex;
  flex-direction: column;
}

.right-container {
  width: 50%;
  display: flex;
  flex-direction: column;
}

#container_chi1 {
  height: 100%; /* 占满左侧高度 */
  background-color: #e0e0e0;
  border: 1px solid #bdbdbd;
  border-radius: 18px;
  box-shadow: 0 5px 10px rgba(0, 0, 0, 0.08);
  margin: 20px;
}

#container_chi2 {
  height: calc(50% - 40px);
  background-color: #e0e0e0;
  border: 1px solid #bdbdbd;
  border-radius: 18px;
  box-shadow: 0 5px 10px rgba(0, 0, 0, 0.08);
  margin: 20px;
}

#button-container {
  position: fixed; /* 固定定位 */
  top: 50%; /* 垂直居中 */
  right: 20px; /* 距离右侧 20px */
  transform: translateY(-50%); /* 向上偏移自身高度的一半以实现精确居中 */
  color: white; /* 按钮文字颜色 */
  border: none; /* 去除边框 */
  border-radius: 5px; /* 圆角 */

  font-size: 16px; /* 文字大小 */
  cursor: pointer; /* 鼠标指针样式 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2); /* 添加阴影 */
  transition: background-color 0.3s ease; /* 背景颜色过渡效果 */
}

#container_chi3,
#container_chi4 {
  height: calc(50% - 40px);
  background-color: #e0e0e0;
  border: 1px solid #bdbdbd;
  border-radius: 18px;
  box-shadow: 0 5px 10px rgba(0, 0, 0, 0.08);
  margin: 20px;
}

h1 {
  color: #35495e;
}
</style>