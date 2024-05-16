package com.example.registrationms.respository;

import com.example.registrationms.model.User;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Primary
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}