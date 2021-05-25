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
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
public class RouterFunctionConfiguration implements ReactiveBookAPI {

    /**
     * monoRouterFunction.
     *
     * @param bookHandler a {@link BookHandler} object.
     * @return a {@link org.springframework.web.reactive.function.server.RouterFunction} object.
     */
    @Bean
    @Override
    public RouterFunction<ServerResponse> monoRouterFunction(
            BookHandler bookHandler, ValidationBookHandler validationBookHandler) {
        return RouterFunctions.route(
                        GET(BookConstants.BOOKS_END_POINT).and(accept(MediaType.APPLICATION_JSON)),
                        request -> bookHandler.getAll())
                .andRoute(
                        GET("/api/book/{id}").and(accept(MediaType.APPLICATION_JSON)),
                        bookHandler::getBook)
                .andRoute(
                        POST(BookConstants.BOOKS_END_POINT).and(accept(MediaType.APPLICATION_JSON)),
                        validationBookHandler::handleRequest)
                .andRoute(
                        PUT("/api/book/{id}").and(accept(MediaType.APPLICATION_JSON)),
                        validationBookHandler::handleRequest)
                .andRoute(
                        DELETE("/api/book/{id}").and(accept(MediaType.APPLICATION_JSON)),
                        bookHandler::deleteBook)
                .andRoute(
                        DELETE(BookConstants.BOOKS_END_POINT)
                                .and(accept(MediaType.APPLICATION_JSON)),
                        request -> bookHandler.deleteAllBooks());
    }
}
