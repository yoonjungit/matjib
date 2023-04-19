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
    const tokenExpireTime = sessionStorage.getItem('expTime');
    const now = new Date().getTime() / 1000;

    if (!tokenExpireTime) {    //토큰 유효시간이 없는 경우(로그인 전)
        store.commit('setNickname', 0);
    } else if (tokenExpireTime > now) {   //토큰 유효시간이 지난 경우
        alert("로그인 해주세요.");
        this.$logout();
    }

    if (isPublic && tokenExpireTime) {            //로그인 했는데 public 페이지에 있으면 검색 페이지로 라우팅
        return next('/search');
    } else if (requiresAuth && !tokenExpireTime) {
        // 로그인 안한 사용자는 로그인 페이지로 이동
        alert("로그인 이후 이용 가능합니다.")
        this.$logout()
    }
    next();
    console.log(tokenExpireTime)
});

export default router;