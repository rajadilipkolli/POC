/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.restfulpoc.config;

import javax.jms.ConnectionFactory;

import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

/**
 * <p>
 * JMSApplicationConfig class.
 * </p>
 *
 * @author Raja Kolli
 * @version 0: 5
 */
@Configuration
@EnableJms
public class JMSApplicationConfig {

	/**
	 * <p>
	 * myJMSFactory.
	 * </p>
	 * @param connectionFactory a {@link javax.jms.ConnectionFactory} object.
	 * @param configurer a
	 * {@link org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer}
	 * object.
	 * @return a {@link org.springframework.jms.config.JmsListenerContainerFactory}
	 * object.
	 */
	@Bean
	public JmsListenerContainerFactory<DefaultMessageListenerContainer> myJMSFactory(
			ConnectionFactory connectionFactory,
			DefaultJmsListenerContainerFactoryConfigurer configurer) {
		final DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		// This provides all boot's default to this factory, including the message
		// converter
		configurer.configure(factory, connectionFactory);
		// You could still override some of Boot's default if necessary.
		return factory;
	}

	/**
	 * <p>
	 * jacksonJmsMessageConverter.
	 * </p>
	 * @return a {@link org.springframework.jms.support.converter.MessageConverter}
	 * object.
	 */
	@Bean
	public MessageConverter jacksonJmsMessageConverter() {
		final MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		return converter;
	}

}
