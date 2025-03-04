package com.github.food2gether.sessionservice.repository;

import java.util.Optional;

public interface AnonymousRepository {

  boolean exists(Class<?> entityClass, Object id);

  <T>Optional<T> findOptional(Class<T> entityClass, Object id);

}
