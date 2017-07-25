package com.example.demo.config;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageReceiver {
 
    @JmsListener(destination = "jms.message.endpoint")
    public void receiveMessage(Message msg) 
    {
        System.out.println("Received " + msg );
    }
}
