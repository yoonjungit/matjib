<template>
  <div class="archive">
Archive
  </div>
</template>

<script>
import router from "@/router";
import store from "@/store/store";

export default {
  name: 'Archive',
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

<style scoped>

</style>
