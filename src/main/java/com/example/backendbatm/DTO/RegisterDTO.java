package com.example.backendbatm.DTO;

import com.example.backendbatm.model.Role;

public class RegisterDTO {
    private String name;
    private String email;
    private String password;
    private String confPassword;
    private Integer roleId;

    public RegisterDTO() {
    }

    public RegisterDTO(String name, String email, String password, String confPassword, Integer roleId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.confPassword = confPassword;
        this.roleId = roleId;
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

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    


}
