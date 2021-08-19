package com.poc.boot.rabbitmq.config;

import com.poc.boot.rabbitmq.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Slf4j
public class OrderMessageListener {

    @RabbitListener(queues = RabbitMQConfig.QUEUE_ORDERS)
    public void processOrder(Order order) {
        log.debug("Order Received: {}", order);
    }
}
