package com.system.fsharksocialmedia.repositories;

import com.system.fsharksocialmedia.dtos.PostDto;
import com.system.fsharksocialmedia.entities.Post;
import com.system.fsharksocialmedia.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Map;

public interface PostRepository extends JpaRepository<Post, Integer> {

    Page<Post> findByContentContainingIgnoreCase(String title, Pageable pageable);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId")
    long countCmtByPost(Integer postId);

    @Query("SELECT COUNT(c) FROM Likepost c WHERE c.post.id = :postId")
    long countLikeByPost(Integer postId);

    List<Post> findByUsername(User user);

    @Procedure(procedureName = "GetPostsWithUserDetails")
    List<Object[]> getPostsWithUserDetails(@Param("inputUsername") String username);

    List<Post> findAllByUsernameInAndStatusTrueOrderByCreatedateDesc(List<User> users);

    @Query("SELECT COUNT(p) FROM Post p WHERE " +
            "(:year IS NULL OR FUNCTION('YEAR', p.createdate) = :year) AND " +
            "(:month IS NULL OR FUNCTION('MONTH', p.createdate) = :month)")
    Integer countPostsByYearAndMonth(@Param("year") Integer year, @Param("month") Integer month);

    @Query(value = "EXEC GetUserPostCount :year, :month", nativeQuery = true)
    List<Map<String, Object>> getUserPostCount(Integer year, Integer month);

    @Query(value = "EXEC GetUserPostCountTop5 :year, :month", nativeQuery = true)
    List<Map<String, Object>> GetUserPostCountTop5(Integer year, Integer month);

}