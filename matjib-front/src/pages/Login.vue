<template>
  <div>
<!-- 네이버 로그인 버튼 -->
    <img
      src="https://user-images.githubusercontent.com/64455378/228105101-f9582cd3-32ec-439c-b44a-be5bc30ef226.png"
      style="width:150px"
      @click="naverLogin()">
  </div>
</template>

<script>
import store from "@/store/store";
export default {
  name: "Login.vue",

  setup() {
    const client = process.env.VUE_APP_CLIENT_ID;
    const callback = process.env.VUE_APP_CALLBACK_URI;
    const randomState = Math.random().toString(36).substring(2, 12);
    const naverLogin = () => {
      store.commit('setNState', randomState);
      const url = "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id="+
          client + "&state=" + store.state.loginState + "&redirect_uri=" + callback
      window.location.replace(url);

    }
    return {naverLogin}

  }
}
</script>

<style scoped>

</style>