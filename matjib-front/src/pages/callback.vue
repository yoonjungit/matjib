<template>
  <div>

  </div>
</template>


<script>
import axios from "axios";
import router from "@/router";
import store from "@/store/store";
import VueJwtDecode from 'vue-jwt-decode'

export default {
  name: "Callback.vue",

  mounted() {
    //네이버 로그인 시 인가 코드 주소에서 가져오기
    const tempToken = window.location.href.split('=')[1].split('&')[0];
    //로그인 이후 state 가져오기
    const callbackState = window.location.href.split('=')[2];

    const submit = () => {
      axios.post('/matjib/member/login', {tempToken, callbackState}).then((res) => {
        const nickname = res.data;
        store.commit('setNickname', nickname);

        const token = this.$cookies.get("token")
        const expTime = VueJwtDecode.decode(this.$cookies.get("token")).exp;
        sessionStorage.setItem('token', token);
        sessionStorage.setItem('expTime', expTime);

        router.push({path:'/search'});
      })
          .catch((error) => {
            alert(error + "\n로그인에 실패하였습니다.")
          })
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

</style>