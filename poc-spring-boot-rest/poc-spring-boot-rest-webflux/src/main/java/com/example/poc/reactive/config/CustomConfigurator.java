package com.example.poc.reactive.config;

public class CustomConfigurator extends ContextAwareBase implements Configurator {

	@Override
	public void configure(LoggerContext context) {

		// Set this to avoid LoggerContext override by Spring Boot
		context.putObject(LoggingSystem.class.getName(), new Object());

		// Apply Spring Boot default configuration, make sure CustomConfigurator
		// is in the org.springframework.boot.logging.logback package
		LogbackConfigurator configurator = new LogbackConfigurator(context);
		new DefaultLogbackConfiguration(null).apply(configurator);
		context.setPackagingDataEnabled(true);
    }
}