import { createApp } from 'vue';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import App from './App.vue';
import request from './js/request';
import ocr_request from './js/ocrRequest';
import router from './router/index';
import store from './store/index';

// 使用 Vite 的 glob 导入所有组件
const modules = import.meta.glob('../components/*.vue')

const app = createApp(App);
// 在应用启动时检查 localStorage 并重新添加路由
const savedMenu = JSON.parse(localStorage.getItem('menu') || '[]');

if (Array.isArray(savedMenu)) {
  savedMenu.forEach((menu: any) => {
    const componentPath = `../components/${menu.menuComponent}.vue`;
    const newRoute = {
      path: '/' + menu.menuClick,
      name: menu.menuName,
      meta: { title: menu.menuName },
      component: modules[componentPath],
    };
    router.addRoute(newRoute);
  });
}

app.provide('request', request);
app.provide('ocrRequest', ocr_request);
app.provide('router', router);
app.use(router);
app.use(ElementPlus);
app.use(store);
app.mount('#app');

