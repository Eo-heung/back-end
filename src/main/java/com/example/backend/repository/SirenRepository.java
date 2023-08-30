package com.example.backend.repository;

import com.example.backend.entity.Siren;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SirenRepository extends JpaRepository<Siren,Integer> {
}


