package com.system.fsharksocialmedia.repositories;

import com.system.fsharksocialmedia.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}