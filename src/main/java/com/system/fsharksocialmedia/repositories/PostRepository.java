package com.system.fsharksocialmedia.repositories;

import com.system.fsharksocialmedia.entities.Post;
import com.system.fsharksocialmedia.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    Page<Post> findByContentContainingIgnoreCase(String title, Pageable pageable);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId")
    long countCmtByPost(Integer postId);

    @Query("SELECT COUNT(c) FROM Likepost c WHERE c.post.id = :postId")
    long countLikeByPost(Integer postId);

    List<Post> findByUsername(User user);

    @Procedure(procedureName = "GetPostsWithUserDetails")
    List<Object[]> getPostsWithUserDetails(@Param("inputUsername") String username);

    List<Post> findAllByUsernameIn(List<User> usernames);
    Page<Post> findByStatus(Boolean status, Pageable pageable);
    Page<Post> findByContentContainingIgnoreCaseAndStatus(String search, Boolean status, Pageable pageable);

}