<template>
    <div class="container">
        <el-container id="container_chi1">
        <TodoList :current_seasoning="current_seasoning" :current_weight="current_weight"/>
        </el-container>
        <el-container id="container_chi2">
        <!-- <h1>To-Do List</h1> -->
        <Counts_View 
        @photo-taken="updatePhotoSrc"           
        @current_seasoning="updateCurrentSeasoning" 
        @current_weight="updateCurrentWeight"></Counts_View>
        <!-- <TodoList /> -->
        </el-container>
        <el-container id="container_chi3">
        <h1>今日任务列表</h1>
        <Task />
        <!-- <TodoList /> -->
        </el-container>
        <el-container id="container_chi4">
        <Video_View :photoSrc="photoSrc" />
        <!-- <TodoList /> -->
        </el-container>
    </div>
</template>

<script>
import Counts_View from './Counts_View.Vue';
import Task from './task.vue';
import TodoList from './TodoList.Vue';
import Video_View from './Video_View.Vue';

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
            photoSrc: '',
            current_seasoning: '',
            current_weight: '',
        };
    },
  methods: {
      updatePhotoSrc(src) {
          this.photoSrc = src;
          // console.log('photoSrc:', this.photoSrc);
      },
      updateCurrentSeasoning(seasoning) {
          this.current_seasoning = seasoning;
          console.log('onCurrent_seasoning_father:', this.current_seasoning);
      },
      updateCurrentWeight(weight) {
          this.current_weight = weight;
          console.log('current_weight_father :', this.current_weight);
      }
  }
    
};
</script>

<style>
.container {
    /* display: inline-block; */
    /* margin: 20px; */
    /* padding: 20px; */
    height: 100%;
    width: 100%;
    background-color: #fff;
    border-radius: 5px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    box-shadow: 0 5px 10px rgba(0, 0, 0, 0.08);
    justify-content: space-around;
  }
  #container_chi1 {
    width: calc(50% - 40px); /* 适当增大宽度 */
    height: calc(50% - 40px); /* 适当增大高度 */
    background-color: #e0e0e0;
    border: 1px solid #bdbdbd;
    border-radius: 18px;
    box-shadow: 0 5px 10px rgba(0, 0, 0, 0.08);
    margin: 20px; /* 减小外边距 */
}

#container_chi2 {
    width: calc(50% - 40px);
    height: calc(50% - 40px);
    background-color: #e0e0e0;
    border: 1px solid #bdbdbd;
    border-radius: 18px;
    box-shadow: 0 5px 10px rgba(0, 0, 0, 0.08);
    margin: 20px;
}

#container_chi3 {
    width: calc(50% - 40px);
    height: calc(50% - 40px);
    background-color: #e0e0e0;
    border: 1px solid #bdbdbd;
    border-radius: 18px;
    box-shadow: 0 5px 10px rgba(0, 0, 0, 0.08);
    flex-direction: column;
    align-items: center;
    justify-content: center;
    margin: 20px;
}

#container_chi4 {
    width: calc(50% - 40px);
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