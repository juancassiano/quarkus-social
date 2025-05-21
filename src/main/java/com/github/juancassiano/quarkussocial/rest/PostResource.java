package com.github.juancassiano.quarkussocial.rest;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


import com.github.juancassiano.quarkussocial.domain.model.Post;
import com.github.juancassiano.quarkussocial.domain.model.User;
import com.github.juancassiano.quarkussocial.domain.repository.PostRepository;
import com.github.juancassiano.quarkussocial.domain.repository.UserRepository;
import com.github.juancassiano.quarkussocial.rest.dto.CreatePostRequest;
import com.github.juancassiano.quarkussocial.rest.dto.PostResponse;
import com.github.juancassiano.quarkussocial.rest.dto.ResponseError;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

  private UserRepository userRepository;
  private PostRepository postRepository;
  private Validator validator;

  @Inject
  public PostResource(UserRepository userRepository, PostRepository postRepository, Validator validator) {
    this.validator = validator;
    this.userRepository = userRepository; 
    this.postRepository = postRepository;

  }

  @POST
  @Transactional
  public Response createPost(@PathParam("userId") Long userId, CreatePostRequest createPostRequest){
            
    Set<ConstraintViolation<CreatePostRequest>> violations = validator.validate(createPostRequest);
    if (!violations.isEmpty()) {
      return ResponseError.createFromValidation(violations).withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);
    }

    User user = userRepository.findById(userId);
    if (user == null){
      return ResponseError.createFromValidation(violations).withStatusCode(ResponseError.NOT_FOUND_STATUS);
    }

    Post post = new Post();
    post.setText(createPostRequest.getPost_text());
    post.setUser(user);
    postRepository.persist(post);
    return Response.status(Response.Status.CREATED).entity(post).build();
  }

  @GET
  public Response listPost(@PathParam("userId") Long userId){
    User user = userRepository.findById(userId);
    if (user == null){
      return Response.status(Response.Status.NOT_FOUND).build();
    }

    PanacheQuery<Post> postQuery = postRepository.find("user",user);
    
    if (postQuery == null){
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    List<Post> posts = postQuery.list();

    List<PostResponse> postResponses = posts.stream()
      .map(PostResponse::fromEntity)
      .collect(Collectors.toList());

    return Response.ok(postResponses).build();
    
  }
}
