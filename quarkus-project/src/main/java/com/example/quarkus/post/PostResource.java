package com.example.quarkus.post;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

@Path("/posts/")
@RequestScoped
public class PostResource {

    @Context
    UriInfo uriInfo;

    private final PostRepository postRepository;

    @Inject
    public PostResource(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPosts() {
        // Convert the map values to a list to return all posts
        List<Post> posts = new ArrayList<>(this.postRepository.getAllPosts());
        return Response.ok(posts).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response savePost(@Valid Post post) {
        Post saved = this.postRepository.save(Post.of(post.getTitle(), post.getContent()));
        return Response.created(uriInfo.getBaseUriBuilder().path("/posts/{id}").build(saved.getId()))
                .build();
    }

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPostById(@PathParam("id") final Long id) {
        Post post = this.postRepository.getById(id);
        if (post == null) {
            throw new PostNotFoundException(id);
        }
        return Response.ok(post).build();
    }

    @Path("{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePost(@PathParam("id") final Long id, @Valid Post post) {
        Post existingPost = this.postRepository.getById(id);
        if (existingPost == null) {
            throw new PostNotFoundException(id);
        }

        post.setId(id);
        Post updated = this.postRepository.update(post);
        return Response.ok(updated).build();
    }

    @Path("{id}")
    @DELETE
    public Response deletePost(@PathParam("id") final Long id) {
        Post existingPost = this.postRepository.getById(id);
        if (existingPost == null) {
            throw new PostNotFoundException(id);
        }

        this.postRepository.deleteById(id);
        return Response.noContent().build();
    }
}
