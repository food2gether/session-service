package com.github.food2gether.sessionservice.service;


import com.github.food2gether.sessionservice.repository.AnonymousRepository;
import com.github.food2gether.shared.model.MenuItem;
import com.github.food2gether.shared.model.Order;
import com.github.food2gether.shared.model.OrderItem;
import com.github.food2gether.shared.model.Profile;
import com.github.food2gether.shared.model.Session;
import com.github.food2gether.sessionservice.repository.OrderRepository;
import com.github.food2gether.sessionservice.repository.SessionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class OrderServiceImpl implements OrderService {

  @Inject
  SessionRepository sessionRepository;

  @Inject
  OrderRepository orderRepository;

  @Inject
  AnonymousRepository anonymousRepository;

  @Override
  public Order createOrUpdate(Long sessionId, Order.DTO orderDto) {
    return orderDto.getId() == null ? this.create(sessionId, orderDto) : this.update(sessionId, orderDto);
  }

  @Transactional
  public Order create(Long sessionId, Order.DTO orderDto) {
    Session session = this.sessionRepository.findByIdOptional(sessionId)
        .orElseThrow(() -> new NotFoundException("Session with id " + sessionId + " does not exist"));

    if (session.getDeadline().isBefore(LocalDateTime.now())) {
      throw new WebApplicationException("Session is already closed", Status.BAD_REQUEST);
    }

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

    order.setProfile(this.anonymousRepository.findOptional(Profile.class, orderDto.getProfileId())
        .orElseThrow(() -> new NotFoundException("Profile not found")));

    order.setSession(session);

    session.getOrders().add(order);

    this.orderRepository.persist(order);
    this.sessionRepository.persist(session);

    return order;
  }

  @Transactional
  public Order update(Long sessionId, Order.DTO orderDto) {
    if (orderDto.getId() == null) {
      throw new WebApplicationException("Order id must not be null for updating a session", Status.BAD_REQUEST);
    }

    Order existingOrder = this.orderRepository.findByIdOptional(orderDto.getId())
        .orElseThrow(() -> new WebApplicationException(
            "Order with id " + orderDto.getId() + " does not exist",
            Status.NOT_FOUND
        ));

    Session session = existingOrder.getSession();

    if (!Objects.equals(session.getId(), sessionId)) {
      throw new WebApplicationException("Order does not belong to the session", Status.BAD_REQUEST);
    }

    if (session.getDeadline().isBefore(LocalDateTime.now())) {
      throw new WebApplicationException("Session is already closed", Status.BAD_REQUEST);
    }

    if (orderDto.getProfileId() != null)
      existingOrder.setProfile(this.anonymousRepository.findOptional(Profile.class, orderDto.getProfileId())
          .orElseThrow(() -> new NotFoundException("Profile not found")));

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
                this.anonymousRepository.findOptional(MenuItem.class, dto.getMenuItemId())
                    .orElseThrow(() -> new NotFoundException("MenuItem %d not found".formatted(dto.getMenuItemId()))),
                dto.getQuantity()
            ))
            .toList();
  }

  @Override
  public List<Order> getAll(Long sessionId, Long profileId) {
    if (this.sessionRepository.findByIdOptional(sessionId).isEmpty())
      throw new NotFoundException("Session with id %d not found".formatted(sessionId));
    return profileId == null
        ? this.orderRepository.findBySession(sessionId)
        : this.orderRepository.findBySessionAndProfile(sessionId, profileId);
  }

  @Override
  public Order getById(Long sessionId, Long orderId) {
    Order order = this.orderRepository.findByIdOptional(orderId)
        .orElseThrow(() -> new WebApplicationException(
            "Order with id " + orderId + " does not exist",
            Status.NOT_FOUND
        ));

    if (!Objects.equals(order.getSession().getId(), sessionId)) {
      throw new WebApplicationException("Order does not belong to the session", Status.BAD_REQUEST);
    }

    return order;
  }

  @Override
  @Transactional
  public Order delete(Long sessionId, Long orderId, String userEmail) {
    Order order = this.orderRepository.findByIdOptional(orderId)
        .orElseThrow(() -> new NotFoundException("Order with id " + orderId + " does not exist"));

    Session session = order.getSession();
    if (!Objects.equals(session.getId(), sessionId)) {
      throw new WebApplicationException("Order does not belong to the session", Status.BAD_REQUEST);
    }

    if (session.getDeadline().isBefore(LocalDateTime.now())) {
      throw new WebApplicationException("Session is already closed", Status.BAD_REQUEST);
    }

    if (!order.getProfile().getPrimaryEmail().equals(userEmail)) {
      throw new WebApplicationException("Only the user who owns the order can delete it", Status.FORBIDDEN);
    }

    order.getSession().getOrders().remove(order);
    return order;
  }
}