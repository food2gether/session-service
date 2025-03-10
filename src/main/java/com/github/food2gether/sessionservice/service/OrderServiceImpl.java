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
import java.util.List;

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
    return orderDto.getId() == null ? this.create(sessionId, orderDto) : this.update(orderDto);
  }

  @Transactional
  public Order create(Long sessionId, Order.DTO orderDto) {
    Session session = this.sessionRepository.findByIdOptional(sessionId)
        .orElseThrow(() -> new NotFoundException("Session with id " + sessionId + " does not exist"));

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
  public Order update(Order.DTO orderDto) {
    if (orderDto.getId() == null) {
      throw new WebApplicationException("Order id must not be null for updating a session", Status.BAD_REQUEST);
    }

    Order existingOrder = this.orderRepository.findByIdOptional(orderDto.getId()).
        orElse(new Order());

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
    return this.orderRepository.findByIdOptional(orderId)
        .orElseThrow(() -> new WebApplicationException(
            "Order with id " + orderId + " does not exist",
            Status.NOT_FOUND
        ));
  }

  @Override
  @Transactional
  public Order delete(Long sessionId, Long orderId, String userEmail) {
    Order order = this.orderRepository.findByIdOptional(orderId)
        .orElseThrow(() -> new NotFoundException("Order with id " + orderId + " does not exist"));

    if (!order.getProfile().getPrimaryEmail().equals(userEmail)) {
      throw new WebApplicationException("Only the user who owns the order can delete it", Status.FORBIDDEN);
    }

    order.getSession().getOrders().remove(order);
    return order;
  }
}