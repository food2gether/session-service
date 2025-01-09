package com.github.food2gether.sessionservice.repository;

import com.github.food2gether.sessionservice.model.Order;
import com.github.food2gether.sessionservice.model.Order$;
import com.github.food2gether.sessionservice.model.Session;
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
    var stream = jpaStreamer.stream(Order.class);
    stream = stream.filter(Order$.id.equal(id));
    stream = stream.filter(order -> order.getSession().getId().equals(sessionId));
    return stream.findFirst();
  }

  public Optional<List<Order>> getOrdersBySessionIdAndProfileId(Session session, Integer profileId) {
    List<Order> orders = session.getOrders();   //Ineffizienter als Database query aber einfacher

    if (profileId != null) {
      orders = orders.stream()
          .filter(order -> order.getProfileId().equals(profileId))
          .toList();
    }

    return orders.isEmpty() ? Optional.empty() : Optional.of(orders);
  }


  @Transactional
  public Order createOrder(Order order) {
    entityManager.persist(entityManager.contains(order) ? order : entityManager.merge(order));
    return order;
  }

  @Transactional
  public Order updateOrder(Order order) {
    return entityManager.merge(order);    //works as long as order exists, else 500
  }

  @Transactional
  public boolean deleteOrderById(Order order) {
    Order managedOrder = entityManager.find(Order.class, order.getId());
    if (managedOrder != null) {
      entityManager.remove(managedOrder);
      return true;
    }
    return false;
  }
}


