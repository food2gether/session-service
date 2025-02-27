package com.github.food2gether.sessionservice.service;

import com.github.food2gether.model.Session;
import java.util.List;

public interface SessionService {

  Session createOrUpdate(Session.DTO sessionDto);

  Session create(Session.DTO sessionDto);

  Session update(Session.DTO session);

  List<Session> getAll(Long restaurantId, boolean filterOrderable);

  Session getById(Long id);

  Session delete(String userEmail, Long id);
}
