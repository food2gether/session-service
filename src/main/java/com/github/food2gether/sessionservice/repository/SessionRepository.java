package com.github.food2gether.sessionservice.repository;

import com.github.food2gether.sessionservice.model.Session;
import com.github.food2gether.sessionservice.model.Session$;
import com.speedment.jpastreamer.application.JPAStreamer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
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

  public Optional<List<Session>> getSessions(Integer restaurantId, Boolean orderable, Integer organizerId) {
    var stream = jpaStreamer.stream(Session.class);

    if (restaurantId != null) {
      stream = stream.filter(Session$.restaurantId.equal(restaurantId));
    }

    if (orderable != null) {
      if (orderable) {
        stream = stream.filter(Session$.deadline.greaterThan(LocalDateTime.now()));
      }else {
        stream = stream.filter(Session$.deadline.lessThan(LocalDateTime.now()));
      }
    }

    if (organizerId != null) {
      stream = stream.filter(Session$.organizerId.equal(organizerId));
    }

    List<Session> sessions = stream.toList();
    return sessions.isEmpty() ? Optional.empty() : Optional.of(sessions);
  }


  @Transactional
  public boolean deleteSessionById(Session session) {
      entityManager.remove(entityManager.contains(session) ? session : entityManager.merge(session));
      return true;
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
