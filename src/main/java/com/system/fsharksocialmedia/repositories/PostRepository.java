package com.system.fsharksocialmedia.repositories;

import com.system.fsharksocialmedia.entities.Post;
import com.system.fsharksocialmedia.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findByContentContainingIgnoreCase(String title, Pageable pageable);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId")
    long countCmtByPost(Integer postId);

    @Query("SELECT COUNT(c) FROM Likepost c WHERE c.post.id = :postId")
    long countLikeByPost(Integer postId);

    List<Post> findByUsername(User user);

}