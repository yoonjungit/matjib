<template>
  <div class="spinner">
    <div class="spinner-grow text-primary" role="status">
      <span class="visually-hidden">Loading...</span>
    </div>
    <div class="spinner-grow text-secondary" role="status">
      <span class="visually-hidden">Loading...</span>
    </div>
    <div class="spinner-grow text-success" role="status">
      <span class="visually-hidden">Loading...</span>
    </div>
    <div class="spinner-grow text-danger" role="status">
      <span class="visually-hidden">Loading...</span>
    </div>
    <div class="spinner-grow text-warning" role="status">
      <span class="visually-hidden">Loading...</span>
    </div>
    <div class="spinner-grow text-info" role="status">
      <span class="visually-hidden">Loading...</span>
    </div>
    <div class="spinner-grow text-light" role="status">
      <span class="visually-hidden">Loading...</span>
    </div>
    <div class="spinner-grow text-dark" role="status">
      <span class="visually-hidden">Loading...</span>
    </div>
  </div>
</template>


<script>
import router from "@/router";
import store from "@/store/store";
import VueJwtDecode from 'vue-jwt-decode'
import VueCookies from "vue-cookies";
import api from "@/main";

export default {
  name: "Callback.vue",

  setup() {
    //네이버 로그인 시 인가 코드 주소에서 가져오기
    const tempToken = window.location.href.split('=')[1].split('&')[0];
    //로그인 이후 state 가져오기
    const callbackState = window.location.href.split('=')[2];

    const submit = async () => {
      try {
        const nickname = res.data;
        const token = VueCookies.get('token')
        const expTime = VueJwtDecode.decode(token).exp;
        const result = await api.post('/matjib/member/login', {tempToken, callbackState})

        sessionStorage.setItem('token', token);
        sessionStorage.setItem('expTime', expTime);
        store.commit('setNickname', nickname);
        await router.push({path: '/search'});
      } catch (error) {
        alert(error + "\n로그인에 실패하였습니다.")
        await router.push({path: '/'})
      }
    }

    //네이버 로그인 실패 시 -> 로그인 실패 알림 및 다시 로그인 화면("/")으로 가기
    if (tempToken.includes("access")) {
      alert("로그인에 실패하였습니다.")
      router.push({path: "/"})
      // state 값이 다를 시(사이트 위변조 방지- CSRF)
    } else if (callbackState != store.state.loginState) {
      alert("잘못된 접근입니다.")
      router.push({path: "/"})
    } else {    //로그인 성공 시 -> 서버로 인가 코드 보내서 정보 받기
      submit()
    }
  }

}

</script>

<style>
.spinner{
  margin:200px
}
</style>