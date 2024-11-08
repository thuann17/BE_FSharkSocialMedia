package com.system.fsharksocialmedia.repositories;

import com.system.fsharksocialmedia.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    @Query(value = "EXEC GetUsersWithoutFriends :username", nativeQuery = true)
    List<User> findUsersWithoutFriends(@Param("username") String username);
}