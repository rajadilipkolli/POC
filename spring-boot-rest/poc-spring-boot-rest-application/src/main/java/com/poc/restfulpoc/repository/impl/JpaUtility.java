package com.poc.restfulpoc.repository.impl;

import javax.persistence.EntityManager;

public interface JpaUtility {

	default <T> T doInJPA(JPATransactionFunction<T> function, EntityManager entityManager) {
		T result = null;
		try {
			function.beforeTransactionCompletion();
			result = function.apply(entityManager);
		}
		finally {
			function.afterTransactionCompletion();
		}
		return result;
	}

}
