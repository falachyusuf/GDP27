package com.example.backendbatm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.example.backendbatm.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "SELECT new com.example.backendbatm.config.MyUserDetails(e.email, u.password, r.name, e.name) FROM User u JOIN u.role r JOIN u.employee e WHERE e.email = ?1")
    public UserDetails loginNext(String email);
}
