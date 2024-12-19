package com.github.food2gether.sessionservice.repository;

import com.github.food2gether.sessionservice.model.Order;
import com.github.food2gether.sessionservice.model.Order$;
import com.github.food2gether.sessionservice.model.Session;
import com.github.food2gether.sessionservice.model.Session$;
import com.speedment.jpastreamer.application.JPAStreamer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class OrderRepository {

  @Inject
  JPAStreamer jpaStreamer;

  @PersistenceContext
  EntityManager entityManager;

  public Optional<Order> getOrderByIdAndSessionId(Integer id, Integer sessionId) {
    return jpaStreamer.stream(Session.class)
        .filter(Session$.id.equal(sessionId))
        .flatMap(session -> session.getOrders().stream())
        .filter(order -> order.getId().equals(id))
        .findFirst();
  }

  @Transactional
  public Order createOrder(Order order) {
    entityManager.persist(entityManager.contains(order) ? order : entityManager.merge(order));
    return order;
  }

  @Transactional
  public Order updateOrder(Order order) {
    return entityManager.merge(order);
  }

  public Optional<List<Order>> getOrdersBySessionIdAndProfileId(Integer sessionId, Integer profileId) {
    var stream = jpaStreamer.stream(Session.class)
        .filter(Session$.id.equal(sessionId))
        .flatMap(session -> session.getOrders().stream());

    if (profileId != null) {
      stream = stream.filter(Order$.profileId.equal(profileId));
    }

    List<Order> orders = stream.toList();
    return orders.isEmpty() ? Optional.empty() : Optional.of(orders);
  }

  @Transactional
  public boolean deleteOrderById(Order order) {
    entityManager.remove(entityManager.contains(order) ? order : entityManager.merge(order));
    return true;
  }
}


