import {createRouter, createWebHistory} from 'vue-router'
import Archive from "@/pages/Archive.vue";
import Search from "@/pages/Search.vue";
import MyPage from "@/pages/MyPage.vue";
import Login from "@/pages/Login.vue"
import Callback from "@/pages/callback.vue";
import store from "@/store/store";

const routes = [
    {path: '/archive', component: Archive, meta: {requiresAuth: true}},
    {path: '/search', component: Search, meta: {requiresAuth: true}},
    {path: '/mypage', component: MyPage, meta: {requiresAuth: true}},
    {path: '/', component: Login, meta: {public: true}},
    {path: '/callback', component: Callback, meta: {public: true}},
];

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach((to, from, next) => {
    const isPublic = to.matched.some(record => record.meta.public);
    const requiresAuth = to.matched.some(record => record.meta.requiresAuth);
    const token = sessionStorage.getItem('token');
    const tokenExpireTime = sessionStorage.getItem('expTime');

    const logout = () => {
        store.commit('setNickname', 0);
        sessionStorage.removeItem('token');
        sessionStorage.removeItem('expTime');
        next('/');
    }

    if(!token){
        store.commit('setNickname', 0);
    }
    if (isPublic && token) {            //로그인 했는데 public 페이지에 있으면 검색 페이지로 라우팅
        return next('/search');
    } else if (requiresAuth && !token) {
        // 로그인 안한 사용자는 로그인 페이지로 이동
        alert("로그인 이후 이용 가능합니다.")
        logout();
    } else if (requiresAuth && token && tokenExpireTime) {
        // 토큰이 있고 로그인이 필요한 페이지인 경우
        const now = new Date().getTime() / 1000; // 현재 시간
        const expireTime = parseInt(tokenExpireTime); // 만료 시간
        if (now > expireTime) {
            // 토큰이 만료되었으면 로그인 페이지로 이동
            alert("다시 로그인 해주세요.");
            logout();
        }
    }
    next();
});

export default router;