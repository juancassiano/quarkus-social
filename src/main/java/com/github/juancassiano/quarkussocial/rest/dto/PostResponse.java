package com.github.juancassiano.quarkussocial.rest.dto;

import java.time.LocalDateTime;

import com.github.juancassiano.quarkussocial.domain.model.Post;

import lombok.Data;

@Data
public class PostResponse {
  private String post_text;
  private LocalDateTime dateTime;

  public static PostResponse fromEntity(Post post) {
    PostResponse postResponse = new PostResponse();
    postResponse.setPost_text(post.getText());
    postResponse.setDateTime(post.getDataTime());
    return postResponse;
  }
}
