package com.github.food2gether.sessionservice.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import java.util.Map;
import java.util.stream.Stream;

public interface ReadOnlyPanacheRepository<E> extends PanacheRepository<E> {

  @Override
  default void persist(E e) {
    throw new UnsupportedOperationException("This repository is read-only");
  }

  @Override
  default void persistAndFlush(E e) {
    throw new UnsupportedOperationException("This repository is read-only");
  }

  @Override
  default void delete(E e) {
    throw new UnsupportedOperationException("This repository is read-only");
  }

  @Override
  default int update(String query, Parameters params) {
    throw new UnsupportedOperationException("This repository is read-only");
  }

  @Override
  default int update(String query, Map<String, Object> params) {
    throw new UnsupportedOperationException("This repository is read-only");
  }

  @Override
  default int update(String query, Object... params) {
    throw new UnsupportedOperationException("This repository is read-only");
  }

  @Override
  default void persist(E firstEntity, E... es) {
    throw new UnsupportedOperationException("This repository is read-only");
  }

  @Override
  default void persist(Stream<E> entities) {
    throw new UnsupportedOperationException("This repository is read-only");
  }

  @Override
  default void persist(Iterable<E> es) {
    throw new UnsupportedOperationException("This repository is read-only");
  }

  @Override
  default long delete(String query, Parameters params) {
    throw new UnsupportedOperationException("This repository is read-only");
  }

  @Override
  default long delete(String query, Map<String, Object> params) {
    throw new UnsupportedOperationException("This repository is read-only");
  }

  @Override
  default long delete(String query, Object... params) {
    throw new UnsupportedOperationException("This repository is read-only");
  }

  @Override
  default boolean deleteById(Long aLong) {
    throw new UnsupportedOperationException("This repository is read-only");
  }

  @Override
  default long deleteAll() {
    throw new UnsupportedOperationException("This repository is read-only");
  }

  @Override
  default void flush() {
    throw new UnsupportedOperationException("This repository is read-only");
  }
}
