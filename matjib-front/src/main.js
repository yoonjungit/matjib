import { createApp } from 'vue'
import App from './App.vue'
import router from "@/router/index";
import store from "@/store/store";
import axios from "axios";

const api = axios.create({
    baseURL: process.env.VUE_APP_BASE_URL,
    withCredentials: true,
});
export default api;

const app = createApp(App);

app.config.globalProperties.$logout = async function () {
    await api.get("/matjib/member/logout").finally(() => {
        store.commit('setNickname', 0);
        sessionStorage.clear();
        router.push('/')
    })
}

app.use(router)
    .use(store)
    .mount('#app')