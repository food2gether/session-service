package com.github.food2gether.sessionservice;

import com.github.food2gether.shared.ExceptionHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

@ApplicationScoped
public class ExceptionMapper {

  @ServerExceptionMapper
  public Response handleException(Exception e) {
    return ExceptionHandler.handle(e);
  }

}
