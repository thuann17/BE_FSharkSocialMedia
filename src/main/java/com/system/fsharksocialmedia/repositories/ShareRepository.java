package com.system.fsharksocialmedia.repositories;

import com.system.fsharksocialmedia.entities.Share;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShareRepository extends JpaRepository<Share, Integer> {
}