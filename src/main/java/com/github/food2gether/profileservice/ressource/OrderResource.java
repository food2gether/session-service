package com.github.food2gether.profileservice.ressource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import com.github.food2gether.profileservice.api.Order;
import com.github.food2gether.profileservice.repository.OrderRepository;

import java.util.*;

@Path("/api/v1/sessions/{sessionId}/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    private final OrderRepository orderRepository = new OrderRepository();

    @PUT
    public Response createOrUpdateOrder(
            @PathParam("sessionId") String sessionId,
            @QueryParam("order_id") Integer orderId,  // Optional query parameter
            @QueryParam("profile_id") Integer profileId, // Optional query parameter
            @QueryParam("entries") List<String> entries
    ) {
        try {
            // Save the createdOrder using the repository
            Order createdOrder = orderRepository.createOrUpdateOrder(new Order(orderId, profileId, entries));

            // Determine status code based on whether it's a create or update
            int status = (orderId == null) ? Response.Status.CREATED.getStatusCode() : Response.Status.OK.getStatusCode();
            return Response.status(status)
                    .entity(Map.of("success", true, "data", Map.of("id", createdOrder.getId())))
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("success", false, "error", Map.of("code", 400, "message_key", "request.invalid")))
                    .build();
        }
    }


    // GET: Retrieve all orders with optional filters
    @GET
    public Response getOrders(
            @PathParam("sessionId") String sessionId,
            @QueryParam("profile_id") Integer profileId) {
        List<Order> orders = orderRepository.getOrders(profileId);
        return Response.ok(Map.of("success", true, "data", orders)).build();
    }

    // GET: Retrieve a single order by ID
    @GET
    @Path("/{id}")
    public Response getOrderById(
            @PathParam("sessionId") String sessionId,
            @PathParam("id") Integer id) {
        return orderRepository.getOrderById(id)
                .map(order -> Response.ok(Map.of("success", true, "data", order)).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("success", false, "error", Map.of("code", 404, "message_key", "order.not_found"))).build());
    }

    // DELETE: Remove an order by ID
    @DELETE
    @Path("/{id}")
    public Response deleteOrder(
            @PathParam("sessionId") String sessionId,
            @PathParam("id") Integer id) {
        if (orderRepository.deleteOrder(id)) {
            return Response.ok(Map.of("success", true, "data", Map.of("id", id))).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("success", false, "error", Map.of("code", 404, "message_key", "order.not_found"))).build();
        }
    }
}
