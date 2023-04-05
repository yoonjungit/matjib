<template>
  <div id="main">
    <div class="controller">
      <div id="infowindow">
        <i @click="addBM()" class="fa fa-star" aria-hidden="true"></i>
        <span style="font-size: 25px">{{ infoRes.resName }}</span>
        <span>대현동 56-129번지</span>
        <div>
          <span><img src="https://map.naver.com/v5/assets/icon/favicon-32x32.png" width="20px" height="20px"> 네이버 평점/갯수</span>
          <span><img src="https://map.kakao.com/favicon.ico" width="20px" height="20px"> 카카오별점/갯수</span>
          <span><img src="https://www.google.com/images/branding/product/ico/maps15_bnuw3a_32dp.ico" width="20px"
                     height="20px"> 구글별점/갯수</span>
        </div>
      </div>
      <div id="search">
        <button id="getMarkersBtn" @click="getMarkers()">근처 맛집 보기</button>
        <form v-on:submit.prevent="search">
          <input type="text" v-model="resName">
          <input type="submit" value="검색">
        </form>
        <!--      <p id="latlon"></p> &lt;!&ndash;남서쪽/북동쪽 위도, 경도 표시 하려고 임시로 만듬&ndash;&gt;-->
      </div>
    </div>
    <div id="map">
    </div>
  </div>

</template>

<script>
import {reactive} from "vue";
import axios from "axios";

export default {
  name: "Map",
  data() {
    return {
      infoRes:Object,
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
  mounted() {
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

        // 가져온 위도, 경도 정보 표시 (나중에 지울 것)
        /*var message = '<p>남서쪽<br>' + this.l1 + '<br>' + this.l3 + '<br>북동쪽<br>' + this.l2 + '<br>' + this.l4 + '</p>';
        var resultDiv = document.getElementById('latlon');
        resultDiv.innerHTML = message;*/
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
                  /*var lat = marker.getPosition().getLat()
                  var lng = marker.getPosition().getLng()
                  console.log(lat);
                  console.log(lng);*/

                  /*const lat = restaurant.latitude
                  const lng = restaurant.longitude*/
                  /*console.log("이거" + lat);*/
              }
            // 마커에 클릭 이벤트를 등록한다 (우클릭 : rightclick)
            kakao.maps.event.addListener(state.marker, 'click', function () {
              const name = restaurant.resName;
              this.showInfoWindow(name);
            }.bind(this));
              console.log(this.markers.length);
            });
          })
          .catch((error) => {
            console.error(error); // 프로미스 자체에서 발생한 에러를 콘솔에 출력합니다.
          });
      return {state};
    },

    showInfoWindow(name) {
      console.log("자자자자자자자" + name);
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

#search {
  display: flex;
  position: relative;
  z-index: 2;
  padding-left: 10px;
  width: 30%;
  height: 100%;
}

.controller {
  display: flex;
  position: relative;
  width: 100%;
  height: 10vh;
  vertical-align: middle;
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

#infowindow {
  padding-left: 20px;
  height: 100%;
  width: 70%;
}
</style>