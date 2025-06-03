package com.github.juancassiano.quarkussocial.rest;

import org.junit.jupiter.api.Test;
import com.github.juancassiano.quarkussocial.rest.dto.CreateUserRequest;
import com.github.juancassiano.quarkussocial.rest.dto.ResponseError;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.DisplayName;

@QuarkusTest
public class UserResourceTest {
  
  @Test()
  @DisplayName("Should create a user successfully")
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
          .post("/users")
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
  public void testCreateUserInvalidRequest() {
    CreateUserRequest user = new CreateUserRequest();
    user.setName(""); // Invalid name
    user.setAge(null); // Invalid age

    Response response =
        given()
          .contentType(ContentType.JSON)
          .body(user)
        .when()
          .post("/users")
        .then()
          .extract()
          .response();

    assertEquals(ResponseError.UNPROCESSABLE_ENTITY_STATUS, response.statusCode(), "Expected status code 422 for invalid user creation request");
    assertEquals("Validation error",response.jsonPath().getString("message"));

    List<Map<String, String>> errors = response.jsonPath().getList("errors");

    assertNotNull(errors.get(0).get("message"));
    assertEquals("Age cannot be null", errors.get(0).get("message"));
    assertEquals("Name cannot be blank", errors.get(1).get("message"));
  }
}
