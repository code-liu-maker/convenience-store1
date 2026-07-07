import { createRouter, createWebHistory } from "vue-router";

import Login from "@/components/login/Login.vue";
import Register from "@/components/login/Register.vue";
import AdminIndex from "@/components/admin/AdminIndex.vue";
import AdminHome from "@/components/admin/AdminHome.vue";
import Dashboard from "@/components/admin/Dashboard.vue";
import Products from "@/components/admin/Products.vue";
import Categories from "@/components/admin/Categories.vue";
import Orders from "@/components/admin/Orders.vue";
import Stock from "@/components/admin/Stock.vue";
import Suppliers from "@/components/admin/Suppliers.vue";
import Users from "@/components/admin/Users.vue";
import StockLog from "@/components/admin/StockLog.vue";
import AppIndex from "@/components/app/AppIndex.vue";
import AppHome from "@/components/app/AppHome.vue";
import AppProducts from "@/components/app/Products.vue";
import Cart from "@/components/app/Cart.vue";
import AppOrders from "@/components/app/Orders.vue";
import Profile from "@/components/app/Profile.vue";
import UserInfo from "@/components/common/UserInfo.vue";

const router = createRouter({
    history: createWebHistory(),
    routes: [
        { path: "/", redirect: '/login' },
        { path: "/login", component: Login, name: 'login' },
        { path: "/register", component: Register, name: 'register' },
        {
            path: '/admin',
            component: AdminIndex,
            redirect: '/admin/dashboard',
            children: [
                { path: '/admin/home', component: AdminHome },
                { path: '/admin/dashboard', component: Dashboard },
                { path: '/admin/products', component: Products },
                { path: '/admin/categories', component: Categories },
                { path: '/admin/orders', component: Orders },
                { path: '/admin/stock', component: Stock },
                { path: '/admin/suppliers', component: Suppliers },
                { path: '/admin/users', component: Users },
                { path: '/admin/stocklog', component: StockLog },
                { path: '/admin/userinfo', component: UserInfo },
            ]
        },
        {
            path: '/app',
            component: AppIndex,
            redirect: '/app/home',
            children: [
                { path: '/app/home', component: AppHome },
                { path: '/app/products', component: AppProducts },
                { path: '/app/cart', component: Cart },
                { path: '/app/orders', component: AppOrders },
                { path: '/app/profile', component: Profile },
                { path: '/app/userinfo', component: UserInfo },
            ]
        }
    ]
});

// 路由守卫
router.beforeEach((to, from, next) => {
    const userInfo = sessionStorage.getItem('userInfo');

    if (to.path === '/login' || to.path === '/register') {
        next();
        return;
    }

    if (!userInfo) {
        next('/login');
        return;
    }

    const user = JSON.parse(userInfo);

    // 后台页面：操作员(1)和超级管理员(2)可以访问
    if (to.path.startsWith('/admin')) {
        if (user.userType !== 1 && user.userType !== 2) {
            next('/app/home');
            return;
        }
        next();
        return;
    }

    // 前台页面：所有人都可以访问
    next();
});

export default router;