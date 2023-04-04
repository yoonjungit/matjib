<template>
  <div v-if="show" id="box">
    <div>
      <i @click="deleteBM()" class="fa fa-star" aria-hidden="true"></i>
    </div>
    <div>
      <span @click="showOnMap">
        <!--    1. 음식점 이름    -->
        <div style="font-size: medium">{{ bookmarks.resName }}</div>
        <!--    2. 음식점 리뷰    -->
        <div style="font-size: small">
          <span>
            <img
                src="https://user-images.githubusercontent.com/64455378/229777461-da741945-9607-45f0-956e-467942bcf0ac.jpg">
          </span>
               <span v-if="bookmarks.ncount>0"> {{ bookmarks.nscore }}({{ bookmarks.ncount }})</span>
               <span v-else> 후기 미제공</span>
          <span>&nbsp;
            <img
                src="https://user-images.githubusercontent.com/64455378/229777466-f0dabb0e-e0c2-4dff-9587-8f1284c9ca21.jpg">
          </span>
               <span v-if="bookmarks.kcount>0">{{ bookmarks.kscore }}({{ bookmarks.kcount }})</span>
               <span v-else> 후기 미제공</span>

          <span>&nbsp;
            <img
                src="https://user-images.githubusercontent.com/64455378/229777468-939349b7-cbcc-4e3c-a348-143629f0fe5a.jpg">
          </span>
               <span v-if="bookmarks.gcount>0">{{ bookmarks.gscore }}({{ bookmarks.gcount }})</span>
               <span v-else> 후기 미제공</span>

        </div>
        <!--    3. 음식점 주소    -->
        <div style="font-size: small"> {{ bookmarks.address }}</div>

      </span>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: 'Bookmark',
  props: {
    bookmarks: Object
  },

  data() {
    return {
      show: true
    }
  },

  methods: {
    async deleteBM() {
      const token = sessionStorage.getItem("token");
      const resId = this.bookmarks.id;
      let answer = confirm("북마크를 삭제하시겠습니까?")
      if (answer == true) {
        const res = await axios.post('/matjib/bookmark/delete', {token, resId});
        if (res.status == 200) {
          alert("삭제 되었습니다.")
          this.show = false
        } else {
          alert("처리되지 않았습니다.")
        }
      }
    },
    showOnMap() {
      this.$emit("showOnMap", this.bookmarks.address)
    }
  }
}
</script>

<style scoped>
#box {
  width: 400px;
  hegith: 100px;
  border: 1px solid #666666;
  border-radius: 5px;
  font-weight: bold;
  margin-left: 30px;
  margin-bottom: 30px;
  display: flex;
}

.fa {
  color: #E74C3C;
}
</style>