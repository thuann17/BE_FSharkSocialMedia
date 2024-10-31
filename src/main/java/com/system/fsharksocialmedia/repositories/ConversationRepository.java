package com.system.fsharksocialmedia.repositories;

import com.system.fsharksocialmedia.entities.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, Integer> {
}