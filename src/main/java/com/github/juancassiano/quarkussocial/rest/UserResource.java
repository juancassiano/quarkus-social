package com.github.juancassiano.quarkussocial.rest;

import com.github.juancassiano.quarkussocial.rest.dto.CreateUserRequest;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
  
  @POST
  public Response createUser(CreateUserRequest createUserResquest){
    return Response.ok(createUserResquest).build();
  }

  @GET
  public Response listAllUsers(){
    return Response.ok().build();
  }
}
