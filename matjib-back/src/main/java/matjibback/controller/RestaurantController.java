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

    //검색어로 맛집 찾기
    @GetMapping("/matjib/restaurants/search")
    public List<Restaurant> findMap(@RequestParam ("keyword") String keyword) {
        List<Restaurant> restaurants = restaurantRepository.findRestaurantByResNameContaining(keyword);
        return restaurants;
    }

    //현재 지도 중심위치 근방 맛집 보여주기
    @GetMapping("/matjib/restaurants")
    public List<Restaurant> showMap(
            @RequestParam ("latS") float latS, @RequestParam ("latE") float latE,
            @RequestParam ("longS") float longS, @RequestParam ("longE") float longE) {
        System.out.println(latS + "" + longE);
        List<Restaurant> restaurants = restaurantRepository.findRestaurantByLatitudeBetweenAndLongitudeBetweenOrderByAvgScoreDesc(latS, latE, longS, longE);
        System.out.println("검색결과 : "+ restaurants.size() +"건");
        if(restaurants.size()>20){
            return restaurants.subList(0, 20);
        }
        return restaurants;
    }
}
