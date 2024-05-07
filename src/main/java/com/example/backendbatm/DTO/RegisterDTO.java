package com.example.backendbatm.DTO;

import com.example.backendbatm.model.Role;

public class RegisterDTO {
    private String name;
    private String email;
    private String password;
    private String confPassword;
    private Role role;

    public RegisterDTO() {
    }

    public RegisterDTO(String name, String email, String password, String confPassword) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.confPassword = confPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfPassword() {
        return confPassword;
    }

    public void setConfPassword(String confPassword) {
        this.confPassword = confPassword;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


}
