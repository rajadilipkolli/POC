/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.mongodbredisintegration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * <p>
 * MongoDBRedisIntegrationApplication class.
 * </p>
 *
 * @author rajakolli
 * @version 0 : 5
 * @since July 2017
 */
@SpringBootApplication
@EnableWebFlux
public class MongoDBRedisIntegrationApplication {

	/**
	 * <p>
	 * main.
	 * </p>
	 * @param args an array of {@link java.lang.String} objects.
	 */
	public static void main(String[] args) {
		SpringApplication.run(MongoDBRedisIntegrationApplication.class, args);
	}

}
