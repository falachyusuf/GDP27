package com.example.backendbatm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backendbatm.model.Region;

@Repository
public interface RegionRepository extends JpaRepository<Region, Integer> {

}
