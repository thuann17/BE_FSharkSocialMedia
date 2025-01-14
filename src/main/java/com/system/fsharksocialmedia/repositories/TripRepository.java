package com.system.fsharksocialmedia.repositories;

import com.system.fsharksocialmedia.entities.Place;
import com.system.fsharksocialmedia.entities.Trip;
import com.system.fsharksocialmedia.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Integer> {
    @Query("SELECT t , p, ut FROM Trip t " +
            "JOIN t.usertrips ut " +
            "JOIN ut.userid u " +
            "JOIN t.placetrips pt " +
            "JOIN pt.placeid p " +
            "WHERE u.username = :username")
    List<Trip> findTripsByUsernameAndPlaceId(String username);
}