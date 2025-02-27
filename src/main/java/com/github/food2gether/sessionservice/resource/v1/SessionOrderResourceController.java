package com.github.food2gether.sessionservice.resource.v1;

import com.github.food2gether.model.Order;
import com.github.food2gether.model.Order.State;
import com.github.food2gether.model.OrderItem;
import com.github.food2gether.response.APIResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Path("/api/v1/sessions/{session_id}/orders")
public class SessionOrderResourceController {

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createOrUpdateOrder(Order.DTO body) {
    if (body != null && body.getId() == null)
      body.setId(ThreadLocalRandom.current().nextLong());

    return APIResponse.response(Response.Status.CREATED, body);
  }

  // TODO
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAllOrders(
      @PathParam("session_id") Long sessionId
  ) {
    return APIResponse.response(Response.Status.OK, List.of(new Order.DTO(
        1L,
        List.of(new OrderItem.DTO(1L, 1L, 1)),
        1L,
        State.SUBMITTED
    )));
  }

  @GET
  @Path("/{order_id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getOrderById(
      @PathParam("session_id") Long sessionId,
      @PathParam("order_id") Long orderId
  ) {
    return APIResponse.response(Response.Status.OK, new Order.DTO(
        orderId,
        List.of(new OrderItem.DTO(1L, 1L, 1)),
        1L,
        State.SUBMITTED
    ));
  }

  @DELETE
  @Path("/{order_id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteOrder(
      @PathParam("session_id") Long sessionId,
      @PathParam("order_id") Long orderId
  ) {
    return APIResponse.response(Response.Status.OK, new Order.DTO(
        orderId,
        List.of(new OrderItem.DTO(1L, 1L, 1)),
        1L,
        State.REJECTED
    ));
  }

}
