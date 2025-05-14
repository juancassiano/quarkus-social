package com.github.juancassiano.quarkussocial.rest;

import com.github.juancassiano.quarkussocial.rest.dto.CreateUserRequest;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/users")
public class UserResource {
  
  @POST
  public Response createUser(CreateUserRequest createUserResquest){
    return Response.ok().build();
  }
}
