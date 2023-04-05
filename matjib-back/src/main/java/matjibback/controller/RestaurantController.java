package matjibback.controller;

import matjibback.entity.Restaurant;
import matjibback.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class RestaurantController {

    @Autowired
    RestaurantRepository restaurantRepository;

    //전체 맛집 리스트 받아오기
    @GetMapping("/matjib/restaurants/getMap")
    public List<Restaurant> getMap() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        return restaurants;
    }

    //검색어로 맛집 찾기
    @PostMapping("/matjib/restaurants/search")
    public List<Restaurant> findMap(@RequestBody Map<String, String> params) {
        System.out.println("검색어 : " + params.get("resName"));
        List<Restaurant> restaurants = restaurantRepository.findRestaurantByResNameContaining(params.get("resName"));
        System.out.println("검색결과 : " + restaurants.size() + "건");
        return restaurants;
    }

    //현재 지도 중심위치 근방 맛집 보여주기
    @PostMapping("/matjib/restaurants/show")
    public List<Restaurant> showMap(@RequestBody Map<String, String> params) {
        float latS= Float.parseFloat(String.valueOf(params.get("latS")));
        float latE= Float.parseFloat(String.valueOf(params.get("latE")));
        float longS= Float.parseFloat(String.valueOf(params.get("longS")));
        float longE= Float.parseFloat(String.valueOf(params.get("longE")));
        List<Restaurant> restaurants = restaurantRepository.findRestaurantByLatitudeBetweenAndLongitudeBetweenOrderByAvgScoreDesc(latS, latE, longS, longE);
        System.out.println("검색결과 : "+ restaurants.size() +"건");
        if(restaurants.size()>20){
            return restaurants.subList(0, 20);
        }
        return restaurants;
    }
}
