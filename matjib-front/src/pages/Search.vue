<template>
  <div class="search">
    search
  </div>
</template>

<script>

import store from "@/store/store";
import router from "@/router";

export default {
  name: 'Search',
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
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
