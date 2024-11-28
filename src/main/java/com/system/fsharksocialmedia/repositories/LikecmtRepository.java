package com.system.fsharksocialmedia.repositories;

import com.system.fsharksocialmedia.entities.Likecmt;
import com.system.fsharksocialmedia.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

public interface LikecmtRepository extends JpaRepository<Likecmt, Integer> {

    @Procedure(name = "GetLikeCountByCommentId")
    int getLikeCountByCommentId(@Param("CommentId") Integer commentId);

    boolean existsByCommentIdAndUsername(Integer commentId, User username);
}