package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.Visit.Visit;

public interface VisitRepository extends JpaRepository<Visit, Long>{
    List<Visit> findByPropertyId(Long propertyId);
    
    List<Visit> findByUser_Email(String email);
}
