/* Licensed under Apache-2.0 2021-2022 */
package com.mongodb.redis.integration.route;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import com.mongodb.redis.integration.api.ReactiveBookAPI;
import com.mongodb.redis.integration.constants.BookConstants;
import com.mongodb.redis.integration.handler.BookHandler;
import com.mongodb.redis.integration.handler.ValidationBookHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration(proxyBeanMethods = false)
public class RouterFunctionConfiguration implements ReactiveBookAPI {

    @Bean
    @Override
    public RouterFunction<ServerResponse> monoRouterFunction(
            BookHandler bookHandler, ValidationBookHandler validationBookHandler) {
        return RouterFunctions.route(
                        GET(BookConstants.BOOKS_END_POINT).and(accept(MediaType.APPLICATION_JSON)),
                        request -> bookHandler.getAll())
                .andRoute(
                        GET(BookConstants.BOOKS_END_POINT + "{id}")
                                .and(accept(MediaType.APPLICATION_JSON)),
                        bookHandler::getBook)
                .andRoute(
                        POST(BookConstants.BOOKS_END_POINT).and(accept(MediaType.APPLICATION_JSON)),
                        validationBookHandler::handleRequest)
                .andRoute(
                        PUT(BookConstants.BOOKS_END_POINT + "{id}")
                                .and(accept(MediaType.APPLICATION_JSON)),
                        validationBookHandler::handleRequest)
                .andRoute(
                        DELETE(BookConstants.BOOKS_END_POINT + "{id}")
                                .and(accept(MediaType.APPLICATION_JSON)),
                        bookHandler::deleteBook)
                .andRoute(
                        DELETE(BookConstants.BOOKS_END_POINT)
                                .and(accept(MediaType.APPLICATION_JSON)),
                        request -> bookHandler.deleteAllBooks());
    }
}
