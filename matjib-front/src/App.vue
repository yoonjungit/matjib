<template>
  <div style="display:flex;">
    <div style="width : 17%; min-width: 280px">
      <Sidebar/>
    </div>
    <div style="width: 100% ; height: 100vh">
      <RouterView/>
    </div>
  </div>
</template>

<script>
import Sidebar from "@/components/Sidebar.vue";

export default {
  name: 'App',
  components: {
    Sidebar
  }
}
</script>

<style>
#app {
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen-Sans, Ubuntu, Cantarell, "Helvetica Neue", sans-serif;
  font-weight: 500;
  position: sticky;
  height: 100vh;
  weight: 100%;
  /*background-color: navajowhite;*/
}
RouterView{
  margin: 10px;
  height: 100%;
  width: 100%;
}
</style>
