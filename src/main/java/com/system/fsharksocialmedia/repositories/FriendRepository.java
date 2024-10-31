package com.system.fsharksocialmedia.repositories;

import com.system.fsharksocialmedia.entities.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Integer> {
}