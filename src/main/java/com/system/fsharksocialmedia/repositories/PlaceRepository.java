package com.system.fsharksocialmedia.repositories;

import com.system.fsharksocialmedia.entities.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Integer> {
    Page<Place> findByNameplaceContaining(String nameplace, Pageable pageable);
}