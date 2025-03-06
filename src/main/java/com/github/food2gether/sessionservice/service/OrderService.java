package com.github.food2gether.sessionservice.service;

import com.github.food2gether.shared.model.Order;
import java.util.List;

public interface OrderService {

  Order createOrUpdate(Long sessionId, Order.DTO orderDto);

  List<Order> getAll(Long sessionId, Long profileId);

  Order getById(Long sessionId, Long orderId);

  Order delete(Long sessionId, Long orderId, String userEmail);
}
