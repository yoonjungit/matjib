<template>

  <div class="list" style="overflow:auto;  height:95%; font-size: 60px; margin:10px; align-items: center">
    스크롤<br>    스크롤<br>
    스크롤<br>    스크롤<br>
    스크롤<br>    스크롤<br>
    스크롤<br>    스크롤<br>
    스크롤<br>    스크롤<br>
    스크롤<br>    스크롤<br>
    스크롤<br>    스크롤<br>
  </div>

</template>

<script>
import store from "@/store/store";
import router from "@/router";

export default {
  name: 'List',
  components: {
  },
  mounted() {
    if (store.state.account.id == 0){
      alert("로그인 후 사용 가능 합니다.")
      router.push({path: "/"})
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
