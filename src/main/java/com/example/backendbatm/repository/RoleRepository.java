package com.example.backendbatm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backendbatm.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

}
