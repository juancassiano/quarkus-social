package com.github.juancassiano.quarkussocial.rest.dto;

import java.util.List;


import lombok.Data;

@Data
public class FollowerPerUserResponse {
  private Integer count;
  private List<FollowerResponse> content;
  
}
