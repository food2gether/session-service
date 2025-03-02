package com.github.food2gether.sessionservice.controller.v1;

import com.github.food2gether.response.DataAPIResponse;
import com.github.food2gether.response.ErrorAPIResponse;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection(
    targets = {
        DataAPIResponse.class,
        ErrorAPIResponse.class
    }
)
public class ReflectionConfig {

}
