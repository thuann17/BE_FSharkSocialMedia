package com.system.fsharksocialmedia.repositories;

import com.system.fsharksocialmedia.entities.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Integer> {
    @Procedure(procedureName = "GetPlaceDetailsWithImages")
    List<Object[]> getPlaceDetailsWithImages();
}