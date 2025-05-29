package com.github.juancassiano.quarkussocial.rest;

import org.junit.jupiter.api.Test;
import com.github.juancassiano.quarkussocial.rest.dto.CreateUserRequest;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import static org.junit.jupiter.api.Assertions.*;
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

}
