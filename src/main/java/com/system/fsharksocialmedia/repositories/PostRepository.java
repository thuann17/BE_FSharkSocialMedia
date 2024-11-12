package com.system.fsharksocialmedia.repositories;

import com.system.fsharksocialmedia.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< Updated upstream
=======
import org.springframework.data.jpa.repository.Query;
>>>>>>> Stashed changes
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    @Procedure(procedureName = "GetPostsWithUserDetails")
    List<Object[]> getPostsWithUserDetails(@Param("inputUsername") String username);

    @Procedure(procedureName = "GetPostsWithUserDetails")
    List<Object[]> getPostsWithUserDetails(@Param("inputUsername") String username);

}