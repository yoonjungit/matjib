<template>
  <div id="main">
    <div class="controller"> <!--controller-->

      <div id="infowindow"> <!--infowindow-->
        <div id="nameAddWindow"> <!--nameAddWindow-->
          <span id="starNameWindow"> <!--starNameWindow-->
          <span v-if="show"> <!--별 아이콘-->
            <i v-if="this.bookmarked" @click="deleteBM()" class="fa fa-star" aria-hidden="true"></i>
            <i v-else @click="addBM" class="fa fa-star-o" aria-hidden="true"></i>
          </span> <!--별 아이콘-->
          <span id="resTitle">{{ infoRes.resName }}</span> <!-- 음식점 이름 -->
            </span> <!--/starNameWindow-->
          <div>   <!-- 주소 -->
            <span id="resAddress">{{ infoRes.address }}</span>
          </div>  <!-- /주소 -->
        </div> <!--/nameAddWindow-->

        <div id="scoresWindow"> <!--scoresWindow-->
          <div id="scores"> <!--scores-->
            <span id="nIcon" v-if="show">
            <img
                src="https://user-images.githubusercontent.com/64455378/229777468-939349b7-cbcc-4e3c-a348-143629f0fe5a.jpg">
          </span>
            <span id="nScore">
        <span v-if="infoRes.ncount>0"> {{ infoRes.nscore }}점 ({{ infoRes.ncount }}건)</span>
        <span v-else-if="infoRes.ncount==0"> 후기 미제공</span>
        <span v-else> &nbsp;</span>
          </span>
            <span id="kIcon" v-if="show">&nbsp;
            <img
                src="https://user-images.githubusercontent.com/64455378/229777466-f0dabb0e-e0c2-4dff-9587-8f1284c9ca21.jpg">
          </span>
            <span id="kScore">
        <span v-if="infoRes.kcount>0"> {{ infoRes.kscore }}점 ({{ infoRes.kcount }}건)</span>
        <span v-else-if="infoRes.kcount==0"> 후기 미제공</span>
        <span v-else> &nbsp;</span>
            </span>
            <span id="gIcon" v-if="show">&nbsp;
            <img
                src="https://user-images.githubusercontent.com/64455378/229777461-da741945-9607-45f0-956e-467942bcf0ac.jpg">
          </span>
            <span id="gScore">
        <span v-if="infoRes.gcount>0"> {{ infoRes.gscore }}점 ({{ infoRes.gcount }}건)</span>
        <span v-else-if="infoRes.gcount==0"> 후기 미제공</span>
        <span v-else> &nbsp;</span>
            </span>
          </div> <!--scores-->
        </div> <!-- /scoresWindow-->

      </div> <!--/infowindow-->

      <div id="searchWindow"> <!--searchWindow-->
        <div id="search">
          <form v-on:submit.prevent="search">
            <input id="searchInput" type="text" v-model="resName" placeholder="맛집 이름으로 검색">
            <input id="searchButton" type="submit" value="검색">
          </form>
        </div>
        <div id="btn">
          <button id="getMarkersBtn" @click="getMarkers()">근처 맛집 보기</button>
        </div>
      </div> <!--/searchWindow-->

    </div>  <!-- controller -->

    <div id="map"></div>

  </div> <!-- main-->

</template>

<script>
import {reactive} from "vue";
import axios from "axios";

