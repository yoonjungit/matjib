<template>
  <div>
    <div class="search">
      <input type="text" name="searchIndex" onclick="this.value ='';" value="식당명을 입력하세요.">
      <button type="submit">검색</button>
    </div>
    <div id="map"></div>
    <div class="button-group">
      <button @click="changeSize(0)">Hide</button>
      <button @click="changeSize(400)">show</button>
      <button @click="displayMarker(markerPositions1)">marker set 1</button>
      <button @click="displayMarker(markerPositions2)">marker set 2</button>
      <button @click="displayMarker([])">marker set 3 (empty)</button>
      <button @click="displayInfoWindow">infowindow</button>
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
      geocoder: null
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
        content: '<div style="padding:13px;"><div class="name">식당이름</div><div class="buttons"><button>리스트</button><button>북마크</button></div><div class="body"><div class="address">주소</div><div class="naver">네이버 별점/갯수</div><div class="kakao">카카오별점/갯수</div><div class="google">구글별점/갯수</div></div></div></div>' // 인포윈도우에 표시할 내용
      });

       //인포윈도우를 지도에 표시한다
       this.infowindow.open(this.map, this.marker);

      // 마커에 클릭 이벤트를 등록한다 (우클릭 : rightclick)
      kakao.maps.event.addListener(this.marker, 'click', function () {
        alert('마커를 클릭했습니다!');
        this.infowindow.setMap(this.map);
      })
    },
    changeSize(size) {
      const container = document.getElementById("map");
      container.style.width = `${size}px`;
      container.style.height = `${size}px`;
      this.map.relayout();
    },
    displayMarker(markerPositions) {
      if (this.markers.length > 0) {
        this.markers.forEach((marker) => marker.setMap(null));
      }

      const positions = markerPositions.map(
          (position) => new kakao.maps.LatLng(...position)
      );

      if (positions.length > 0) {
        this.markers = positions.map(
            (position) =>
                new kakao.maps.Marker({
                  map: this.map,
                  position,
                })
        );

        const bounds = positions.reduce(
            (bounds, latlng) => bounds.extend(latlng),
            new kakao.maps.LatLngBounds()
        );

        this.map.setBounds(bounds);
      }
    },
    displayInfoWindow() {
      if (this.infowindow && this.infowindow.getMap()) {
        //이미 생성한 인포윈도우가 있기 때문에 지도 중심좌표를 인포윈도우 좌표로 이동시킨다.
        this.map.setCenter(this.infowindow.getPosition());
        return;
      }

      var iwContent = '<div style="padding:5px;">Hello World!</div>', // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
          iwPosition = new kakao.maps.LatLng(33.450701, 126.570667), //인포윈도우 표시 위치입니다
          iwRemoveable = true; // removeable 속성을 ture 로 설정하면 인포윈도우를 닫을 수 있는 x버튼이 표시됩니다

      this.infowindow = new kakao.maps.InfoWindow({
        map: this.map, // 인포윈도우가 표시될 지도
        position: iwPosition,
        content: iwContent,
        removable: iwRemoveable,
      });

      this.map.setCenter(iwPosition);
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
#map {
  width: 1500px;
  height: 800px;
}

.button-group {
  margin: 10px 0;
}

button {
  margin: 0 3px;
}
</style>
