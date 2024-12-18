package com.github.food2gether.sessionservice.ressource;

import com.github.food2gether.sessionservice.model.Session;
import com.github.food2gether.sessionservice.repository.SessionRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;
import java.util.Optional;

@Path("/api/v1/sessions")
public class SessionRessource {
  @Inject
  SessionRepository sessionRepository;

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

}
