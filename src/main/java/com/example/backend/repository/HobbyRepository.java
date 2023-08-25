package com.example.backend.repository;

import com.example.backend.entity.Hobby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface HobbyRepository extends JpaRepository<Hobby, Integer > {



}
