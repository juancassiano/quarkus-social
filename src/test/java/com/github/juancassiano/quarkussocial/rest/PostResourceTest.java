package com.github.juancassiano.quarkussocial.rest;

import static io.restassured.RestAssured.given;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.juancassiano.quarkussocial.domain.model.User;
import com.github.juancassiano.quarkussocial.domain.repository.UserRepository;
import com.github.juancassiano.quarkussocial.rest.dto.CreatePostRequest;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@QuarkusTest
@TestHTTPEndpoint(PostResource.class)
class PostResourceTest {

  @Inject
  UserRepository userRepository;
  Long userId;

  @BeforeEach
  @Transactional
  public void setUp(){
    User user = new User();
    user.setName("Test User");
    user.setAge(25);
    userRepository.persist(user);
    userId = user.getId();
  }

  @Test
  @DisplayName("Should create a post successfully")
  public void createPostTest(){
    CreatePostRequest postRequest = new CreatePostRequest();
    postRequest.setPost_text("This is a test post");
    userId = 1L;

    
    given()
      .contentType(ContentType.JSON)
      .body(postRequest)
      .pathParam("userId", userId)
    .when()
      .post()
    .then()
      .statusCode(201);
  }
  
  @Test
  @DisplayName("Should return 404 when user does not exist")
  public void createPostUserNotFoundTest() {
    CreatePostRequest postRequest = new CreatePostRequest();
    postRequest.setPost_text("This is a test post");
    Long nonExistentUserId = 999L;

    given()
      .contentType(ContentType.JSON)
      .body(postRequest)
      .pathParam("userId", nonExistentUserId)
    .when()
      .post()
    .then()
      .statusCode(404);
  }

  @Test
  @DisplayName("Should return 404 when user does not exist")
  public void listPostUserNotFoundTest(){
    Long inexistentUserId = 999L;

    given()
      .pathParam("userId", inexistentUserId)
    .when()
      .get()
    .then()
      .statusCode(404);
  }

  @Test
  @DisplayName("Should return 400 when followerId header is not present")
  public void listPostFollowerHeaderNotSendTest(){
    
  }

  @Test
  @DisplayName("Should return 400 when follower does not exist")
  public void listPostFollowerHeaderNotExistTest(){}

  @Test
  @DisplayName("Should return 403 when follower is not following the user")
  public void listPostNotAFollowerTest() {}

  @Test
  @DisplayName("Should return a list of posts when follower is following the user")
  public void listPostTest(){}
}