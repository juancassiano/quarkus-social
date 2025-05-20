package com.github.juancassiano.quarkussocial.rest.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;


@Data
public class CreateUserRequest {
  @NotBlank(message = "Name cannot be blank")
  private String name;

  @NotNull(message = "Age cannot be null")
  private Integer age;
  
}
