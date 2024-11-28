package com.system.fsharksocialmedia.repositories;

import com.system.fsharksocialmedia.dtos.PostDto;
import com.system.fsharksocialmedia.entities.Likepost;
import com.system.fsharksocialmedia.entities.Post;
import com.system.fsharksocialmedia.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikepostRepository extends JpaRepository<Likepost, Integer> {

    Long countByPostId(Integer postId);

    Optional<Likepost> findByUsernameAndPost(User username, Post post);

    boolean existsByUsernameAndPostId(User username, Integer postId);

    Likepost findByUsernameAndPostId(User username, Integer postId);
}