package com.example.backendbatm.DTO;

public class LoginResponseDTO {
  private String name;

  public LoginResponseDTO() {
  }

  public LoginResponseDTO(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
