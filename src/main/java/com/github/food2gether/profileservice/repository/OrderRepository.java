package com.github.food2gether.profileservice.repository;

import com.github.food2gether.profileservice.api.Order;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderRepository {

    private final Map<Integer, Order> orderStore = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    // Create or update an order
    public Order createOrUpdateOrder(Order order) {
        if (order.getId() == null) {
            order.setId(idGenerator.getAndIncrement());
        }
        orderStore.put(order.getId(), order);
        return order;
    }

    // Retrieve all orders, optionally filter by profileId
    public List<Order> getOrders(Integer profileId) {
        List<Order> orders = new ArrayList<>(orderStore.values());
        if (profileId != null) {
            orders.removeIf(order -> !profileId.equals(order.getProfileId()));
        }
        return orders;
    }

    // Retrieve a single order by ID
    public Optional<Order> getOrderById(Integer id) {
        return Optional.ofNullable(orderStore.get(id));
    }

    // Update an existing order
    public Order updateOrder(Order order) {
        if (!orderStore.containsKey(order.getId())) {
            throw new IllegalArgumentException("Order with ID " + order.getId() + " does not exist.");
        }

        orderStore.put(order.getId(), order);
        return order;
    }

    // Delete an order by ID
    public boolean deleteOrder(Integer id) {
        return orderStore.remove(id) != null;
    }
}