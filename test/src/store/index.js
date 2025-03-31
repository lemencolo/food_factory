import { createStore } from 'vuex';
import router from '../router/index';

function addNewRouter() {
  // 清除之前的动态路由
  menuList.forEach(menu => {
    const existingRoute = router.getRoutes().find(route => route.path === '/' + menu.menuClick);
    if (existingRoute && existingRoute.name) {
      router.removeRoute(existingRoute.name);
    }
  });

  // 遍历菜单列表并动态添加平行路由
  menuList.forEach(menu => {
    const newRoute = {
      path: '/' + menu.menuClick,
      name: menu.menuName,
      meta: { title: menu.menuName },
      component: () => import(`@/components/${menu.menuComponent}.vue`),
    };
    router.addRoute(newRoute);
  });
}

const store = createStore({
  state(){
    const storedMenu = localStorage.getItem('menu');
    return {
      menu: storedMenu ? JSON.parse(storedMenu): [] ,
    };
  },
  mutations: {
    setMenu() {
      state.menu = menuList;
      localStorage.setItem('menu', JSON.stringify(menuList)); // 保存到 localStorage
      addNewRouter(menuList);
    }
  },
  getters: {
    getMenu() {
      return state.menu;
    }
  }
});

export default store;


