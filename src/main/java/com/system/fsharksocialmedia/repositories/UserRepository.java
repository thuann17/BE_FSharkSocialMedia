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

    User findByEmail(String email);;

    Page<User> findByUsernameContainingIgnoreCaseAndActiveAndRoles_Role(String username, Boolean active, String role, Pageable pageable);

    Page<User> findByActiveAndRoles_Role(Boolean active, String role, Pageable pageable);

    Page<User> findByRoles_Role(String role, Pageable pageable);

    Page<User> findByActive(Boolean active, Pageable pageable);

    Page<User> findByUsernameContainingIgnoreCaseAndActive(String username, Boolean active, Pageable pageable);
    Page<User> findByUsernameContainingIgnoreCaseAndRoles_Role(String username, String role, Pageable pageable);
    Page<User> findByUsernameContainingIgnoreCaseAndActiveAndRoles_RoleAndUsernameNot(String search, Boolean active, String role, String excludedUsername, Pageable pageable);

    Page<User> findByUsernameContainingIgnoreCaseAndActiveAndUsernameNot(String search, Boolean active, String excludedUsername, Pageable pageable);

    Page<User> findByUsernameContainingIgnoreCaseAndRoles_RoleAndUsernameNot(String search, String role, String excludedUsername, Pageable pageable);

    Page<User> findByUsernameContainingIgnoreCaseAndUsernameNot(String search, String excludedUsername, Pageable pageable);

    Page<User> findByActiveAndRoles_RoleAndUsernameNot(Boolean active, String role, String excludedUsername, Pageable pageable);

    Page<User> findByActiveAndUsernameNot(Boolean active, String excludedUsername, Pageable pageable);

    Page<User> findByRoles_RoleAndUsernameNot(String role, String excludedUsername, Pageable pageable);

    Page<User> findAllByUsernameNot(String excludedUsername, Pageable pageable);


}