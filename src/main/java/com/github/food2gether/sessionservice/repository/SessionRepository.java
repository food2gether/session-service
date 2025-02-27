package com.github.food2gether.sessionservice.repository;

import com.github.food2gether.model.Restaurant;
import com.github.food2gether.model.Session;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class SessionRepository implements PanacheRepository<Session> {

  public List<Session> findByRestaurant(Long restaurantId) {
    return this.list("restaurant.id", restaurantId);
  }

  public List<Session> findByRestaurant(Restaurant restaurant) {
    return this.list("restaurant", restaurant);
  }

  public List<Session> findByDueDate(LocalDateTime dueDate) {
    return this.list("deadline < ?1", dueDate);
  }

  public List<Session> findByRestaurantAndDueDate(Long restaurantId, LocalDateTime dueDate) {
    return this.list("restaurant.id = ?1 AND deadline < ?2", restaurantId, dueDate);
  }

  public List<Session> findByRestaurantAndDueDate(Restaurant restaurant, LocalDateTime dueDate) {
    return this.list("restaurant = ?1 AND deadline < ?2", restaurant, dueDate);
  }

}
