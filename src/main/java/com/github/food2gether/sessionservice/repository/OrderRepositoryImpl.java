package com.github.food2gether.sessionservice.repository;

import com.github.food2gether.shared.model.Order;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class OrderRepositoryImpl implements OrderRepository {

  @Override
  public List<Order> findBySession(Long sessionId) {
    return this.list("session.id", sessionId);
  }

  @Override
  public List<Order> findBySessionAndProfile(Long sessionId, Long profileId) {
    return this.list("session.id = ?1 AND profile.id = ?2", sessionId, profileId);
  }
}