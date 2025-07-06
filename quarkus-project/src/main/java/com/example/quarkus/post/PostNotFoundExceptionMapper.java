package com.example.quarkus.post;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class PostNotFoundExceptionMapper implements ExceptionMapper<PostNotFoundException> {
    @Override
    public Response toResponse(PostNotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(exception.getMessage())
                .build();
    }
}
