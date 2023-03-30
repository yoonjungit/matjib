<template>
  <div>

  </div>
</template>

<script>
import axios from "axios";
import router from "@/router";
import store from "@/store/store";

export default {
  name: "callback.vue",

  mounted() {
    const webAddress = window.location.href;
    //네이버 로그인 시 인가 코드 주소에서 가져오기
    const tempToken = webAddress.split('=')[1].split('&')[0];
    //로그인 이후 state 가져오기
    const callbackState = webAddress.split('=')[2];

    const submit = () => {
      axios.post('/matjib/member/login', {tempToken, callbackState}).then((res) => {
        const { data } = res;
        store.commit('setNState', 0);
        store.commit('setAccount', data.id);
        store.commit('setNickname', data.nickname);

        router.push({path: "/search"})
        console.log('success');
      })
    };

    //네이버 로그인 실패 시 -> 로그인 실패 알림 및 다시 로그인 화면("/")으로 가기
    if (tempToken.includes("access")) {
      alert("로그인에 실패하였습니다.")
      router.push({path: "/"})
      // state 값이 다를 시(사이트 위변조 방지- CSRF)
    }else if(callbackState!=store.state.loginState) {
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