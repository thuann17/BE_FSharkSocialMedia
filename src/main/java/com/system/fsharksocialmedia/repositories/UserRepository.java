package com.system.fsharksocialmedia.repositories;

import com.system.fsharksocialmedia.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);

    Optional<User> findByUsername(String username);
        @Query(value = "EXEC GetUsersWithoutFriends :username", nativeQuery = true)
        List<User> findUsersWithoutFriends(@Param("username") String username);
    }