<template>
  <div class="starpage">
  <div class="background">
    <div class="textarea">
      <text color="white"><h1>찐찐Univ</h1></text>
      <RouterView/>
  </div>
  </div>
  </div>
</template>

<script>
export default {
  name: "Startpage"
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