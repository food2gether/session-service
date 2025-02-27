package com.github.food2gether.sessionservice;

import com.github.food2gether.response.APIResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

@ApplicationScoped
public class ExceptionMapper {


  @ServerExceptionMapper
  public Response handleException(Exception e) {
    if (e instanceof WebApplicationException webApplicationException) {
      return APIResponse.response(webApplicationException.getResponse().getStatus(), e);
    }
    if (e instanceof EntityNotFoundException) {
      return APIResponse.response(Response.Status.NOT_FOUND, e);
    }

    return APIResponse.response(e);
  }

}
