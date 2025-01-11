package com.system.fsharksocialmedia.repositories;

import com.system.fsharksocialmedia.entities.Place;
import com.system.fsharksocialmedia.entities.Placeimage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceimageRepository extends JpaRepository<Placeimage, Integer> {
    List<Placeimage> findByPlaceid(Place placeid);
}