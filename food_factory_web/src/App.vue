
<template>
    <div id="app">
      <!-- <Management/> -->
      <!-- <Index/> -->
      <router-view></router-view>
      <!-- <Login/> -->
  </div>
</template>

<script>
import Counts_View from './components/Counts_View.Vue';
import Management from './components/Management.vue';
import TodoList from './components/TodoList.Vue';
import Video_View from './components/Video_View.Vue';
import Index from './components/Index.vue';
import Login from './components/Login.vue';
export default {
  name: 'App',
  components: {
    Management,
    Index,
    Login,
  },
    
};
</script>


<style>
/* Add some basic styles */
body {
  font-family: Arial, sans-serif;
  background-color: #f4f4f4;
  margin: 0;
  padding: 0;
}

#app {
  text-align: center;
  height: 100vh;
  width: 100vw;
  /* margin-top: 50px; */
}



</style>
