package com.system.fsharksocialmedia.repositories;

import com.system.fsharksocialmedia.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Integer> {
}