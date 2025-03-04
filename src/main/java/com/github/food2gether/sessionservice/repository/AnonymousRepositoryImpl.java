package com.github.food2gether.sessionservice.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.util.Optional;

@ApplicationScoped
public class AnonymousRepositoryImpl implements AnonymousRepository {

  @Inject
  EntityManager entityManager;

  @Override
  public boolean exists(Class<?> entityClass, Object id) {
    return this.findOptional(entityClass, id).isPresent();
  }

  @Override
  public <T> Optional<T> findOptional(Class<T> entityClass, Object id) {
    return Optional.ofNullable(this.entityManager.find(entityClass, id));
  }
}
