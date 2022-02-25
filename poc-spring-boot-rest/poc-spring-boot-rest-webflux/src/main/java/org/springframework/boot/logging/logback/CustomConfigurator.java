package org.springframework.boot.logging.logback;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.Configurator;
import ch.qos.logback.core.spi.ContextAwareBase;
import org.springframework.boot.logging.LoggingSystem;

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