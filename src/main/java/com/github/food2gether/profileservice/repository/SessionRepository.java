package com.github.food2gether.profileservice.repository;
import com.github.food2gether.profileservice.api.Session;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SessionRepository {
    private final Map<Integer, Session> sessionStore = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    // Create a new session
    public Session createSession(Session session) {
        int newId = idGenerator.getAndIncrement();
        session.setId(newId);
        sessionStore.put(newId, session);
        return session;
    }

    // Retrieve a session by ID
    public Optional<Session> getSessionById(Integer id) {
        return Optional.ofNullable(sessionStore.get(id));
    }

    // Retrieve all sessions, with optional filters
    public List<Session> getSessions(Integer restaurantId, Boolean orderable) {
        List<Session> sessions = new ArrayList<>(sessionStore.values());

        if (restaurantId != null) {
            sessions.removeIf(session -> !restaurantId.equals(session.getRestaurantId()));
        }

        // Example "orderable" filter logic: include sessions with deadlines in the future
        if (orderable != null && orderable) {
            sessions.removeIf(session -> session.getDeadline().before(new Date()));
        }

        return sessions;
    }

    // Update an existing session
    public Session updateSession(Session session) {
        if (!sessionStore.containsKey(session.getId())) {
            throw new IllegalArgumentException("Session with ID " + session.getId() + " does not exist.");
        }

        sessionStore.put(session.getId(), session);
        return session;
    }

    // Delete a session by ID
    public boolean deleteSession(Integer id) {
        return sessionStore.remove(id) != null;
    }
}

