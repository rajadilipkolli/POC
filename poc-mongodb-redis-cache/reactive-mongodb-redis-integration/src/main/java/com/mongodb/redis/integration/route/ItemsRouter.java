/* Licensed under Apache-2.0 2021-2022 */
package com.mongodb.redis.integration.route;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

import com.mongodb.redis.integration.constants.ItemConstants;
import com.mongodb.redis.integration.handler.ItemsHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration(proxyBeanMethods = false)
public class ItemsRouter {

    @Bean
    public RouterFunction<ServerResponse> itemRouterFunction(ItemsHandler itemsHandler) {
        return RouterFunctions.route(
                        GET(ItemConstants.ITEM_FUNCTIONAL_END_POINT_V_1)
                                .and(accept(MediaType.APPLICATION_JSON)),
                        itemsHandler::getAllItems)
                .andRoute(
                        GET(ItemConstants.ITEM_FUNCTIONAL_END_POINT_V_1 + "/{id}")
                                .and(accept(MediaType.APPLICATION_JSON)),
                        itemsHandler::getItemById)
                .andRoute(
                        POST(ItemConstants.ITEM_FUNCTIONAL_END_POINT_V_1)
                                .and(accept(MediaType.APPLICATION_JSON)),
                        itemsHandler::createItem)
                .andRoute(
                        DELETE(ItemConstants.ITEM_FUNCTIONAL_END_POINT_V_1 + "/{id}")
                                .and(accept(MediaType.APPLICATION_JSON)),
                        itemsHandler::deleteItem)
                .andRoute(
                        PUT(ItemConstants.ITEM_FUNCTIONAL_END_POINT_V_1 + "/{id}")
                                .and(accept(MediaType.APPLICATION_JSON)),
                        itemsHandler::updateItem);
    }

    @Bean
    public RouterFunction<ServerResponse> itemStreamFunction(ItemsHandler itemsHandler) {
        return RouterFunctions.route(
                GET(ItemConstants.ITEM_STREAM_FUNCTIONAL_END_POINT_V_1)
                        .and(accept(MediaType.APPLICATION_JSON)),
                itemsHandler::itemsStream);
    }
}
