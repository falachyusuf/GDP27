package com.example.backendbatm.DTO;

public class UserRequestDTO {
  private String password;
  private Integer roleId;

  public UserRequestDTO() {
  }

  public UserRequestDTO(String password, Integer roleId) {
    this.password = password;
    this.roleId = roleId;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Integer getRoleId() {
    return roleId;
  }

  public void setRoleId(Integer roleId) {
    this.roleId = roleId;
  }
}
