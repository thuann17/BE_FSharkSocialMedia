package com.system.fsharksocialmedia.repositories;

import com.system.fsharksocialmedia.entities.Friend;
import com.system.fsharksocialmedia.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Integer> {
    @Query("SELECT fr FROM Friend fr WHERE (fr.userTarget = :user OR fr.userSrc = :user) AND fr.status = true")
    List<Friend> findByUserAndStatus(@Param("user") User user);

    @Query("SELECT fr FROM Friend fr WHERE fr.userTarget = :userTarget AND fr.status =  false")
    List<Friend> findByUserSrcAndStatusFollow(@Param("userTarget") User userSrc);

    @Query(value = "EXEC GetFriendsByUsername :username", nativeQuery = true)
    List<Object[]> findFriendNamesByUsername(@Param("username") String username);

    @Query(value = "CALL GetMutualFriends(:username1, :username2)", nativeQuery = true)
    List<Object[]> findMutualFriends(@Param("username1") String username1, @Param("username2") String username2);

    @Query(value = "EXEC GetFollowerByUsername :username", nativeQuery = true)
    List<Object[]> findFollowersByUsername(@Param("username") String username);
<<<<<<< Updated upstream
=======

    boolean existsByUserSrcAndUserTarget(User userSrc, User userTarget);
>>>>>>> Stashed changes
}
