package com.system.fsharksocialmedia.repositories;

import com.system.fsharksocialmedia.entities.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TripRepository extends JpaRepository<Trip, Integer> {
    @Query("SELECT COUNT(p) FROM Trip p WHERE " +
            "(:year IS NULL OR FUNCTION('YEAR', p.createdate) = :year) AND " +
            "(:month IS NULL OR FUNCTION('MONTH', p.createdate) = :month)")
    Integer countTripsByYearAndMonth(@Param("year") Integer year, @Param("month") Integer month);

}