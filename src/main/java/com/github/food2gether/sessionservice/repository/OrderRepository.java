package com.github.food2gether.sessionservice.repository;

import com.github.food2gether.model.Order;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

public interface OrderRepository extends PanacheRepository<Order> {

}
