package com.mongodb.redis.integration.config;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.mongodb.redis.integration.handler.BookHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration(proxyBeanMethods = false)
public class RoutingConfiguration {

  /**
   * monoRouterFunction.
   *
   * @param bookHandler a {@link BookHandler} object.
   * @return a {@link org.springframework.web.reactive.function.server.RouterFunction} object.
   */
  @Bean
  public RouterFunction<ServerResponse> monoRouterFunction(BookHandler bookHandler) {
    return route(
            GET("/api/book").and(accept(MediaType.APPLICATION_JSON)),
            request -> bookHandler.getAll())
        .andRoute(
            GET("/api/book/{id}").and(accept(MediaType.APPLICATION_JSON)), bookHandler::getBook)
        .andRoute(POST("/api/book/").and(accept(MediaType.APPLICATION_JSON)), bookHandler::postBook)
        .andRoute(
            PUT("/api/book/{id}").and(accept(MediaType.APPLICATION_JSON)), bookHandler::putBook)
        .andRoute(
            DELETE("/api/book/{id}").and(accept(MediaType.APPLICATION_JSON)),
            bookHandler::deleteBook)
        .andRoute(
                DELETE("/api/book/").and(accept(MediaType.APPLICATION_JSON)),
                request -> bookHandler.deleteAllBooks());
  }
}
