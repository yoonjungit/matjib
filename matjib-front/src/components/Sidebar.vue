<template>
  <div class="d-flex flex-column flex-shrink-0 p-3" style=" height: 100vh">
    <div class="sidebar">
      <router-link to="/search" style="font-size:30px; font-weight : bold; text-align: center">찐찐Univ</router-link>
      <br>
      <router-link to="/mypage" style="text-align: left;">
        <div class="profile">
          <i class="fa fa-graduation-cap" aria-hidden="true"></i>
          <div style="margin-left: 10px;">User1</div>
        </div>
      </router-link>
      <hr>
      <ul style="list-style: none;">
        <li>
          <router-link to="/search" class="">
            <i class="fa fa-search" aria-hidden="true"></i> &nbsp;
            Search
          </router-link>
        </li>
        <li>
          <router-link to="/list" class="">
            <i class="fa fa-book" aria-hidden="true"></i> &nbsp;
            List
          </router-link>
        </li>
        <li>
          <router-link to="/archive" class="">
            <i class="fa fa-folder-o" aria-hidden="true"></i> &nbsp;
            Archive
          </router-link>
        </li>

      </ul>
      <hr>
      <div class="dropdown">

        <ul class="dropdown-menu text-small shadow">
          <li><a class="dropdown-item" href="#">New project...</a></li>
          <li><a class="dropdown-item" href="#">Settings</a></li>
          <li><a class="dropdown-item" href="#">Profile</a></li>
          <li>
            <hr class="dropdown-divider">
          </li>
          <li><a class="dropdown-item" href="#">Sign out</a></li>
        </ul>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'Sidebar',
  props: {
    msg: String
  }
}
</script>

<style scoped>
* {
  background-color: #E74C3C;
}

.sidebar {
  padding: 20px;
}

.profile {
  width:100%;
  font-size: 15px;
  display: flex;
  align-items: center;
  margin-top: 10px;
  margin-left : 10px;
}

a {
  text-decoration: none;
  color: white;
  font-size: 17px;
}

li {
  margin-bottom: 15px;
}
</style>