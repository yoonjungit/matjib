Skip to content
Search or jump to…
Pull requests
Issues
Codespaces
Marketplace
Explore

@zeehoonee
yoonjungit
/
matjib
Public
Fork your own copy of yoonjungit/matjib
Code
Issues
Pull requests
Actions
Projects
Wiki
Security
Insights
matjib/matjib-front/src/components/Map.vue
@zeehoonee
zeehoonee style : 인포윈도우 디자인, 지도 div 화면 꽉채우기, 검색창 지도 위에 올리기
…
Latest commit 231d9d3 2 hours ago
History
1 contributor
108 lines (96 sloc)  4.47 KB


<template>
  <div id="map">
    <div class="search">
      <form v-on:submit.prevent="initMap">
        <input type="text" v-model="searchIndex">
        <input type="submit" value="검색">
      </form>
    </div>
  </div>
</template>

<script>
export default {
  name: "Map",
  data() {
    return {
      searchIndex: "",
      map: null,
      marker: null,
      infowindow: null,
      zoomControl: null,
      coords: null,
      geocoder: null,
      content: '<div class="wrap"><div class="info"><div class="title">식당이름<button id="bookmark">북마크</button></div><div class="body"><div class="address">주소</div><div class="naver"><img src="https://map.naver.com/v5/assets/icon/favicon-32x32.png" width="20px" height="20px"> 네이버 평점/갯수</div><div class="kakao"><img src="https://map.kakao.com/favicon.ico" width="20px" height="20px"> 카카오별점/갯수</div><div class="google"><img src="https://www.google.com/images/branding/product/ico/maps15_bnuw3a_32dp.ico" width="20px" height="20px"> 구글별점/갯수</div></div></div></div></div></div>' // 인포윈도우에 표시할 내용
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
          "//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=" + process.env.VUE_APP_KAKAO_MAP_APP_KEY + "&libraries=services,clusterer,drawing";
      document.head.appendChild(script);
    }
  },
  methods: {
    initMap() {
      const container = document.getElementById("map");
      const options = {
        center: new kakao.maps.LatLng(38.04007, 126.92658), // 지도의 중심좌표
        level: 4, //지도의 확대 레벨
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

      // 주소로 좌표를 검색합니다
      this.geocoder.addressSearch(this.searchIndex, function (result, status) {
        // 정상적으로 검색이 완료됐으면
        if (status === kakao.maps.services.Status.OK) {

          this.coords = new kakao.maps.LatLng(result[0].y, result[0].x);

          // 결과값으로 받은 위치를 마커로 표시합니다
          this.marker = new kakao.maps.Marker({
            map: this.map,
            position: this.coords
          })

          // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
          this.map.setCenter(this.coords);

          // 마커 위에 표시할 인포윈도우를 생성한다
          this.infowindow = new kakao.maps.InfoWindow({
            content: this.content
          });

          // 마커에 클릭 이벤트를 등록한다 (우클릭 : rightclick)
          kakao.maps.event.addListener(this.marker, 'click', function () {
            this.infowindow.open(this.map, this.marker);
          }.bind(this));

          // 지도에 클릭 이벤트를 등록한다.
          kakao.maps.event.addListener(this.map, 'click', function () {
            this.infowindow.close();
          }.bind(this));
        }
      }.bind(this));
    },
  },
};
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

.wrap {
  width: 288px;
  height: 150px;
}

.wrap .info {
  width: 286px;
  height: 150px;
  border-radius: 0;
  border-bottom: 2px solid #ccc;
  border-right: 1px solid #ccc;
  overflow: hidden;
  background: #fff;
}

.wrap .info:nth-child(1) {
  border: 0;
  box-shadow: 0 1px 2px #888;
}

.info .title {
  padding: 5px 0 0 10px;
  height: 40px;
  background: #E74C3C;
  border-bottom: 1px solid #ddd;
  color: white;
  font-size: 18px;
  font-weight: bold;
}

.info .body {
  margin-left: 10px;
  position: relative;
  overflow: hidden;
}

.address {
  font-size: 17px;
  color: #E74C3C;
}

.search {
  position: relative;
  z-index: 2;
}

#map {
  z-index: 1;
  position: relative;
  width: 100%;
  height: 100vh;
}
</style>