<template>
  <el-aside width="200px">
    <el-scrollbar>
      <el-menu router default-active="1" :collapse="isCollapse" class="el-menu-vertical-demo" :collapse-transition="false">
        <el-menu-item index="/Index">
          <el-icon><icon-menu /></el-icon>
          <template #title>首页</template>
        </el-menu-item>
        <el-menu-item :index="'/' + item.menuClick" v-for="(item, i) in menu" :key="i">
          <el-icon><component :is="getIconComponent(i)" /></el-icon>
          <template #title>{{ item.menuName }}</template>
        </el-menu-item>
      </el-menu>
    </el-scrollbar>
  </el-aside>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue';
import { useStore } from 'vuex';
import { Menu as IconMenu, Setting, Message, Edit, DataBoard } from '@element-plus/icons-vue';

const isCollapse = ref(false); // 初始化为 false 或者根据您的需要设置初始值
const iconMap = [
  Setting,
  Message,
  Edit,
  DataBoard
];

const getIconComponent = (i: number): any => {
  return iconMap[i % iconMap.length]; // 确保索引在数组范围内
};

const store = useStore(); // 使用 useStore 获取 Vuex store
const menu = computed(() => store.state.menu);
</script>

<style scoped>
.layout-container-demo .el-aside {
  color: var(--el-text-color-primary);
  background-color: rgb(245, 242, 242);
}
</style>


