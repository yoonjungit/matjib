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
  font-family: 'Nanum Gothic', sans-serif;
  font-weight: 500;
  position: sticky;
  height: 100vh;
  width: 100%;
  /*background-color: navajowhite;*/
}
RouterView{
  margin: 10px;
  height: 100%;
  width: 100%;
}
</style>
