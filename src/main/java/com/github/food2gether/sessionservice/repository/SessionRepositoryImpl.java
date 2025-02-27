package com.github.food2gether.sessionservice.repository;

import com.github.food2gether.model.Restaurant;
import com.github.food2gether.model.Session;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class SessionRepositoryImpl implements SessionRepository {

  @Override
  public List<Session> findByRestaurant(Long restaurantId) {
    return this.list("restaurant.id", restaurantId);
  }

  @Override
  public List<Session> findByRestaurant(Restaurant restaurant) {
    return this.list("restaurant", restaurant);
  }

  @Override
  public List<Session> findByDueDate(LocalDateTime dueDate) {
    return this.list("deadline < ?1", dueDate);
  }

  @Override
  public List<Session> findByRestaurantAndDueDate(Long restaurantId, LocalDateTime dueDate) {
    return this.list("restaurant.id = ?1 AND deadline < ?2", restaurantId, dueDate);
  }

  @Override
  public List<Session> findByRestaurantAndDueDate(Restaurant restaurant, LocalDateTime dueDate) {
    return this.list("restaurant = ?1 AND deadline < ?2", restaurant, dueDate);
  }

}
