package com.example.backendbatm.DTO;

public class UserRequestDTO {
  private String password;
  private Integer role_id;

  public UserRequestDTO() {
  }

  public UserRequestDTO(String password, Integer role_id) {
    this.password = password;
    this.role_id = role_id;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Integer getRole_id() {
    return role_id;
  }

  public void setRole_id(Integer role_id) {
    this.role_id = role_id;
  }
}
