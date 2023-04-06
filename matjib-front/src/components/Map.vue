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
      l1: null,
      l2: null,
      l3: null,
      l4: null,
      markers: [],
      restaurantArray: [Object],
      clusterer: null,
      resName: "",
      resAddress: "",
      nScore: null,
      kScore: null,
      gScore: null,
      nCount: null,
      kCount: null,
      gCount: null,
      map: null,
      show: false,
      infowindow: null,
      customOverlay: null,
      zoomControl: null,
      geocoder: null,
      content: '<div class="wrap"><div class="info"><div class="title">restaurant.resName<button id="bookmark">북마크</button></div><div class="body"><div class="address">주소</div><div class="naver"><img src="https://map.naver.com/v5/assets/icon/favicon-32x32.png" width="20px" height="20px"> 네이버 평점/갯수</div><div class="kakao"><img src="https://map.kakao.com/favicon.ico" width="20px" height="20px"> 카카오별점/갯수</div><div class="google"><img src="https://www.google.com/images/branding/product/ico/maps15_bnuw3a_32dp.ico" width="20px" height="20px"> 구글별점/갯수</div></div></div></div></div></div>' // 인포윈도우에 표시할 내용
    };
  },

  watch: {
    infoRes() {
      this.show = true
      if (this.bookmarks.includes(this.infoRes.id)) {
        console.log("여기 " + this.bookmarks)
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
      console.log("여기가 북마크/?" + JSON.stringify(this.bookmarks))
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
      console.log("추가해야할 것" + resId)
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
      //지도 객체를 등록합니다.
      //지도 객체는 반응형 관리 대상이 아니므로 initMap에서 선언합니다.
      this.map = new kakao.maps.Map(container, options);
      // 주소-좌표 변환 객체를 생성합니다
      this.geocoder = new kakao.maps.services.Geocoder();
      // 지도에 확대 축소 컨트롤을 생성한다.
      this.zoomControl = new kakao.maps.ZoomControl();
      // 지도의 우측에 확대 축소 컨트롤을 추가한다.
      this.map.addControl(this.zoomControl, kakao.maps.ControlPosition.RIGHT);

      // 지도가 이동, 확대, 축소로 인해 지도영역이 변경되면 마지막 파라미터로 넘어온 함수를 호출하도록 이벤트를 등록합니다
      kakao.maps.event.addListener(this.map, 'bounds_changed', function () {
        var bounds = this.map.getBounds();

        // 영역정보의 남서쪽 정보를 얻어옵니다
        var swLatlng = bounds.getSouthWest();
        // 스트링 변환 후 남서쪽 위도, 경도 자르기
        var sw = swLatlng.toString().split(',');
        var swLat = sw[0]
        var swLng = sw[1]
        var swLatitude = swLat.substr(1); // 최종 남서쪽 위도
        this.l1 = swLatitude
        var swLongitude = swLng.slice(0, -1); // 최종 남서쪽 경도
        this.l3 = swLongitude

        // 영역정보의 북동쪽 정보를 얻어옵니다
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
        show: false,
        marker: null
      });

      //서버에 음식점 리스트 요청
      axios.post("/matjib/restaurants/show", {
        //변수명 : 변수
        latS: this.l1, //위도(min)
        latE: this.l2, //위도(max)
        longS: this.l3, //경도(min)
        longE: this.l4, //경도(max)
      })
          .then(({data}) => {
            state.restaurants = data; //결과 restaurant배열에 저장
            console.log("검색 결과 : " + state.restaurants.length + "건");
            console.log("검색 결과 : " + JSON.stringify(state.restaurants));

            state.restaurants.forEach((restaurant) => {
              //마커 찍기
              console.log("이거확인" + this.markers.length);
              /*if (this.markers.length > 19) {
                this.markers.forEach((marker) => marker.setMap(null));
                this.markers = [];
              }*/
              if (restaurant.latitude !== 0) {
                //예외처리 : 만약 위도가 0이 아니라면
                //이전 마커 지우는것만 추가해주세요!
                console.log(restaurant.latitude, restaurant.longitude);
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

              kakao.maps.event.addListener(state.marker, 'click', function () {
                this.infoRes = restaurant;
                console.log(this.infoRes.resName)
              }.bind(this));
              console.log(this.markers.length);
            });
          })
          .catch((error) => {
            console.error(error); // 프로미스 자체에서 발생한 에러를 콘솔에 출력합니다.
          });
      return {state};
    },

    async setInfoWindow(lat, lng) {
      try {
        /*console.log(lat + lng)*/
        const {data} = await axios.post("/matjib/restaurants/getInfo", {
          lat: lat,
          lng: lng,
        });
        this.infoRes = data;
        console.log("검색 결과 : " + this.infoRes.length + "건");
        console.log("검색 결과 : " + JSON.stringify(this.infoRes));
      } catch (error) {
        console.error(error);
      }
    },

    search() {
      const state = reactive({
        restaurants: [],
      });

      console.log(this.resName);
      axios.post("/matjib/restaurants/search", {resName: this.resName}).then(({data}) => {
        state.restaurants = data;
        console.log(state.restaurants.length);
        console.log(JSON.stringify(state.restaurants))
        state.restaurants.forEach((restaurant) => {
          if (restaurant.latitude !== 0) {
            console.log(restaurant.latitude, restaurant.longitude)
            const coords = new kakao.maps.LatLng(restaurant.latitude, restaurant.longitude);
            const marker = new kakao.maps.Marker({
              map: this.map,
              position: coords
            });
            marker.setMap(this.map);
            this.map.setCenter(coords);
          }
          return {state}
        })
      })
    }
    ,
  },
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style>
#bookmark {
  width: 70px;
  float: right;
  margin-right: 10px;
  border: none;
  font-size: 17px;
  border-radius: 5px;
}

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
  position : absolute;
  bottom : 10%;
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