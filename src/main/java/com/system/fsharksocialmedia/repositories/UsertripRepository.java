package com.system.fsharksocialmedia.repositories;

import com.system.fsharksocialmedia.entities.User;
import com.system.fsharksocialmedia.entities.Usertrip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsertripRepository extends JpaRepository<Usertrip, Integer> {
    List<Usertrip> findByUserid(User user);
}