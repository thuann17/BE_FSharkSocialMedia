package com.system.fsharksocialmedia.repositories;

import com.system.fsharksocialmedia.entities.Friend;
import com.system.fsharksocialmedia.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Integer> {
    @Query("SELECT fr FROM Friend fr WHERE (fr.userTarget = :user OR fr.userSrc = :user) AND fr.status = true")
    List<Friend> findByUserAndStatus(@Param("user") User user);

    @Query("SELECT fr FROM Friend fr WHERE fr.userTarget = :userTarget AND fr.status =  false")
    List<Friend> findByUserSrcAndStatusFollow(@Param("userTarget") User userSrc);

    @Query(value = "EXEC GetFriendsByUsername :username", nativeQuery = true)
    List<Object[]> findFriendNamesByUsername(@Param("username") String username);
}
