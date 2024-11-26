package com.system.fsharksocialmedia.repositories;

import com.system.fsharksocialmedia.entities.Usertrip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsertripRepository extends JpaRepository<Usertrip, Integer> {
    List<Usertrip> findByUserid_Username(String username);
}