package matjibback.repository;

import matjibback.entity.Member;
import matjibback.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, String> {
    List<Restaurant> findRestaurantByIdIn(List<Integer> resIds);
    List<Restaurant> findRestaurantByResNameContaining(String searchRes);
    List<Restaurant> findRestaurantByLatitudeBetweenAndLongitudeBetweenOrderByAvgScoreDesc(float latS, float latE, float longS, float longE);
}
