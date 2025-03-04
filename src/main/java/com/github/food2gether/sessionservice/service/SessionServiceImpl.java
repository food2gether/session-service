package com.github.food2gether.sessionservice.service;

import com.github.food2gether.sessionservice.repository.AnonymousRepository;
import com.github.food2gether.shared.model.Profile;
import com.github.food2gether.shared.model.Restaurant;
import com.github.food2gether.shared.model.Session;
import com.github.food2gether.sessionservice.repository.SessionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.TimeZone;

@ApplicationScoped
public class SessionServiceImpl implements SessionService {

  @Inject
  SessionRepository sessionRepository;

  @Inject
  AnonymousRepository anonymousRepository;

  @Override
  public Session createOrUpdate(Session.DTO sessionDto) {
    return sessionDto.getId() == null ? this.create(sessionDto) : this.update(sessionDto);
  }

  @Transactional
  public Session create(Session.DTO sessionDto) {
    if (sessionDto.getId() != null) {
      throw new WebApplicationException(
          "Session id must be null for creating a new session",
          Status.BAD_REQUEST
      );
    }

    // Check if all required fields are present for creating a new session
    if (sessionDto.getRestaurantId() == null
        || sessionDto.getOrganizerId() == null
        || sessionDto.getDeadline() == null) {
      throw new WebApplicationException("Missing required fields for creating a new session", Status.BAD_REQUEST);
    }

    if (sessionDto.getDeadline().isBefore(LocalDateTime.now())) {
      throw new WebApplicationException("Session deadline must be in the future", Status.BAD_REQUEST);
    }

    Session session = new Session();

    session.setRestaurant(
        this.anonymousRepository.findOptional(Restaurant.class, sessionDto.getRestaurantId())
            .orElseThrow(() -> new NotFoundException("Restaurant not found")));
    session.setOrganizer(
        this.anonymousRepository.findOptional(Profile.class, sessionDto.getOrganizerId())
            .orElseThrow(() -> new NotFoundException("Organizer not found")));
    session.setDeadline(sessionDto.getDeadline());

    this.sessionRepository.persist(session);
    return session;
  }

  @Transactional
  public Session update(Session.DTO session) {
    if (session.getId() == null) {
      throw new WebApplicationException("Session id must not be null for updating a session", Status.BAD_REQUEST);
    }

    Session existingSession = this.sessionRepository.findByIdOptional(session.getId()).
        orElseThrow(() -> new NotFoundException("Session with id " + session.getId() + " does not exist"));

    LocalDateTime deadline = session.getDeadline();
    if (deadline != null) {
      if (deadline.isBefore(LocalDateTime.now())) {
        throw new WebApplicationException("Session deadline must be in the future", Status.BAD_REQUEST);
      }
      existingSession.setDeadline(session.getDeadline());
    }

    if (session.getRestaurantId() != null) {
      existingSession.setRestaurant(
          this.anonymousRepository.findOptional(Restaurant.class, session.getRestaurantId())
              .orElseThrow(() -> new NotFoundException("Restaurant not found")));
    }

    if (session.getOrganizerId() != null) {
      existingSession.setOrganizer(
          this.anonymousRepository.findOptional(Profile.class, session.getOrganizerId())
              .orElseThrow(() -> new NotFoundException("Organizer not found")));
    }

    this.sessionRepository.persist(existingSession);
    return existingSession;
  }

  @Override
  public List<Session> getAll(Long restaurantId, boolean filterOrderable) {
    LocalDateTime dueDate = filterOrderable ? LocalDateTime.now() : LocalDateTime.of(0, 1, 1, 0, 0);
    return restaurantId == null
        ? this.sessionRepository.findByDueDate(dueDate)
        : this.sessionRepository.findByRestaurantAndDueDate(restaurantId, dueDate);
  }

  @Override
  public Session getById(Long id) {
    return this.sessionRepository.findByIdOptional(id)
        .orElseThrow(() -> new WebApplicationException("Session with id " + id + " does not exist", Status.NOT_FOUND));
  }

  @Override
  @Transactional
  public Session delete(String userEmail, Long id) {
    Session session = this.sessionRepository.findByIdOptional(id)
        .orElseThrow(() -> new NotFoundException("Session with id " + id + " does not exist"));

    if (!session.getOrganizer().getPrimaryEmail().equals(userEmail)) {
      throw new WebApplicationException("Only the organizer of a session can delete it", Status.FORBIDDEN);
    }

    this.sessionRepository.delete(session);
    return session;
  }
}
