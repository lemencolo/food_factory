<template>
    <div class="login-container">
      <div class="login-box">
        <h1 class="login-title">登录</h1>
        <el-form :model="form" status-icon :rules="rules" ref="formRef" label-width="80px">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="form.username" placeholder="请输入用户名"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input v-model="form.password" type="password" placeholder="请输入密码" :show-password="true" @keyup.enter="handleLogin"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleLogin" >登录</el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import {computed, inject, onBeforeMount, onBeforeUnmount, onMounted,watch} from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const request = inject('request');
// 表单数据
const form = reactive({
  username: '',
  password: ''
});

// 表单验证规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于 6 位', trigger: 'blur' },
  ]
};

// 表单引用
const formRef = ref(null);
const router = inject('router');
const store = inject('store');
// 登录方法
const handleLogin = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      // console.log(form.username, form.password);
      
      // 这里可以添加实际的登录逻辑，例如调用 API
      request.post('/users/login',{
          "username":form.username,
          "password":form.password
      }).then(res=>{
          console.log(res)
          if(res.code==200){
              //存储
              sessionStorage.setItem("User",JSON.stringify(res.data.user))
              store.commit('setMenu',res.data.menu)
              //跳转到主页
              // this.$router.replace('/Index')
              ElMessage.success('登录成功！');
              // 存储用户信息
              localStorage.setItem('isAuthenticated', 'true'); // 设置登录状态
              router.replace('/Index');

          }else{
              // this.confirm_disabled=false;
              // ElMessage('校验失败，用户名或密码错误！');
              ElMessage.error('校验失败，用户名或密码错误！');
              // return false;
          }
      });
      // console.log('登录成功，用户名：', form.username);
    } else {
      // console.log('验证失败');
      ElMessage.error('验证失败')
      return false;
    }
  });
};

// 重置表单方法
const resetForm = () => {
  formRef.value.resetFields();
};
</script>

<style scoped>
.login-container {
display: flex;
justify-content: center;
align-items: center;
height: 100vh;
background-color: #f0f2f5;
}

.login-box {
width: 360px;
padding: 40px;
margin-bottom: 100px;
background-color: #fff;
border-radius: 8px;
box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.login-title {
margin-bottom: 30px;
text-align: center;
color: #333;
}
</style>