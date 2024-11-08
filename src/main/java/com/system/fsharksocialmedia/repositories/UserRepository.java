package com.system.fsharksocialmedia.repositories;

import com.system.fsharksocialmedia.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
    Optional<User> findByUsername(String username);
}