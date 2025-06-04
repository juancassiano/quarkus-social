package com.github.juancassiano.quarkussocial.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.github.juancassiano.quarkussocial.rest.dto.CreateUserRequest;
import com.github.juancassiano.quarkussocial.rest.dto.ResponseError;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URL;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserResourceTest {

  @TestHTTPResource("/users")
  URL apiUrl;
  
  @Test()
  @DisplayName("Should create a user successfully")
  @Order(1)
  public void testCreateUser() {
    CreateUserRequest user = new CreateUserRequest();
    user.setName("John Doe");
    user.setAge(30);

    // Jsonb Jsonb = JsonbBuilder.create();
    // String userJson = Jsonb.toJson(user);

    Response response =
        given()
          .contentType(ContentType.JSON)
          .body(user)
        .when()
          .post(apiUrl)
        .then()
          .extract()
          .response();

    assertEquals(201, response.getStatusCode(), "Expected status code 201 for successful user creation");
    assertNotNull(response.jsonPath().getString("id"), "Expected user ID to be present in the response");
    assertEquals("John Doe", response.jsonPath().getString("name"), "Expected user name to match");
    assertEquals(30, response.jsonPath().getInt("age"), "Expected user age to match");

  }

  @Test()
  @DisplayName("Should return 422 for invalid user creation request")
  @Order(2)
  public void testCreateUserInvalidRequest() {
    CreateUserRequest user = new CreateUserRequest();
    user.setName(""); // Invalid name
    user.setAge(null); // Invalid age

    Response response =
        given()
          .contentType(ContentType.JSON)
          .body(user)
        .when()
          .post(apiUrl)
        .then()
          .extract()
          .response();

    assertEquals(ResponseError.UNPROCESSABLE_ENTITY_STATUS, response.statusCode(), "Expected status code 422 for invalid user creation request");
    assertEquals("Validation error",response.jsonPath().getString("message"));

    List<Map<String, String>> errors = response.jsonPath().getList("errors");

    assertNotNull(errors.get(0).get("message"));
    assertEquals("Name cannot be blank", errors.get(0).get("message"));
  }


  @Test()
  @DisplayName("Should list all users")
  @Order(3)
  public void testListAllUsers() {

        given()
          .contentType(ContentType.JSON)
        .when()
          .get(apiUrl)
        .then()
          .statusCode(200)
          .body("size()", Matchers.greaterThan(0));

  }

}
