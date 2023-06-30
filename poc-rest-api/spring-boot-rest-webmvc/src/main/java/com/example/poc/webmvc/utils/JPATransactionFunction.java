/* Licensed under Apache-2.0 2021-2023 */
package com.example.poc.webmvc.utils;

import jakarta.persistence.EntityManager;
import java.util.function.Function;

@FunctionalInterface
public interface JPATransactionFunction<T> extends Function<EntityManager, T> {

    default void beforeTransactionCompletion() {}

    default void afterTransactionCompletion() {}
}
