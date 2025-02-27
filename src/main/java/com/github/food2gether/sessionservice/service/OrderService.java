package com.github.food2gether.sessionservice.service;


import com.github.food2gether.model.MenuItem;
import com.github.food2gether.model.Order;
import com.github.food2gether.model.OrderItem;
import com.github.food2gether.model.Profile;
import com.github.food2gether.model.Session;
import com.github.food2gether.sessionservice.repository.OrderRepository;
import com.github.food2gether.sessionservice.repository.SessionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;
import java.util.List;

@ApplicationScoped
public class OrderService {

  @Inject
  EntityManager entityManager;

  @Inject
  SessionRepository sessionRepository;

  @Inject
  OrderRepository orderRepository;

  public Order createOrUpdate(Long sessionId, Order.DTO orderDto) {
    return orderDto.getId() == null ? this.create(sessionId, orderDto) : this.update(orderDto);
  }

  @Transactional
  public Order create(Long sessionId, Order.DTO orderDto) {
    Session session = this.sessionRepository.findByIdOptional(sessionId)
        .orElseThrow(() -> new WebApplicationException("Session with id " + sessionId + " does not exist", Status.NOT_FOUND));

    if (orderDto.getId() != null) {
      throw new WebApplicationException("Order id must be null for creating a new order", Status.BAD_REQUEST);
    }

    // Check if all required fields are present for creating a new order
    if (orderDto.getProfileId() == null) {
      throw new WebApplicationException("Missing required fields for creating a new order", Status.BAD_REQUEST);
    }

    Order order = new Order();
    order.setItems(this.toOrderItems(orderDto.getItems()));
    order.setState(orderDto.getState() == null ? Order.State.OPEN : orderDto.getState());
    order.setProfile(this.entityManager.getReference(Profile.class, orderDto.getProfileId()));

    session.getOrders().add(order);

    this.orderRepository.persist(order);
    this.sessionRepository.persist(session);

    return order;
  }

  @Transactional
  public Order update(Order.DTO orderDto) {
    if (orderDto.getId() == null) {
      throw new WebApplicationException("Order id must not be null for updating a session", Status.BAD_REQUEST);
    }

    Order existingOrder = this.orderRepository.findByIdOptional(orderDto.getId()).
        orElseThrow(() -> new WebApplicationException(
            "Order with id " + orderDto.getId() + " does not exist",
            Status.NOT_FOUND
        ));

    if (orderDto.getProfileId() != null)
      existingOrder.setProfile(this.entityManager.getReference(Profile.class, orderDto.getProfileId()));

    if (orderDto.getState() != null)
      existingOrder.setState(orderDto.getState());

    if (orderDto.getItems() != null)
      existingOrder.setItems(this.toOrderItems(orderDto.getItems()));

    this.orderRepository.persist(existingOrder);
    return existingOrder;
  }

  private List<OrderItem> toOrderItems(List<OrderItem.DTO> orderItemDtos) {
    return orderItemDtos == null
        ? List.of()
        : orderItemDtos
            .stream()
            .map(dto -> new OrderItem(
                dto.getId(),
                this.entityManager.getReference(MenuItem.class, dto.getMenuItemId()),
                dto.getQuantity()
            ))
            .toList();
  }

  public List<Order> getAll(Long sessionId) {
    return this.sessionRepository.findByIdOptional(sessionId)
        .orElseThrow(() -> new WebApplicationException(
            "Session with id " + sessionId + " does not exist",
            Status.NOT_FOUND
        ))
        .getOrders();
  }

  public Order getById(Long sessionId, Long orderId) {
    return this.orderRepository.findByIdOptional(orderId)
        .orElseThrow(() -> new WebApplicationException(
            "Order with id " + orderId + " does not exist",
            Status.NOT_FOUND
        ));
  }

  public Order delete(Long sessionId, Long orderId, String userEmail) {
    Order order = this.getById(sessionId, orderId);

    if (!order.getProfile().getPrimaryEmail().equals(userEmail)) {
      throw new WebApplicationException("Only the user who owns the order can delete it", Status.FORBIDDEN);
    }

    this.orderRepository.delete(order);
    return order;
  }
}
