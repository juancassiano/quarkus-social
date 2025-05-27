package com.github.juancassiano.quarkussocial.domain.repository;

import java.util.List;
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
  
  public List<Follower> findByUser(Long userId) {
    return find("user.id", userId).list();
  }

  public void deleteByFollowerAndUser(Long followerId, Long userId) {
    Map<String, Object> params = Parameters.with("followerId", followerId)
        .and("userId", userId).map();

    delete("follower.id =:followerId and user.id =:userId", params);
  }
}
