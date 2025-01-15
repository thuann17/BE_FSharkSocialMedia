package com.system.fsharksocialmedia.repositories;

import com.system.fsharksocialmedia.entities.Triprole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.List;

public interface TriproleRepository extends JpaRepository<Triprole, Integer> {

}