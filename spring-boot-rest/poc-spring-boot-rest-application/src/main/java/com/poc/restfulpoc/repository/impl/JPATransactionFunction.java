package com.poc.restfulpoc.repository.impl;

import javax.persistence.EntityManager;
import java.util.function.Function;

@FunctionalInterface
public interface JPATransactionFunction<T> extends Function<EntityManager, T> {

	default void beforeTransactionCompletion() {

	}

	default void afterTransactionCompletion() {

	}

}
