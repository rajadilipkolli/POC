/* Licensed under Apache-2.0 2021-2022 */
package com.mongodb.redis.integration.api;

import com.mongodb.redis.integration.constants.BookConstants;
import com.mongodb.redis.integration.handler.BookHandler;
import com.mongodb.redis.integration.handler.ValidationBookHandler;
import com.mongodb.redis.integration.request.BookDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Tags(value = {@Tag(name = "Books")})
public interface ReactiveBookAPI {

    @RouterOperations({
        @RouterOperation(
                path = BookConstants.BOOKS_END_POINT,
                method = RequestMethod.GET,
                operation =
                        @Operation(
                                operationId = "findBooks",
                                summary = "Find all Books",
                                tags = {"Books"},
                                responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "successful operation",
                                            content =
                                                    @Content(
                                                            array =
                                                                    @ArraySchema(
                                                                            schema =
                                                                                    @Schema(
                                                                                            implementation =
                                                                                                    BookDTO
                                                                                                            .class)))),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Books not found")
                                })),
        @RouterOperation(
                path = BookConstants.BOOKS_END_POINT + "/{id}",
                method = RequestMethod.GET,
                operation =
                        @Operation(
                                operationId = "findBookById",
                                summary = "Find book by ID",
                                tags = {"Books"},
                                parameters = {
                                    @Parameter(
                                            in = ParameterIn.PATH,
                                            name = "id",
                                            description = "Book Id")
                                },
                                responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "successful operation",
                                            content =
                                                    @Content(
                                                            schema =
                                                                    @Schema(
                                                                            implementation =
                                                                                    BookDTO
                                                                                            .class))),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Invalid Book ID supplied"),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Book not found")
                                })),
        @RouterOperation(
                path = BookConstants.BOOKS_END_POINT,
                method = RequestMethod.POST,
                operation =
                        @Operation(
                                operationId = "createBook",
                                summary = "Creates Book",
                                tags = {"Books"},
                                requestBody =
                                        @RequestBody(
                                                content =
                                                        @Content(
                                                                schema =
                                                                        @Schema(
                                                                                implementation =
                                                                                        BookDTO
                                                                                                .class))),
                                responses = {
                                    @ApiResponse(responseCode = "201", description = "book created")
                                })),
        @RouterOperation(
                path = BookConstants.BOOKS_END_POINT + "/{id}",
                method = RequestMethod.PUT,
                operation =
                        @Operation(
                                operationId = "updateBook",
                                summary = "Updates book by ID",
                                tags = {"Books"},
                                parameters = @Parameter(name = "id", in = ParameterIn.PATH),
                                requestBody =
                                        @RequestBody(
                                                content =
                                                        @Content(
                                                                schema =
                                                                        @Schema(
                                                                                implementation =
                                                                                        BookDTO
                                                                                                .class))),
                                responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "updated book successfully"),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Book not found")
                                })),
        @RouterOperation(
                path = BookConstants.BOOKS_END_POINT + "/{id}",
                method = RequestMethod.DELETE,
                operation =
                        @Operation(
                                operationId = "deleteBook",
                                summary = "Deletes book by ID",
                                tags = {"Books"},
                                parameters = @Parameter(name = "id", in = ParameterIn.PATH),
                                responses = {
                                    @ApiResponse(
                                            responseCode = "202",
                                            description = "successful operation"),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Book not found")
                                })),
        @RouterOperation(
                path = BookConstants.BOOKS_END_POINT,
                method = RequestMethod.DELETE,
                operation =
                        @Operation(
                                operationId = "deletesAllBook",
                                summary = "Deletes all Books",
                                tags = {"Books"},
                                responses = {
                                    @ApiResponse(
                                            responseCode = "202",
                                            description = "successful operation")
                                }))
    })
    RouterFunction<ServerResponse> monoRouterFunction(
            BookHandler bookHandler, ValidationBookHandler validationBookHandler);
}
