<template>
  <div class="startpage">
    <div class="background">
      <div class="textarea">
        <text color="white"><h1>찐찐Univ</h1></text>
        <div>
          <!-- 네이버 로그인 버튼 -->
          <img
              src="https://user-images.githubusercontent.com/64455378/228105101-f9582cd3-32ec-439c-b44a-be5bc30ef226.png"
              style="width:150px"
              @click="naverLogin()">
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import store from "@/store/store";

export default {
  name: "login",
  setup() {
    const client = process.env.VUE_APP_CLIENT_ID;
    const callback = process.env.VUE_APP_CALLBACK_URI;
    const randomState = Math.random().toString(36).substring(2, 12);

    const naverLogin = () => {
      store.commit('setNState', randomState);

      const url = "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=" +
          client + "&state=" + store.state.loginState + "&redirect_uri=" + callback
      window.location.replace(url);
    }
    return {naverLogin}
  }
}

</script>

<style scoped>
.textarea {
  color: white;
  text-align: center;
  padding: 400px 700px;
  position: absolute;
}

.background {
  position: relative;
  height: 100vh;
  overflow: hidden;
  margin: 0;
  background-size: 33.3333vw auto;
  background-image: linear-gradient(rgba(0, 0, 0, 0.527), rgba(0, 0, 0, 0.5)), url('~@/assets/bg.jpg'); /*어둡게 적용*/
  background-repeat: repeat;
  background-position: center;
  animation: movebg 15s linear infinite alternate; /* 15s : 목표지점까지 걸리는 시간 (속도), linear : 끝에 다다를 때 속도 일정, infinite : 무한반복, alternate : 왕복하기 */
}

@keyframes movebg {
  0% {
    background-position: 0 0;
  }
  50% {
    background-position: 1000px 1000px;
  }
  100% {
    background-position: 0 2000px;
  }
}
</style>