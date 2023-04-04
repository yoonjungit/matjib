<template>
    <div id="map">
      <div class="search">
        <input type="text" name="searchIndex" onclick="this.value ='';" value="식당명을 입력하세요.">
        <button type="submit">검색</button>
      </div>
    </div>
</template>

<script>
export default {
  name: "Map",
  data() {
    return {
      map: null,
      marker: null,
      infowindow: null,
      zoomControl: null,
      geocoder: null,
      content: '<div class="wrap"><div class="info"><div class="title">식당이름</div><div class="buttons"><button>리스트</button><button>북마크</button></div><div class="body"><div class="address">주소</div><div class="naver">네이버 별점/갯수</div><div class="kakao">카카오별점/갯수</div><div class="google">구글별점/갯수</div></div></div></div></div></div>' // 인포윈도우에 표시할 내용
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
          "//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=915cffed372954b7b44804ed422b9cf0&libraries=services,clusterer,drawing";
      document.head.appendChild(script);
    }
  },
  methods: {
    initMap() {
      const container = document.getElementById("map");
      const options = {
        center: new kakao.maps.LatLng(38.04007, 126.92658), // 지도의 중심좌표벨
        level: 4, //지도의 확대 레벨
      };

      //지도 객체를 등록합니다.
      //지도 객체는 반응형 관리 대상이 아니므로 initMap에서 선언합니다.
      this.map = new kakao.maps.Map(container, options);
      this.geocoder = new kakao.maps.services.Geocoder();


      // 지도에 확대 축소 컨트롤을 생성한다.
      this.zoomControl = new kakao.maps.ZoomControl();

      // 지도의 우측에 확대 축소 컨트롤을 추가한다.
      this.map.addControl(this.zoomControl, kakao.maps.ControlPosition.RIGHT);

      // 지도에 마커를 생성하고 표시한다
      this.marker = new kakao.maps.Marker({
        position: new kakao.maps.LatLng(38.04007, 126.92658), // 마커의 좌표
        map: this.map // 마커를 표시할 지도 객체
      });

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
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style>
.wrap {position: absolute;left: 0;bottom: 40px;width: 288px;height: 132px;margin-left: -144px;text-align: left;overflow: hidden;font-size: 12px;font-family: 'Malgun Gothic', dotum, '돋움', sans-serif;line-height: 1.5;}
.wrap * {padding: 0;margin: 0;}
.wrap .info {width: 286px;height: 120px;border-radius: 5px;border-bottom: 2px solid #ccc;border-right: 1px solid #ccc;overflow: hidden;background: #fff;}
.wrap .info:nth-child(1) {border: 0;box-shadow: 0px 1px 2px #888;}
.info .title {padding: 5px 0 0 10px;height: 30px;background: #eee;border-bottom: 1px solid #ddd;font-size: 18px;font-weight: bold;}
.info .close {position: absolute;top: 10px;right: 10px;color: #888;width: 17px;height: 17px;background: url('https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/overlay_close.png');}
.info .close:hover {cursor: pointer;}
.info .body {position: relative;overflow: hidden;}
.info .desc {position: relative;margin: 13px 0 0 90px;height: 75px;}
.desc .ellipsis {overflow: hidden;text-overflow: ellipsis;white-space: nowrap;}
.desc .jibun {font-size: 11px;color: #888;margin-top: -2px;}
.info .img {position: absolute;top: 6px;left: 5px;width: 73px;height: 71px;border: 1px solid #ddd;color: #888;overflow: hidden;}
.info:after {content: '';position: absolute;margin-left: -12px;left: 50%;bottom: 0;width: 22px;height: 12px;background: url('https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/vertex_white.png')}
.info .link {color: #5085BB;}

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
