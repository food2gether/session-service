package com.github.food2gether.sessionservice.resource.v1;

import com.github.food2gether.model.Session;
import com.github.food2gether.response.APIResponse;
import com.github.food2gether.sessionservice.service.SessionService;
import jakarta.inject.Inject;
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
import java.util.List;

@Path("/api/v1/sessions")
public class SessionResource {

  @Inject
  SessionService service;

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createOrUpdateSession(Session.DTO body) {
    if (body == null) {
      return APIResponse.response(Response.Status.BAD_REQUEST, new Throwable("Request body is required"));
    }

    try {
      Session session = this.service.createOrUpdateSession(body);

      return APIResponse.response(body.getId() == null ? Response.Status.CREATED : Response.Status.OK, session);
    } catch (Exception e) {
      return APIResponse.response(Response.Status.BAD_REQUEST, e);
    }
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAllSessions(
      @QueryParam("restaurant_id") Long restaurantId,
      @QueryParam("orderable") @DefaultValue("false") boolean filterOrderable
  ) {
    try {
      List<Session> sessions = this.service.getAllSessions(restaurantId, filterOrderable);

      return APIResponse.response(Response.Status.OK, sessions.stream().map(Session.DTO::fromSession).toList());
    } catch (Exception e) {
      return APIResponse.response(Response.Status.BAD_REQUEST, e);
    }
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getSessionById(@PathParam("id") Long id) {
    try {
      return APIResponse.response(Response.Status.OK, Session.DTO.fromSession(this.service.getSessionById(id)));
    } catch (Exception e) {
      return APIResponse.response(Response.Status.BAD_REQUEST, e);
    }
  }

  @DELETE
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteSession(
      // This is provided by the auth proxy, therefore it is safe to use for user identification
      @HeaderParam("X-User-Email") String userEmail,
      @PathParam("id") Long id
  ) {
    try {
      Session session = this.service.deleteSession(userEmail, id);
      return APIResponse.response(Response.Status.OK, Session.DTO.fromSession(session));
    } catch (Exception e) {
      return APIResponse.response(Response.Status.BAD_REQUEST, e);
    }
  }

}
