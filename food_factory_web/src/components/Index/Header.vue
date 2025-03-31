<template>
  <el-header style="text-align: right; font-size: 12px">
      <div class="toolbar">
        <el-dropdown>
          <el-icon style="margin-right: 8px; margin-top: 1px">
            <setting />
          </el-icon>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="handleLogout">Log out</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        <span>{{ userName }}</span>
      </div>
  </el-header>
  </template>
  
  <script lang="ts" setup>
  import { ref, onMounted } from 'vue'
  import { useRouter } from 'vue-router'
  import { Menu as IconMenu, Message, Setting, DataBoard } from '@element-plus/icons-vue'
  import { ElMessage, ElMessageBox } from 'element-plus'
  const router = useRouter()
  const userName = ref('')
  
  // 从sessionStorage获取用户名
  onMounted(() => {
    const userData = sessionStorage.getItem("User")
    if (userData) {
      try {
        const user = JSON.parse(userData)
        userName.value = user.username || ''
      } catch (error) {
        console.error('Error parsing user data:', error)
      }
    }
  })
  
  // 处理注销
  const handleLogout = () => {
    ElMessageBox.confirm('确认退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
    }).then(() => {
      // localStorage.removeItem('isAuthenticated')
      // router.push('/login')
          // 清除用户会话
    sessionStorage.removeItem("User")
    // 跳转到登录页面
    router.push('/login')
    // 正确的注销操作（推荐）
    localStorage.removeItem('isAuthenticated');
    })


  }
  </script>
  
  <style>
  .el-header {
    position: relative;
    background-color: rgb(245, 242, 242);
    color: var(--el-text-color-primary);
  }
  .toolbar {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    height: 100%;
    right: 20px;
  }
  .el-menu {
    border-right: none;
  }
  </style>