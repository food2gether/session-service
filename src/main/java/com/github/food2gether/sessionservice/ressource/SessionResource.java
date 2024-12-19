package com.github.food2gether.sessionservice.ressource;

import com.github.food2gether.sessionservice.model.Session;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Path("/api/v1/sessions")
public class SessionResource {

  @Inject
  SessionRepository sessionRepository;

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getSessionById(@PathParam("id") Integer id) {
    Optional<Session> session = sessionRepository.getSessionById(id);
    if (session.isEmpty()) {
      return buildErrorResponse(Response.Status.NOT_FOUND, "session.not_found");
    }

    return Response.ok(Map.of("success", true, "data", session.get())).build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAllSessions(@QueryParam("restaurant_id") Integer restaurantId,
      @QueryParam("orderable") Boolean orderable) {
    Optional<List<Session>> optionalSessions = sessionRepository.getSessions(restaurantId,
        orderable);

    if (optionalSessions.isEmpty()) {
      return buildErrorResponse(Response.Status.NOT_FOUND, "session.not_found");
    }

    return Response.ok(Map.of("success", true, "data", optionalSessions.get())).build();
  }


  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createOrUpdateSession(@QueryParam("id") Integer id,
      @QueryParam("restaurant_id") Integer restaurantId,
      @QueryParam("organizer_id") Integer organizerId,
      @QueryParam("deadline") LocalDateTime deadline) {
    if (id == null) {
      // Handle session creation
      return handleSessionCreation(restaurantId, organizerId, deadline);
    } else {
      // Handle session update
      return handleSessionUpdate(id, restaurantId, organizerId, deadline);
    }
  }

  private Response handleSessionCreation(Integer restaurantId, Integer organizerId,
      LocalDateTime deadline) {
    if (restaurantId == null || organizerId == null || deadline == null) {
      return buildErrorResponse(Response.Status.BAD_REQUEST, "request.invalid");
    }

    Session createdSession = sessionRepository.createSession(
        new Session(restaurantId, organizerId, deadline));
    return Response.status(Response.Status.CREATED)
        .entity(Map.of("success", true, "data", Map.of("id", createdSession.getId()))).build();
  }

  private Response handleSessionUpdate(Integer id, Integer restaurantId, Integer organizerId,
      LocalDateTime deadline) {
    Optional<Session> existingSession = sessionRepository.getSessionById(id);
    if (existingSession.isEmpty()) {
      return buildErrorResponse(Response.Status.NOT_FOUND, "session.not_found");
    }

    Session updatedSession = existingSession.get();
    Optional.ofNullable(restaurantId).ifPresent(updatedSession::setRestaurantId);
    Optional.ofNullable(organizerId).ifPresent(updatedSession::setOrganizerId);
    Optional.ofNullable(deadline).ifPresent(updatedSession::setDeadline);

    updatedSession = sessionRepository.updateSession(updatedSession);
    return Response.ok(Map.of("success", true, "data", Map.of("id", updatedSession.getId())))
        .build();
  }


  @DELETE
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteSessionById(@PathParam("id") Integer id) {
    boolean deleted = sessionRepository.deleteSessionById(id);
    if (!deleted) {
      return buildErrorResponse(Response.Status.NOT_FOUND, "session.not_found");
    }

    return Response.ok(Map.of("success", true)).build();
  }

  private Response buildErrorResponse(Response.Status status, String messageKey) {
    return Response.status(status).entity(Map.of("success", false, "error",
        Map.of("code", status.getStatusCode(), "message_key", messageKey))).build();
  }
}
