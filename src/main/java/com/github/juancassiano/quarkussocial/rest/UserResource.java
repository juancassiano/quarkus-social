package com.github.juancassiano.quarkussocial.rest;

import com.github.juancassiano.quarkussocial.domain.model.User;
import com.github.juancassiano.quarkussocial.domain.repository.UserRepository;
import com.github.juancassiano.quarkussocial.rest.dto.CreateUserRequest;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

  private final UserRepository userRepository;
  
  @Inject
  public UserResource(UserRepository userRepository) {
    this.userRepository = userRepository;
  }
  
  @POST
  @Transactional
  public Response createUser(CreateUserRequest createUserResquest){
    User user = new User();
    user.setName(createUserResquest.getName());
    user.setAge(createUserResquest.getAge());
 
    userRepository.persist(user);
     
    return Response.ok(user).build();
  }

  @GET
  public Response listAllUsers(){
    PanacheQuery<User> query = userRepository.findAll();
    return Response.ok(query.list()).build();
  }

  @GET
  @Path("/{id}")
  public Response getUser(@PathParam("id") Long id){
    User user = userRepository.findById(id);
    if (user == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok(user).build();
  }

  @DELETE
  @Transactional
  @Path("/{id}")
  public Response deleteUser(@PathParam("id") Long id){
    User user = userRepository.findById(id);
    if (user == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    userRepository.delete(user);
    return Response.noContent().build();
    
  }

  @PUT
  @Path("/{id}")
  @Transactional
  public Response updateUser(@PathParam("id") Long id, CreateUserRequest createUserRequest){
    User user = userRepository.findById(id);
    if (user == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    user.setName(createUserRequest.getName());
    user.setAge(createUserRequest.getAge());
    userRepository.persist(user);
    return Response.ok(user).build();
  }
}


