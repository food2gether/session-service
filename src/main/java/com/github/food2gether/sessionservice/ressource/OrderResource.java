package com.github.food2gether.sessionservice.ressource;

import com.github.food2gether.sessionservice.model.Entry;
import com.github.food2gether.sessionservice.model.Order;
import com.github.food2gether.sessionservice.model.Session;
import com.github.food2gether.sessionservice.repository.OrderRepository;
import com.github.food2gether.sessionservice.repository.SessionRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Path("/api/v1/sessions/{sessionId}/orders")
public class OrderResource {

  @Inject
  OrderRepository orderRepository;

  @Inject
  SessionRepository sessionRepository;

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getOrderById(@PathParam("sessionId") Integer sessionId,
      @PathParam("id") Integer id) {
    Optional<Order> order = orderRepository.getOrderByIdAndSessionId(id, sessionId);

    if (order.isEmpty()) {
      return buildErrorResponse(Response.Status.NOT_FOUND, "order.not_found");
    }

    return Response.ok(Map.of("success", true, "data", order.get())).build();
  }

  private Response buildErrorResponse(Response.Status status, String messageKey) {
    return Response.status(status).entity(Map.of("success", false, "error",
        Map.of("code", status.getStatusCode(), "message_key", messageKey))).build();
  }

  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createOrUpdateOrder(@PathParam("sessionId") Integer sessionId,
      @QueryParam("id") Integer id,
      @QueryParam("profileId") Integer profileId,
      List<Entry> entries)
  {
    if (id == null) {
      return handleOrderCreation(sessionId, profileId, entries);
    } else {
      return handleOrderUpdate(sessionId, id, profileId, entries);
    }

  }

  private Response handleOrderCreation(Integer sessionId, Integer profileId, List<Entry> entries) {
    if (profileId == null || entries == null || entries.isEmpty()) {
      return buildErrorResponse(Response.Status.BAD_REQUEST, "request.invalid");
    }

    Optional<Session> session = sessionRepository.getSessionById(sessionId);
    if (session.isEmpty()) {
      return buildErrorResponse(Response.Status.NOT_FOUND, "session.not_found");
    }

    Order createdOrder = orderRepository.createOrder(new Order(profileId, session.get(), entries));
    return Response.status(Response.Status.CREATED)
        .entity(Map.of("success", true, "data", Map.of("id", createdOrder.getId()))).build();
  }

  private Response handleOrderUpdate(Integer sessionId, Integer id, Integer profileId, List<Entry> entries) {
    Optional<Order> order = orderRepository.getOrderByIdAndSessionId(id, sessionId);
    if (order.isEmpty()) {
      return buildErrorResponse(Response.Status.NOT_FOUND, "order.not_found");
    }

    Optional.ofNullable(profileId).ifPresent(order.get()::setProfileId);
    Optional.ofNullable(entries).ifPresent(order.get()::setEntries);

    return Response.ok(
        Map.of("success", true, "data", orderRepository.updateOrder(order.get()).getId())).build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getOrders(@PathParam("sessionId") Integer sessionId,
      @QueryParam("profileId") Integer profileId) {
    Optional<List<Order>> optionalOrders = orderRepository.getOrdersBySessionIdAndProfileId(
        sessionId, profileId);
    if (optionalOrders.isEmpty()) {
      return buildErrorResponse(Response.Status.NOT_FOUND, "orders.not_found");
    }
    return Response.ok(Map.of("success", true, "data", optionalOrders.get())).build();
  }

  @DELETE
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteOrderById(@PathParam("sessionId") Integer sessionId,
      @PathParam("id") Integer id) {
    Optional<Order> order = orderRepository.getOrderByIdAndSessionId(id, sessionId);
    if (order.isEmpty()) {
      return buildErrorResponse(Response.Status.NOT_FOUND, "order.not_found");
    }
    boolean deleted = orderRepository.deleteOrderById(order.get());
    return Response.ok(Map.of("success", deleted, "data", Map.of("id", id))).build();
  }
}

