package com.example.poc.webmvc.repository.impl;

import com.example.poc.webmvc.utils.JPATransactionFunction;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class JpaUtility {

    @PersistenceContext private EntityManager persistenceContextEntityManager;

    <T> T doInJPA(JPATransactionFunction<T> function) {
        T result;
        try {
            function.beforeTransactionCompletion();
            result = function.apply(this.persistenceContextEntityManager);
        } finally {
            function.afterTransactionCompletion();
        }
        return result;
    }
}
