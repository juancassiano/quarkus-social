package com.github.juancassiano.quarkussocial.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreatePostRequest {
  
    @NotBlank(message = "Post text cannot be blank")
    private String post_text;
}
