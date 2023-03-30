import { createRouter, createWebHistory} from 'vue-router'
import Archive from "@/pages/Archive.vue";
import List from "@/pages/List.vue";
import Search from "@/pages/Search.vue";
import MyPage from "@/pages/MyPage.vue";

const routes = [
    {path: '/archive', component: Archive},
    {path: '/list', component: List},
    {path: '/search', component: Search},
    {path: '/mypage', component: MyPage},
]
const router = createRouter({
    history: createWebHistory(),
    routes
})
export default router;