package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.offer.Offer;

public interface OfferRepository extends JpaRepository<Offer, Long>{
    List<Offer> findAllByProperty_Id(Long propertyId);

    List<Offer> findAllByUser_Id(Long userId);
}
