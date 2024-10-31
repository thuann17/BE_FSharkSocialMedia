package com.system.fsharksocialmedia.repositories;

import com.system.fsharksocialmedia.entities.Postimage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostimageRepository extends JpaRepository<Postimage, Integer> {
}