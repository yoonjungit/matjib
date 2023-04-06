<template>
  <div class="archive">
    <div class="header">
      <div style="font-size: 30px; font-weight: bold">Bookmark
        <span style="font-size: medium">&nbsp;&nbsp;&nbsp;저장된 맛집을 확인하세요!</span></div>
      <hr>
    </div>

    <div class="mainView" style="">
      <div v-if="state.bookmarks.length===0" style="padding: 30px">북마크 한 음식점이 없습니다.</div>
      <div>
        <div v-for="(bookmarks, idx) in state.bookmarks" :key="idx">
          <Bookmark :bookmarks="bookmarks"
                    @showOnMap="showOnMap"/>
        </div>
      </div>
      <div id = "map-frame">
      <div id="map" style=""></div><br>
        <i class="fa fa-map-marker" aria-hidden="true"></i> 지도에서 위치를 확인해보세요!
      </div>
    </div>
  </div>
</template>

<script>

import Bookmark from "@/components/Bookmark.vue";
import {reactive} from "vue";
import axios from "axios";

export default {
  name: 'Archive',
  components: {
    Bookmark
  },

  data() {
    return {
      markerPositions: [],
      markers: [],
    };
  },
  //1. 북마크 목록 받아오기
  setup() {
    const state = reactive({
      bookmarks: [],
    })
    const token = sessionStorage.getItem("token");
    axios.post("/matjib/bookmark/get", token).then(({data}) => {
      state.bookmarks = data;
    })
    return {state}
  },

  mounted() {
    if (window.kakao && window.kakao.maps) {
      this.initMap();
    } else {
      const script = document.createElement("script");
      console.log()
      const url="//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey="
          + process.env.VUE_APP_KAKAO_MAP_APP_KEY + "&libraries=services,clusterer";
      /* global kakao */
      script.onload = () => kakao.maps.load(this.initMap);
      script.src = url;
      document.head.appendChild(script);
    }
  },

  methods: {
    showOnMap(address) {
      // 주소로 좌표를 검색
      this.geocoder.addressSearch(address, (result, status) => {
        // 정상적으로 검색이 완료됐으면
        if (status === kakao.maps.services.Status.OK) {
          var coords = new kakao.maps.LatLng(result[0].y, result[0].x);
          this.displayMarker(coords)
        }
      });
    },
    initMap() {
      const container = document.getElementById("map");
      const options = {
        center: new kakao.maps.LatLng(37.55599730178367, 126.93688275718985),
        level: 5,
      };

      this.map = new kakao.maps.Map(container, options);
      this.geocoder = new kakao.maps.services.Geocoder();
    },

    displayMarker(coords) {
      if (this.markers.length > 0) {  //마커 배열에 원소가 하나라도 있으면
        this.markers.forEach((marker) => marker.setMap(null));
        this.markers = [];
      }

      const marker = new kakao.maps.Marker({
        map: this.map,
        position: coords
      });

      marker.setMap(this.map);
      this.markers.push(marker);

      this.map.setCenter(coords);
    },
  },

}
</script>

<style scoped>
* {
  color: #666666;
}

.archive {
  overflow-x: hidden;
  overflow-y: auto;
  height:100%;
  font-size: medium;
}

.header {
  height: 70px;
  width:100%;
  padding-top: 30px;
  padding-left: 30px;
  position: fixed;
  z-index: 1;
  background-color: white;
}

.mainView {
  display: flex;
  width: 100%;
  margin-top:100px;
  position:relative;
  z-index:0;
}
#map-frame {
  position: fixed;
  margin-left: 460px;
  align-items: center;
  text-align: left;
}
#map {
  width: 400px;
  height: 400px;
}

button {
  margin: 0 3px;
}
</style>
