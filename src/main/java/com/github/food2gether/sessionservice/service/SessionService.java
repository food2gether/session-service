package com.github.food2gether.sessionservice.service;

import com.github.food2gether.model.Profile;
import com.github.food2gether.model.Restaurant;
import com.github.food2gether.model.Session;
import com.github.food2gether.sessionservice.repository.ProfileRepository;
import com.github.food2gether.sessionservice.repository.RestaurantRepository;
import com.github.food2gether.sessionservice.repository.SessionRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class SessionService {

  @Inject
  SessionRepository sessionRepository;

  @Inject
  ProfileRepository profileRepository;

  @Inject
  RestaurantRepository restaurantRepository;

  public Session createOrUpdateSession(Session.DTO sessionDto) {
    return sessionDto.getId() == null ? this.createSession(sessionDto) : this.updateSession(sessionDto);
  }

  public Session createSession(Session.DTO sessionDto) {
    if (sessionDto.getId() != null) {
      throw new IllegalArgumentException("Session id must be null for creating a new session");
    }

    // Check if all required fields are present for creating a new session
    if (sessionDto.getRestaurantId() == null
        || sessionDto.getOrganizerId() == null
        || sessionDto.getDeadline() == null) {
      throw new IllegalArgumentException("Missing required fields for creating a new session");
    }

    Restaurant restaurant = this.restaurantRepository.findByIdOptional(sessionDto.getRestaurantId())
        .orElseThrow(() -> new IllegalArgumentException("Restaurant with id " + sessionDto.getRestaurantId() + " does not exist"));
    Profile organizer = this.profileRepository.findByIdOptional(sessionDto.getOrganizerId())
        .orElseThrow(() -> new IllegalArgumentException("Profile with id " + sessionDto.getOrganizerId() + " does not exist"));

    Session session = new Session(null, List.of(), restaurant, organizer, sessionDto.getDeadline());
    this.sessionRepository.persist(session);
    return session;
  }

  public Session updateSession(Session.DTO session) {
    if (session.getId() == null) {
      throw new IllegalArgumentException("Session id must not be null for updating a session");
    }

    Session existingSession = this.sessionRepository.findByIdOptional(session.getId()).
        orElseThrow(() -> new IllegalArgumentException("Session with id " + session.getId() + " does not exist"));

    if (session.getDeadline() != null) existingSession.setDeadline(session.getDeadline());

    if (session.getRestaurantId() != null) {
      Restaurant restaurant = this.restaurantRepository.findByIdOptional(session.getRestaurantId())
          .orElseThrow(() -> new IllegalArgumentException("Restaurant with id " + session.getRestaurantId() + " does not exist"));
      existingSession.setRestaurant(restaurant);
    }

    if (session.getOrganizerId() != null) {
      Profile organizer = this.profileRepository.findByIdOptional(session.getOrganizerId())
          .orElseThrow(() -> new IllegalArgumentException("Profile with id " + session.getOrganizerId() + " does not exist"));
      existingSession.setOrganizer(organizer);
    }

    this.sessionRepository.persist(existingSession);
    return existingSession;
  }

  public List<Session> getAllSessions(Long restaurantId, boolean filterOrderable) {
    LocalDateTime dueDate = filterOrderable ? LocalDateTime.now() : LocalDateTime.MAX;
    return restaurantId == null
        ? this.sessionRepository.findByDueDate(dueDate)
        : this.sessionRepository.findByRestaurantAndDueDate(restaurantId, dueDate);
  }

  public Session getSessionById(Long id) {
    return this.sessionRepository.findByIdOptional(id)
        .orElseThrow(() -> new IllegalArgumentException("Session with id " + id + " does not exist"));
  }

  public Session deleteSession(String userEmail, Long id) {
    Session session = this.sessionRepository.findByIdOptional(id)
        .orElseThrow(() -> new IllegalArgumentException("Session with id " + id + " does not exist"));

    if (!session.getOrganizer().getPrimaryEmail().equals(userEmail)) {
      throw new IllegalArgumentException("Only the organizer of a session can delete it");
    }

    this.sessionRepository.delete(session);
    return session;
  }
}
