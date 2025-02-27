package com.github.food2gether.sessionservice.service;

import com.github.food2gether.model.Order;
import com.github.food2gether.model.OrderItem;
import java.util.List;

public interface OrderService {

  Order createOrUpdate(Long sessionId, Order.DTO orderDto);

  Order create(Long sessionId, Order.DTO orderDto);

  Order update(Order.DTO orderDto);

  List<OrderItem> toOrderItems(List<OrderItem.DTO> orderItemDtos);

  List<Order> getAll(Long sessionId);

  Order getById(Long sessionId, Long orderId);

  Order delete(Long sessionId, Long orderId, String userEmail);
}
