package matjibback.controller;

import matjibback.entity.Restaurant;
import matjibback.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestaurantController {

    @Autowired
    RestaurantRepository restaurantRepository;

    @GetMapping("/matjib/getMap")
    public List<Restaurant> getMap() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        return restaurants;
    }
}
