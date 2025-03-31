// import './assets/main.css'

// import { createApp } from 'vue'
// import App from './App.vue'
// import router from './router'

// const app = createApp(App)

// app.use(router)

// app.mount('#app')


// import './assets/main.css'
import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import request from './js/request.js'
import ocr_request from './js/ocrRequest.js'
import router from './router'
import store from './store/index.js'

// 在应用启动时检查 localStorage 并重新添加路由
const savedMenu = JSON.parse(localStorage.getItem('menu'));
if (savedMenu && savedMenu.length > 0) {
    savedMenu.forEach(menu => {
        const newRoute = {
            path: '/' + menu.menuClick,
            name: menu.menuName,
            meta: { title: menu.menuName },
            component: () => import(`../components/${menu.menuComponent}.vue`),
        };
        router.addRoute(newRoute);
    });
}

const app = createApp(App)
app.provide('request', request)
app.provide('ocrRequest', ocr_request)
app.provide('router', router)
app.use(router)
app.use(ElementPlus)
app.use(store)
app.mount('#app')
