<template>
  <div>
    <div class="search">
      <input type="text" name="searchIndex" onclick="this.value ='';" value="식당명을 입력하세요.">
      <button type="submit">검색</button>
    </div>
    <div id="map" style="width:750px;height:350px;"></div>
  </div>
</template>

<script>

import {map} from "core-js/internals/array-iteration";

export default {
  name: "KakaoMap",
  data() {
    return {
      map: null,
      marker: null,
      infowindow: null,
      zoomControl : null
    };
  },
  mounted() {
    if (!window.kakao || !window.kakao.maps) {
      const script = document.createElement('script')
      script.type = 'text/javascript'
      script.src =
          '//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${process.env.VUE_APP_KAKAO_MAP_API_KEY}&libraries=clusterer,drawing,services'
      /* eslint를 사용한다면 kakao 변수가 선언되지 않았다고
       * 오류를 내기 때문에 아래 줄과 같이 전역변수임을
       * 알려주어야 한다. */
      /* global kakao */
      script.addEventListener('load', () => {
        kakao.maps.load(() => {
          // 카카오맵 API가 로딩이 완료된 후 지도의 기본적인 세팅을 시작해야 한다.
          this.initMap()
        })
      })
      document.head.appendChild(script)
    } else {
      // 이미 카카오맵 API가 로딩되어 있다면 바로 지도를 생성한다.
      this.initMap()
    }
  },
  methods: {
    initMap() {
      const mapContainer = document.getElementById('map')

      const mapOption = {
        center: new kakao.maps.LatLng(38.04183, 126.92657), // 지도의 중심좌표
        level: 4, // 지도의 확대 레벨
        mapTypeId : kakao.maps.MapTypeId.ROADMAP // 지도종류
      }

      // 지도를 생성한다
      this.map = new kakao.maps.Map(mapContainer, mapOption)

      // 지도에 확대 축소 컨트롤을 생성한다
      this.zoomControl = new kakao.maps.ZoomControl();

      // 지도의 우측에 확대 축소 컨트롤을 추가한다
      map.addControl(this.zoomControl, kakao.maps.ControlPosition.RIGHT);

      // 지도에 마커를 생성하고 표시한다
      this.marker = new kakao.maps.Marker({
        position: new kakao.maps.LatLng(38.04007, 126.92658), // 마커의 좌표
        map: map // 마커를 표시할 지도 객체
      });

      // 마커 위에 표시할 인포윈도우를 생성한다
      this.infowindow = new kakao.maps.InfoWindow({
        content : '<div class="name">식당이름<button>리스트</button><button>북마크</button> </div><div class="body">영역II<div class="etc"></div><div class="address">주소</div><div class="naver">네이버 별점/갯수</div><div class="kakao">카카오별점/갯수</div><div class="google">구글별점/갯수</div></div></div>' // 인포윈도우에 표시할 내용
      });

      // 인포윈도우를 지도에 표시한다
      this.infowindow.open(map, this.marker);

      // 마커에 클릭 이벤트를 등록한다 (우클릭 : rightclick)
      kakao.maps.event.addListener(this.marker, 'click', function() {
        alert('마커를 클릭했습니다!');
      });
    }
  }
};
</script>

<style scoped>
#map {
  width: 400px;
  height: 400px;
}
</style>
