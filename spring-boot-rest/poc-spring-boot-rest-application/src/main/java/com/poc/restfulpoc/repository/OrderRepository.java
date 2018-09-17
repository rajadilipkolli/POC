package com.poc.restfulpoc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poc.restfulpoc.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}
