package com.github.food2gether.sessionservice.service;

import com.github.food2gether.shared.model.Session;
import java.util.List;

public interface SessionService {

  Session createOrUpdate(Session.DTO sessionDto);

  List<Session> getAll(Long restaurantId, boolean filterOrderable);

  Session getById(Long id);

  Session delete(String userEmail, Long id);
}
