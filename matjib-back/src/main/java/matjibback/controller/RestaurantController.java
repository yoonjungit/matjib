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

    @GetMapping("/matjib/getMap")
    public List<Restaurant> getMap() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        return restaurants;
    }

    @PostMapping("/matjib/find")
    public List<Restaurant> findMap(@RequestBody Map<String, String> params) {
        System.out.println(params.get("resName"));
        List<Restaurant> restaurants = restaurantRepository.findRestaurantByResNameContaining(params.get("resName"));
        System.out.println(restaurants.size());
        return restaurants;
    }
}
