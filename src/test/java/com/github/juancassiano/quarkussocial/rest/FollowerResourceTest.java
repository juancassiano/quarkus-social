package com.github.juancassiano.quarkussocial.rest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.github.juancassiano.quarkussocial.domain.model.Follower;
import com.github.juancassiano.quarkussocial.domain.model.User;
import com.github.juancassiano.quarkussocial.domain.repository.FollowerRepository;
import com.github.juancassiano.quarkussocial.domain.repository.UserRepository;
import com.github.juancassiano.quarkussocial.rest.dto.FollowerRequest;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@QuarkusTest
@TestHTTPEndpoint(FollowerResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FollowerResourceTest {

  @Inject
  UserRepository userRepository;
  @Inject
  FollowerRepository followerRepository;

  Long userId;
  Long followerId;

  @BeforeEach
  @Transactional
  public void setUp(){
    User user = new User();
    user.setName("Test User");
    user.setAge(25);
    userRepository.persist(user);
    userId = user.getId();

    User follower = new User();
    follower.setName("Test User");
    follower.setAge(25);
    userRepository.persist(follower);
    followerId = follower.getId();

  }

  @Test
  @DisplayName("Should return 409 when followerId is equal to User id")
  @Order(1)
  public void sameUserAsFollowerTest(){
    FollowerRequest followerRequest = new FollowerRequest();
    followerRequest.setFollowerId(userId);

    given()
      .contentType(ContentType.JSON)
      .pathParam("userId", userId)
      .body(followerRequest)
    .when()
      .put()
    .then()
      .statusCode(409);
  }

  @Test
  @DisplayName("Should return 404 on follow a user when userId does not exist")
  @Order(2)
  public void userNotFoundWhenTryingToFollowTest(){
    FollowerRequest followerRequest = new FollowerRequest();
    followerRequest.setFollowerId(userId);

    Long inexistentUserId = 999L; // Assuming this ID does not exist in the database

    given()
      .contentType(ContentType.JSON)
      .pathParam("userId", inexistentUserId)
      .body(followerRequest)
    .when()
      .put()
    .then()
      .statusCode(404);
  }


  @Test
  @DisplayName("Should follow a user")
  @Order(3)
  public void followUserTest(){

    FollowerRequest followerRequest = new FollowerRequest();
    followerRequest.setFollowerId(followerId);


    given()
      .contentType(ContentType.JSON)
      .pathParam("userId", userId)
      .body(followerRequest)
    .when()
      .put()
    .then()
      .statusCode(204);
  }

  @Test
  @DisplayName("Should return 404 on list user followeres and userId does not exist")
  @Order(4)
  public void userNotFoundWhenListingFollowersTest(){


    Long inexistentUserId = 999L; // Assuming this ID does not exist in the database

    given()
      .contentType(ContentType.JSON)
      .pathParam("userId", inexistentUserId)
    .when()
      .get()
    .then()
      .statusCode(404);
  }

  // @Test
  // @DisplayName("Should list a users followers")
  // @Transactional
  // @Order(5)
  // public void listingFollowersTest(){

  //   Follower follower = new Follower();
  //   follower.setUser(userRepository.findById(userId));
  //   follower.setFollower(userRepository.findById(followerId));
  //   followerRepository.persist(follower);
    
  //   var response = given()
  //     .contentType(ContentType.JSON)
  //     .pathParam("userId", userId)
  //   .when()
  //     .get()
  //   .then()
  //     .extract()
  //     .response();

  //   assertEquals(Response.Status.OK.getStatusCode(), response.statusCode());

  //   var followersCount = response.jsonPath().get("count");
  //   assertEquals(1, followersCount);
  // }

  @Test
  @DisplayName("Should return 404 when trying to unfollow a user and userId does not exist")
  public void userNotFoundWhenUnfollowingUserTest(){
    Long inexistentUserId = 999L; // Assuming this ID does not exist in the database

    given()
      .contentType(ContentType.JSON)
      .pathParam("userId", inexistentUserId)
      .queryParam("followerId", followerId)
    .when()
      .delete()
    .then()
      .statusCode(404);
  }


  @Test
  @DisplayName("Should unfollow a user")
  @Transactional
  public void unfollowUserTest(){

  Follower followerRelation = new Follower();
  followerRelation.setUser(userRepository.findById(userId));
  followerRelation.setFollower(userRepository.findById(followerId));
  followerRepository.persist(followerRelation);

    given()
      .contentType(ContentType.JSON)
      .pathParam("userId", userId)
      .queryParam("followerId", followerId)
    .when()
      .delete()
    .then()
      .statusCode(204);
  }
}
