package com.system.fsharksocialmedia.repositories;

import com.system.fsharksocialmedia.entities.Place;
import com.system.fsharksocialmedia.entities.Placetrip;
import com.system.fsharksocialmedia.entities.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlacetripRepository extends JpaRepository<Placetrip, Integer> {
    Placetrip findPlacetripByPlaceidAndTripid(Place placeid, Trip tripid);
}