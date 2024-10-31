package com.system.fsharksocialmedia.repositories;

import com.system.fsharksocialmedia.entities.Groupmember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupmemberRepository extends JpaRepository<Groupmember, Integer> {
}