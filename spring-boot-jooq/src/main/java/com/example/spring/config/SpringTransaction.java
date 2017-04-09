package com.example.spring.config;

import org.jooq.Transaction;
import org.springframework.transaction.TransactionStatus;

public class SpringTransaction implements Transaction {
    final TransactionStatus tx;

    SpringTransaction(TransactionStatus tx) {
        this.tx = tx;
    }
}
