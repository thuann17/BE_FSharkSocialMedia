package com.system.fsharksocialmedia.repositories;

import com.system.fsharksocialmedia.entities.Likepost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikepostRepository extends JpaRepository<Likepost, Integer> {
}