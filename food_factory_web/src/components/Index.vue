<template>
    <el-container class="layout-container-demo">
        <Aside/>
      <el-container class="layout-container-demo2">
        <Header/>
        <router-view>
          
        </router-view>
        <!-- <Main/> -->
         
      </el-container>
    </el-container>
</template>
  
<script lang="ts" setup>
// import { ref } from 'vue'
import Aside from './Index/Aside.vue'
import Header from './Index/Header.vue'
// import Main from './Index/Main.vue';
// import Login from './Login.vue';

</script>

<style scoped>

.layout-container-demo{
    height: 100%;
    width: 100%;
}
.layout-container-demo2{
    height: 100%;
    width: 100%;
    display: flex;
    flex-direction: column;
}

</style>