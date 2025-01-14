package com.system.fsharksocialmedia.repositories;

import com.system.fsharksocialmedia.entities.Friend;
import com.system.fsharksocialmedia.entities.Post;
import com.system.fsharksocialmedia.entities.Share;
import com.system.fsharksocialmedia.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShareRepository extends JpaRepository<Share, Integer> {
    void deleteByPost(Post post);

    @Query(value = "EXEC GetSharesByUsername :username", nativeQuery = true)
    List<Object[]> getSharesByUsername(@Param("username") String username);

}