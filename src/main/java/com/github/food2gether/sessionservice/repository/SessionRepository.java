package com.github.food2gether.sessionservice.repository;

import com.github.food2gether.sessionservice.model.Session;
import com.github.food2gether.sessionservice.model.Session$;
import com.speedment.jpastreamer.application.JPAStreamer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Optional;

@ApplicationScoped
public class SessionRepository {

  @Inject
  JPAStreamer jpaStreamer;

  @PersistenceContext
  EntityManager entityManager;

  public Optional<Session> getSessionById(Integer id) {
    return jpaStreamer.stream(Session.class)
        .filter(Session$.id.equal(id))
        .findFirst();
  }

//  public void deleteSessionById(Integer id) {
//    jpaStreamer.stream(Session.class)
//        .filter(Session$.id.equal(id))
//        .forEach(session -> entityManager.remove(entityManager.contains(session) ? session : entityManager.merge(session)));
//  }

}
