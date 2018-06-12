/*
 * Copyright 2015-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.poc.mongodbredisintegration.config;

import com.poc.mongodbredisintegration.handler.BookHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * WebFlux API Routing Configuration.
 *
 * @author Raja Kolli
 * @version $Id: $Id
 */
@Configuration
public class RoutingConfiguration {

	/**
	 * <p>monoRouterFunction.</p>
	 *
	 * @param bookHandler a {@link com.poc.mongodbredisintegration.handler.BookHandler} object.
	 * @return a {@link org.springframework.web.reactive.function.server.RouterFunction} object.
	 */
	@Bean
	public RouterFunction<ServerResponse> monoRouterFunction(BookHandler bookHandler) {
		return route(GET("/api/book").and(accept(MediaType.APPLICATION_JSON)),
				bookHandler::getAll).andRoute(
						GET("/api/book/{id}").and(accept(MediaType.APPLICATION_JSON)),
						bookHandler::getBook)
						.andRoute(
								POST("/api/book/post")
										.and(accept(MediaType.APPLICATION_JSON)),
								bookHandler::postBook)
						.andRoute(
								PUT("/api/book/put/{id}")
										.and(accept(MediaType.APPLICATION_JSON)),
								bookHandler::putBook)
						.andRoute(
								DELETE("/api/book/delete/{id}")
										.and(accept(MediaType.APPLICATION_JSON)),
								bookHandler::deleteBook);
	}

}
