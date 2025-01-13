package com.system.fsharksocialmedia.repositories;

import com.system.fsharksocialmedia.entities.Comment;
import com.system.fsharksocialmedia.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByPostId(Integer postId);

    void deleteByPost(Post post);

}