export default {
  name: "Map",
  data() {
    return {
      bookmarks: [],
      bookmarked: false,
      infoRes: Object,
      l1: null, l2: null,
      l3: null, l4: null,
      markers: [],
      restaurantArray: [Object],
      show: false,
      map: null,
      zoomControl: null,
    };
  },

  watch: {
    infoRes() {
      this.show = true
      if (this.bookmarks.includes(this.infoRes.id)) {
        this.bookmarked = true;
      } else {
        this.bookmarked = false;
      }
    }
  },

  mounted() {
    const token = sessionStorage.getItem("token");
    axios.post("/matjib/bookmark/getbmid", token).then(({data}) => {
      this.bookmarks = data;
    })


    if (window.kakao && window.kakao.maps) {
      this.initMap();
    } else {
      const script = document.createElement("script");
      /* global kakao */
      script.onload = () => kakao.maps.load(this.initMap);
      script.src =
          "//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey="
          + process.env.VUE_APP_KAKAO_MAP_APP_KEY + "&libraries=services,clusterer,drawing";
      document.head.appendChild(script);
    }
  },
  methods: {

    async deleteBM() {
      const token = sessionStorage.getItem("token");
      const resId = this.infoRes.id;
      let answer = confirm("북마크를 삭제하시겠습니까?")
      if (answer == true) {
        const res = await axios.post('/matjib/bookmark/delete', {token, resId});
        if (res.status == 200) {
          alert("삭제 되었습니다.")
          this.bookmarked = false;
          let indexBM = this.bookmarks.indexOf(resId);
          this.bookmarks.splice(indexBM, 1);
        } else {
          alert("처리되지 않았습니다.")
        }
      }
    },

    async addBM() {
      const token = sessionStorage.getItem("token");
      const resId = this.infoRes.id;
      const res = await axios.post('/matjib/bookmark/add', {token, resId});
      if (res.status == 200) {
        alert("추가 되었습니다.")
        this.bookmarked = true;
        this.bookmarks.push(resId);
      } else {
        alert("처리되지 않았습니다.")
      }
    },


    initMap() {
      const container = document.getElementById("map");
      const options = {
        center: new kakao.maps.LatLng(37.555184166, 126.936910322), // 지도의 중심좌표
        level: 1, //지도의 확대 레벨
      };
      //지도 객체를 등록
      this.map = new kakao.maps.Map(container, options);
      // 지도에 확대 축소 컨트롤
      this.zoomControl = new kakao.maps.ZoomControl();
      // 우측에 확대 축소 컨트롤
      this.map.addControl(this.zoomControl, kakao.maps.ControlPosition.RIGHT);

      // 지도가 이동, 확대, 축소로 인해 지도영역이 변경되면 마지막 파라미터로 넘어온 함수를 호출하도록 이벤트 등록
      kakao.maps.event.addListener(this.map, 'bounds_changed', function () {
        var bounds = this.map.getBounds();

        // 영역정보의 남서쪽 정보
        var swLatlng = bounds.getSouthWest();
        // 스트링 변환 후 남서쪽 위도, 경도 자르기
        var sw = swLatlng.toString().split(',');
        var swLat = sw[0]
        var swLng = sw[1]
        var swLatitude = swLat.substr(1); // 최종 남서쪽 위도
        this.l1 = swLatitude
        var swLongitude = swLng.slice(0, -1); // 최종 남서쪽 경도
        this.l3 = swLongitude

        // 영역정보의 북동쪽 정보
        var neLatlng = bounds.getNorthEast();
        // 스트링 변환 후 북동쪽 위도, 경도 잘라내기
        var ne = neLatlng.toString().split(',');
        var neLat = ne[0]
        var neLng = ne[1]
        var neLatitude = neLat.substr(1); // 최종 북동쪽 위도
        this.l2 = neLatitude
        var neLongitude = neLng.slice(0, -1); // 최종 북동쪽 경도
        this.l4 = neLongitude

      }.bind(this));
    },

    getMarkers() {
      //검색결과 담을 반응형 배열 선언
      const state = reactive({
        restaurants: [],
        marker: null
      });

      //서버에 음식점 리스트 요청
      axios.post("/matjib/restaurants/show", {
        latS: this.l1, //위도(min)
        latE: this.l2, //위도(max)
        longS: this.l3, //경도(min)
        longE: this.l4, //경도(max)
      })
          .then(({data}) => {
            state.restaurants = data; //결과 restaurant배열에 저장
            state.restaurants.forEach((restaurant) => {
              if (restaurant.latitude !== 0) {
                const coords = new kakao.maps.LatLng(
                    restaurant.latitude,
                    restaurant.longitude
                );
                state.marker = new kakao.maps.Marker({
                  title: restaurant.resName,
                  map: this.map,
                  position: coords,
                });
                state.marker.setMap(this.map);
                this.restaurantArray.push(restaurant);
                this.markers.push(state.marker);
              }

              kakao.maps.event.addListener(state.marker, 'click', function () { //마커 클릭 시 음식점 보이기 이벤트
                this.infoRes = restaurant;
              }.bind(this));
            });
          })
          .catch((error) => {
            console.error(error);
          });
      return {state};
    },

    search() {
      const state = reactive({
        searchRestaurants: [],
      });
      axios.post("/matjib/restaurants/search", {resName: this.resName}).then(({data}) => {
        state.searchRestaurants = data;
        state.searchRestaurants.forEach((restaurant) => {
          if (restaurant.latitude !== 0) {
            const coords = new kakao.maps.LatLng(restaurant.latitude, restaurant.longitude);
            const marker = new kakao.maps.Marker({
              map: this.map,
              position: coords
            });
            marker.setMap(this.map);
            this.map.setCenter(coords);
            kakao.maps.event.addListener(marker, 'click', function () { //마커 클릭 시 음식점 보이기 이벤트
              this.infoRes = restaurant;
            }.bind(this));
          }
          return {state}
        })
      })
    }
    ,
  },
}
</script>

<style>
.controller {
  display: flex;
  position: relative;
  width: 100%;
  height: 10vh;
  vertical-align: middle;
}

#infowindow {
  padding-left: 20px;
  height: 100%;
  width: 84%;
  display: flex;
  background: white;
}

#nameAddWindow {
  margin-top: 5px;
  height: 100%;
  width: 65%;
  display: block;
  position: relative;
}

#starNameWindow {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

#resTitle {
  font-size: xx-large;
  margin-left: 10px;
  text-overflow: ellipsis; /*식당 이름이 길어지면 생략 기호(...)로 표시*/
}

#resAddress {
  position: absolute;
  bottom: 10%;
  font-size: large;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: salmon;
}

#scoresWindow {
  font-size: large;
  height: 100%;
  width: 49%;
  position: relative;
  display: block;
}

#scores {
  position: absolute;
  top: 30%;
  width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-left: 10px;
}

#searchWindow {
  position: relative;
  width: 16%;
  height: 100%;
  text-align: center;
  background: white;
}

#search {
  position: relative;
  margin-top: 5px;
  width: 100%;
  height: 50%;
}

#searchInput {
  width: 75%;
}

#searchButton {
  width: 25%;
}

#getMarkersBtn {
  width: 100%;
  height: 50%;
}

#map {
  z-index: 1;
  position: relative;
  width: 100%;
  height: 90vh;
}

#main {
  position: relative;
  height: 100vh;
}
</style>