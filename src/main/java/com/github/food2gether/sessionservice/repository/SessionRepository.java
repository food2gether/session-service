package com.github.food2gether.sessionservice.repository;

import com.github.food2gether.model.Restaurant;
import com.github.food2gether.model.Session;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository extends PanacheRepository<Session> {

  List<Session> findByRestaurant(Long restaurantId);

  List<Session> findByRestaurant(Restaurant restaurant);

  List<Session> findByDueDate(LocalDateTime dueDate);

  List<Session> findByRestaurantAndDueDate(Long restaurantId, LocalDateTime dueDate);

  List<Session> findByRestaurantAndDueDate(Restaurant restaurant, LocalDateTime dueDate);
}
