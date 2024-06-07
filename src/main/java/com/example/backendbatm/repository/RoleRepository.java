package com.example.backendbatm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.backendbatm.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query(value = "SELECT id FROM tb_m_role WHERE level = (SELECT MAX(level) FROM tb_m_role)", nativeQuery=true)
    public Integer findRoleIdByLevel();
}
