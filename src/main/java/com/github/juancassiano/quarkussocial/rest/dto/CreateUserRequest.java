package com.github.juancassiano.quarkussocial.rest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public class CreateUserRequest {
  @NotBlank(message = "Name cannot be blank")
  private String name;

  @NotNull(message = "Age cannot be null")
  private Integer age;
  
  public String getName() {
    return name;
  }
  public Integer getAge() {
    return age;
  }
  public void setName(String name) {
    this.name = name;
  }
  public void setAge(Integer age) {
    this.age = age;
  }


  
}
