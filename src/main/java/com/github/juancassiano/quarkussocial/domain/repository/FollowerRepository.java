package com.github.juancassiano.quarkussocial.domain.repository;

import java.util.Map;

import com.github.juancassiano.quarkussocial.domain.model.Follower;
import com.github.juancassiano.quarkussocial.domain.model.User;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<Follower> {

  public boolean follows(User follower, User user){
    Map<String, Object> params = Map.of("follower", follower, "user", user);

    Parameters.with("follower", follower)
        .and("user", user).map();
        
    return find("follower =:follower and user =:user", params).firstResultOptional().isPresent();
  }
  
}
