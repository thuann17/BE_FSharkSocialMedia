package com.system.fsharksocialmedia.repositories;

import com.system.fsharksocialmedia.entities.Post;
import com.system.fsharksocialmedia.entities.Postimage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostimageRepository extends JpaRepository<Postimage, Integer> {
    List<Postimage> findByPostid(Post postid);

    void deleteByPostid(Post post);
}