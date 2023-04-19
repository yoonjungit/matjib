<template>
  <div class="d-flex flex-column flex-shrink-0 p-3" style=" height: 100vh">
    <div class="sidebar">
      <router-link to="/search" style="font-size:30px; font-weight : bold; text-align: center">
        찐맛집&nbsp;<i class="fa fa-cutlery" aria-hidden="true"></i>
      </router-link>
      <br>
      <div class="profile">
        <i class="fa fa-graduation-cap" aria-hidden="true"></i>
        <router-link to="/mypage" style="text-align: left;">
          <div style="margin-left: 10px;" v-if="$store.state.account.nickname">
            {{ $store.state.account.nickname }}
          </div>
        </router-link>
        <div style="margin-left: 10px;" v-if="!$store.state.account.nickname">로그인 후 사용해주세요.</div>
        <a to="/" v-else @click="logout()" style="display: flex; align-items: center; margin-left: auto;">
          <div style="margin-right: 5px;">
            <i class="fa fa-sign-out" aria-hidden="true" style="float: right"></i>
          </div>
        </a>
      </div>
      <hr>
      <ul style="list-style: none;">
        <li>
          <router-link to="/search" class="">
            <i class="fa fa-search" aria-hidden="true"></i> &nbsp;
            Search
          </router-link>
        </li>
        <li>
          <router-link to="/archive" class="">
            <i class="fa fa-star" aria-hidden="true"></i> &nbsp;
            Bookmark
          </router-link>
        </li>
        <li>
          <router-link to="/mypage" class="">
            <i class="fa fa-user" aria-hidden="true"></i> &nbsp;
            MyPage
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
import axios from "axios";
import store from "@/store/store";
import router from "@/router";

export default {
  name: 'Sidebar',
  props: {
    msg: String
  },

  setup() {
    const logout = async () => {
      await axios.post("/matjib/member/logout").then(() => {
        store.commit('setNickname', 0);
        sessionStorage.clear();
        router.push({path: "/"});
      })
          .catch(() => {
            store.commit('setNickname', 0);
            sessionStorage.clear();
            router.push({path: "/"});
          })
    }
    return {logout}
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
  width: 100%;
  font-size: 15px;
  display: flex;
  align-items: center;
  margin-top: 10px;
  margin-left: 10px;
  color: white;
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