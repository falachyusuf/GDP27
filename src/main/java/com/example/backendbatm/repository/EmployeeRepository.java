package com.example.backendbatm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.backendbatm.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    @Query(value = "SELECT * FROM tb_m_employee WHERE email = ?1", nativeQuery = true)
    public Employee findByEmail(String name);
}
