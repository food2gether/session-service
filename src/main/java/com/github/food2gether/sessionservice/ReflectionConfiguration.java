package com.github.food2gether.sessionservice;

import com.github.food2gether.shared.model.Order;
import com.github.food2gether.shared.model.OrderItem;
import com.github.food2gether.shared.model.Session;
import com.github.food2gether.shared.response.DataAPIResponse;
import com.github.food2gether.shared.response.ErrorAPIResponse;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection(
    targets = {
        DataAPIResponse.class,
        ErrorAPIResponse.class,
        Session.DTO.class,
        Order.DTO.class,
        OrderItem.DTO.class
    }
)
public class ReflectionConfiguration {

}
