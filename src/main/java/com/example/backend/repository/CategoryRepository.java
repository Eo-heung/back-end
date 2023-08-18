package com.example.backend.repository;

import com.example.backend.entity.Category;
import com.example.backend.entity.Moim;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
