package com.example.backendbatm.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_m_user")
public class User {
    @Id
    Integer id;
    String password;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    @OneToOne
    @JoinColumn(name = "id")
    private Employee employee;

    public User() {
    }

    public User(Integer id, String password, Role role, Employee employee) {
        this.id = id;
        this.password = password;
        this.role = role;
        this.employee = employee;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }



}
