package com.github.food2gether.sessionservice.repository;

import com.github.food2gether.sessionservice.model.Session;
import com.github.food2gether.sessionservice.model.Session$;
import com.speedment.jpastreamer.application.JPAStreamer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
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

  @Transactional
  public boolean deleteSessionById(Integer id) {
    Optional<Session> sessionOptional = jpaStreamer.stream(Session.class)
        .filter(Session$.id.equal(id))
        .findFirst();

    if (sessionOptional.isPresent()) {
      Session session = sessionOptional.get();
      entityManager.remove(entityManager.contains(session) ? session : entityManager.merge(session));
      return true;
    } else {
      return false;
    }
  }

  @Transactional
  public Session updateSession(Session session) {
    return entityManager.merge(session);
  }

  @Transactional
  public Session createSession(Session session) {
    entityManager.persist(session);
    return session;
  }

}
