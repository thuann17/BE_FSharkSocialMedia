package com.system.fsharksocialmedia.repositories;

import com.system.fsharksocialmedia.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}