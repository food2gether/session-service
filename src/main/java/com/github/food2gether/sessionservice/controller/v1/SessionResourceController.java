package com.github.food2gether.sessionservice.controller.v1;

import com.github.food2gether.model.Session;
import com.github.food2gether.response.APIResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Path("/api/v1/sessions")
public class SessionResourceController {

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createOrUpdateSession(Session body) {
    if (body != null && body.getId() == null)
      body.setId(ThreadLocalRandom.current().nextLong());

    return APIResponse.response(Response.Status.CREATED, body);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAllSessions(
      @QueryParam("restaurant_id") Long restaurantId,
      @QueryParam("orderable") @DefaultValue("false") boolean orderable
  ) {
    return APIResponse.response(Response.Status.OK, List.of(new Session.DTO(
        1L,
        restaurantId,
        1L,
        LocalDateTime.now()
    )));
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getSessionById(@PathParam("id") Long id) {
    return APIResponse.response(Response.Status.OK, new Session.DTO(
        id,
        1L,
        1L,
        LocalDateTime.now()
    ));
  }

  @DELETE
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteSession(
      // This is provided by the auth proxy, therefore it is safe to use for user identification
      @HeaderParam("X-User-Email") String userEmail,
      @PathParam("id") Long id
  ) {
    return APIResponse.response(Response.Status.OK, new Session.DTO(
        id,
        1L,
        1L,
        LocalDateTime.now()
    ));
  }

}
