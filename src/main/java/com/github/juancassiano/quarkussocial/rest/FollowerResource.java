package com.github.juancassiano.quarkussocial.rest;


import java.util.List;
import java.util.stream.Collectors;

import com.github.juancassiano.quarkussocial.domain.model.Follower;
import com.github.juancassiano.quarkussocial.domain.model.User;
import com.github.juancassiano.quarkussocial.domain.repository.FollowerRepository;
import com.github.juancassiano.quarkussocial.domain.repository.UserRepository;
import com.github.juancassiano.quarkussocial.rest.dto.FollowerPerUserResponse;
import com.github.juancassiano.quarkussocial.rest.dto.FollowerRequest;
import com.github.juancassiano.quarkussocial.rest.dto.FollowerResponse;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
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

  @GET
  public Response listFollowers(@PathParam("userId") Long userId){
    User user = userRepository.findById(userId);
    if (user == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }

    List<Follower> followers = followerRepository.findByUser(userId);
    FollowerPerUserResponse responseUsers = new FollowerPerUserResponse();
    responseUsers.setCount(followers.size());
    
    List<FollowerResponse> followerList = followers.stream()
        .map(FollowerResponse::new).collect(Collectors.toList());

    responseUsers.setContent(followerList);

    return Response.ok(responseUsers).build();
  }

  @DELETE
  @Transactional
  public Response unfollowerUser(@PathParam("userId") Long userId, @QueryParam("followerId") Long followerId) {
    User user = userRepository.findById(userId);
    if (user == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    if (followerId == null) {
      return Response.status(Response.Status.BAD_REQUEST).entity("Follower ID must be provided").build();
    }
    
    User follower = userRepository.findById(followerId);
    if (follower == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    if (!followerRepository.follows(follower, user)) {
        return Response.status(Response.Status.CONFLICT).entity("This user is not following the given user.").build();    }


    followerRepository.deleteByFollowerAndUser(followerId, userId);
    return Response.status(Response.Status.NO_CONTENT).build();
  }
  
}
