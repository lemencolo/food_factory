import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router';
import Login from '@/components/Login.vue';

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/Index',
    component: () => import('@/components/Index.vue'),
    children: [
      {
        path: '',
        name: 'Main',
        component: () => import('@/components/Index/Main.vue'),
        meta: { requiresAuth: true }
      },
      {
        path: '/SudoSetting',
        name: 'SudoSetting',
        component: () => import('@/components/Index/SudoSetting.vue'),
        meta: { requiresAuth: true }
      },
      {
        path: '/TodoManagement',
        name: 'TodoManagement',
        component: () => import('@/components/Index/TodoManagement.vue'),
        meta: { requiresAuth: true }
      },
      {
        path: '/FormulaManagement',
        name: 'FormulaManagement',
        component: () => import('@/components/Index/FormulaManagement.vue'),
        meta: { requiresAuth: true }
      },
    ]
  },
  {
    path: '/Management',
    name: 'Management',
    component: () => import('@/components/Management.vue'),
    meta: { requiresAuth: true }
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// 全局前置守卫
router.beforeEach(async (to, from, next) => {
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth);
  const isAuthenticated = localStorage.getItem('isAuthenticated') === 'true'; // 检查登录状态

  // 如果需要认证但未登录，则重定向到登录页
  if (requiresAuth && !isAuthenticated) {
    next('/login');
    return;
  }

  // 加载动态路由
  const savedMenu = JSON.parse(localStorage.getItem('menu') || '[]');  // 安全地获取存储的菜单数据
  if (savedMenu.length > 0) {
    for (const menu of savedMenu) {
      const existingRoute = router.getRoutes().find(route => route.path === '/' + menu.menuClick);
      if (!existingRoute) {
        try {
          const newRoute: RouteRecordRaw = {
            path: '/' + menu.menuClick,
            name: menu.menuName,
            meta: { title: menu.menuName },
            component: () => import(`../components/${menu.menuComponent}.vue`),
          };
          router.addRoute(newRoute);
        } catch (error) {
          console.error('Failed to load dynamic route:', error);
        }
      }
    }
  }

  // 继续导航
  next();
});

export default router;