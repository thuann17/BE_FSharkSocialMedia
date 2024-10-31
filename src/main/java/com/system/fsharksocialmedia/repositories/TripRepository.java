package com.system.fsharksocialmedia.repositories;

import com.system.fsharksocialmedia.entities.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trip, Integer> {
}