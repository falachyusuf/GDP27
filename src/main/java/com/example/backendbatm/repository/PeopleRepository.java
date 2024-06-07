package com.example.backendbatm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backendbatm.model.People;

@Repository
public interface PeopleRepository extends JpaRepository<People, Integer> {

}
