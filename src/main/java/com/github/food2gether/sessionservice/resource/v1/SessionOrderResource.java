package com.github.food2gether.sessionservice.resource.v1;

import com.github.food2gether.shared.model.Order;
import com.github.food2gether.shared.response.APIResponse;
import com.github.food2gether.sessionservice.service.OrderService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
@Path("/api/v1/sessions/{session_id}/orders")
public class SessionOrderResource {

  @Inject
  OrderService service;

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createOrUpdateOrder(@PathParam("session_id") Long sessionId, Order.DTO body) {
    if (body == null) {
      throw new WebApplicationException("Missing request body", Response.Status.BAD_REQUEST);
    }

    Order session = this.service.createOrUpdate(sessionId, body);

    return APIResponse.response(
        body.getId() == null
            ? Response.Status.CREATED
            : Response.Status.OK,
        Order.DTO.fromOrder(session)
    );
  }

  // TODO
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAllOrders(
      @PathParam("session_id") Long sessionId
  ) {
    List<Order> sessions = this.service.getAll(sessionId);

    return APIResponse.response(
        Response.Status.OK,
        sessions.stream()
            .map(Order.DTO::fromOrder)
            .toList()
    );
  }

  @GET
  @Path("/{order_id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getOrderById(
      @PathParam("session_id") Long sessionId,
      @PathParam("order_id") Long orderId
  ) {
    Order order = this.service.getById(sessionId, orderId);
    return APIResponse.response(
        Response.Status.OK,
        Order.DTO.fromOrder(order)
    );
  }

  @DELETE
  @Path("/{order_id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteOrder(
      // This is provided by the auth proxy, therefore it is safe to use for user identification
      @HeaderParam("X-User-Email") String userEmail,
      @PathParam("session_id") Long sessionId,
      @PathParam("order_id") Long orderId
  ) {
    return APIResponse.response(
        Response.Status.OK,
        this.service.delete(sessionId, orderId, userEmail)
    );
  }

}
