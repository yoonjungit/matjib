import { createApp } from 'vue'
import App from './App.vue'
import router from "@/router/index";
import store from "@/store/store";
import VueCookies from "vue-cookies";

createApp(App)
    .use(router)
    .use(store)
    .use(VueCookies)
    .mount('#app')
