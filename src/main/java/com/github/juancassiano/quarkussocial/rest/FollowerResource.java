package com.github.juancassiano.quarkussocial.rest;


import com.github.juancassiano.quarkussocial.domain.model.Follower;
import com.github.juancassiano.quarkussocial.domain.model.User;
import com.github.juancassiano.quarkussocial.domain.repository.FollowerRepository;
import com.github.juancassiano.quarkussocial.domain.repository.UserRepository;
import com.github.juancassiano.quarkussocial.rest.dto.FollowerRequest;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users/{userId}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowerResource {
  
  private FollowerRepository followerRepository;
  private UserRepository userRepository;

  @Inject
  public FollowerResource(FollowerRepository followerRepository, UserRepository userRepository) {
    this.followerRepository = followerRepository;
    this.userRepository = userRepository;
  }

  @PUT
  @Transactional
  public Response followUser(@PathParam("userId") Long userId, FollowerRequest followRequest) {
    User user = userRepository.findById(userId);
    if (user == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    User followerUser = userRepository.findById(followRequest.getFollowerId());
    if (followerUser == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    if (followerUser.getId().equals(user.getId())) {
      return Response.status(Response.Status.CONFLICT).entity("You cannot follow yourself").build();
    }
    if (followerRepository.follows(followerUser, user)) {
      return Response.status(Response.Status.CONFLICT).entity("You are already following this user").build();
    }


    Follower follower = new Follower();
    follower.setUser(user);
    follower.setFollower(followerUser);
    
    followerRepository.persist(follower);
    
    return Response.status(Response.Status.NO_CONTENT).entity(follower).build();
  }
  
}
