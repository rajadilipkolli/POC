package com.example.quarkus.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class HelloService {

    @ConfigProperty(name = "greeting")
    String greeting;

    public String politeHello(String name) {
        return greeting + " " + name;
    }
}
