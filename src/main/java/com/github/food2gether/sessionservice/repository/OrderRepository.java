package com.github.food2gether.sessionservice.repository;

import com.github.food2gether.shared.model.Order;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import java.util.List;

public interface OrderRepository extends PanacheRepository<Order> {

    List<Order> findBySession(Long sessionId);

  List<Order> findBySessionAndProfile(Long sessionId, Long profileId);
}
