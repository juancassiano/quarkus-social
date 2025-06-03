package com.github.juancassiano.quarkussocial.rest;

import static io.restassured.RestAssured.given;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.juancassiano.quarkussocial.domain.model.Follower;
import com.github.juancassiano.quarkussocial.domain.model.Post;
import com.github.juancassiano.quarkussocial.domain.model.User;
import com.github.juancassiano.quarkussocial.domain.repository.FollowerRepository;
import com.github.juancassiano.quarkussocial.domain.repository.PostRepository;
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
  @Inject
  FollowerRepository followerRepository;
  @Inject
  PostRepository postRepository;

  Long userId;
  Long userNotFollowerId;
  Long userFollowerId;

  @BeforeEach
  @Transactional
  public void setUp(){
    User user = new User();
    user.setName("Test User");
    user.setAge(25);
    userRepository.persist(user);
    userId = user.getId();

    User userNotFollower = new User();
    userNotFollower.setName("Test User Not Follower");
    userNotFollower.setAge(30);
    userRepository.persist(userNotFollower);
    userNotFollowerId = userNotFollower.getId();

    User userFollower = new User();
    userFollower.setName("Test User Not Follower");
    userFollower.setAge(30);
    userRepository.persist(userFollower);
    userFollowerId = userFollower.getId();

    Follower follower = new Follower();
    follower.setUser(user);
    follower.setFollower(userFollower);
    followerRepository.persist(follower);

    Post post = new Post();
    post.setText("This is a test post");
    post.setUser(user);
    postRepository.persist(post);
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

    given()
      .pathParam("userId", userId)
    .when()
      .get()
    .then()
      .statusCode(400)
      .body(Matchers.is("Follower ID must be provided"));
  }


  @Test
  @DisplayName("Should return 400 when follower does not exist")
  public void listPostFollowerNotFountTest(){
    Long inexistentFollowerId = 999L;

    given()
      .pathParam("userId", userId)
      .header("followerId", inexistentFollowerId)
    .when()
      .get()
    .then()
      .statusCode(400)
      .body(Matchers.is("Inexistent followerId"));
  }


  @Test
  @DisplayName("Should return 403 when follower is not following the user")
  public void listPostNotAFollowerTest() {
    given()
      .pathParam("userId", userId)
      .header("followerId", userNotFollowerId)
    .when()
      .get()
    .then()
      .statusCode(403)
      .body(Matchers.is("You must follow the user to see their posts"));
  }

  @Test
  @DisplayName("Should return a list of posts when follower is following the user")
  public void listPostTest(){

    given()
      .pathParam("userId", userId)
      .header("followerId", userFollowerId)
    .when()
      .get()
    .then()
      .statusCode(200)
      .body("size()", Matchers.is(1)); // Assuming there are posts for the user
  }
}