package com.github.food2gether.profileservice.ressource;


import com.github.food2gether.profileservice.repository.SessionRepository;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import com.github.food2gether.profileservice.api.Session;

import java.util.*;


@Path("/api/v1/sessions")
public class SessionResource {

    // Example service to interact with database (to be implemented)
    private SessionRepository sessionRepository = new SessionRepository();

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createOrUpdateSession(@QueryParam("id") Integer id,
                                          @QueryParam("restaurant_id") Integer restaurantId,
                                          @QueryParam("organizer_id") Integer organizerId,
                                          @QueryParam("deadline") Date deadline) {
        if (id == null) {
            // Create new session
            if (restaurantId == null || organizerId == null || deadline == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("success", false, "error", Map.of("code", 400, "message_key", "request.invalid")))
                        .build();
            }

            Session createdSession = sessionRepository.createSession(new Session(id, restaurantId, organizerId, deadline));
            return Response.status(Response.Status.CREATED)
                    .entity(Map.of("success", true, "data", Map.of("id", createdSession.getId())))
                    .build();
        } else {
            // Update existing session
            Optional<Session> existingSession = sessionRepository.getSessionById(id);
            if (existingSession.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("success", false, "error", Map.of("code", 404, "message_key", "session.not_found")))
                        .build();
            }

            Session updatedSession = sessionRepository.updateSession(new Session(id, restaurantId, organizerId, deadline));
            return Response.ok(Map.of("success", true, "data", Map.of("id", updatedSession.getId()))).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSessions(@QueryParam("restaurant_id") Integer restaurantId,
                                   @QueryParam("orderable") Boolean orderable) {
        List<Session> sessions = sessionRepository.getSessions(restaurantId, orderable);
        return Response.ok(Map.of("success", true, "data", sessions)).build();  //TODO sessions gibt was zurück?
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSessionById(@PathParam("id") Integer id) {
        Optional<Session> session = sessionRepository.getSessionById(id);
        if (session.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("success", false, "error", Map.of("code", 404, "message_key", "session.not_found")))
                    .build();
        }

        return Response.ok(Map.of("success", true, "data", session.get())).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSessionById(@PathParam("id") Integer id) {
        boolean deleted = sessionRepository.deleteSession(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("success", false, "error", Map.of("code", 404, "message_key", "session.not_found")))
                    .build();
        }

        return Response.ok(Map.of("success", true)).build();
    }
}
