package com.github.juancassiano.quarkussocial.domain.repository;

import com.github.juancassiano.quarkussocial.domain.model.User;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
    
  
}
