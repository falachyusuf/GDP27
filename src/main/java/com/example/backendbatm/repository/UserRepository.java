package com.example.backendbatm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backendbatm.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}
