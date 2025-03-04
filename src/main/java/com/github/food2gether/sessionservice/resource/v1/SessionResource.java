package com.github.food2gether.sessionservice.resource.v1;

import com.github.food2gether.shared.Constant;
import com.github.food2gether.shared.model.Session;
import com.github.food2gether.shared.response.APIResponse;
import com.github.food2gether.sessionservice.service.SessionService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
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
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
@Path("/api/v1/sessions")
public class SessionResource {

  @Inject
  SessionService service;

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createOrUpdateSession(Session.DTO body) {
    if (body == null) {
      throw new WebApplicationException("Missing request body", Response.Status.BAD_REQUEST);
    }
    Session session = this.service.createOrUpdate(body);

    return APIResponse.response(
        body.getId() == null
            ? Response.Status.CREATED
            : Response.Status.OK,
        Session.DTO.fromSession(session)
    );
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAllSessions(
      @QueryParam("restaurant_id") Long restaurantId,
      @QueryParam("orderable") @DefaultValue("false") boolean filterOrderable
  ) {
    List<Session> sessions = this.service.getAll(restaurantId, filterOrderable);

    return APIResponse.response(
        Response.Status.OK,
        sessions.stream()
            .map(Session.DTO::fromSession)
            .toList()
    );
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getSessionById(@PathParam("id") Long id) {
    return APIResponse.response(
        Response.Status.OK,
        Session.DTO.fromSession(this.service.getById(id))
    );
  }

  @DELETE
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteSession(
      // This is provided by the auth proxy, therefore it is safe to use for user identification
      @HeaderParam(Constant.USER_MAIL_HEADER) String userEmail,
      @PathParam("id") Long id
  ) {
    Session session = this.service.delete(userEmail, id);
    return APIResponse.response(Response.Status.OK, Session.DTO.fromSession(session));
  }

}
