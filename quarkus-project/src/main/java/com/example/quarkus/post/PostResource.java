package com.example.quarkus.post;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response savePost(@Valid Post post) {
        Post saved = this.postRepository.save(Post.of(post.getTitle(), post.getContent()));
        return Response.created(
            uriInfo.getBaseUriBuilder()
                .path("/posts/{id}")
                .build(saved.getId())
        ).build();
    }

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPostById(@PathParam("id") final String id) {
        Post post = this.postRepository.getById(id);
        if (post == null) {
            throw new PostNotFoundException(id);
        }
        return Response.ok(post).build();
    }

}