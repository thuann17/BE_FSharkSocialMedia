package com.system.fsharksocialmedia.repositories;

import com.system.fsharksocialmedia.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Integer> {
}