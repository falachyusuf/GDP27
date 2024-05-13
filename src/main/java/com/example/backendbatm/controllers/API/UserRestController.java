package com.example.backendbatm.controllers.API;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backendbatm.DTO.UserRequestDTO;
import com.example.backendbatm.model.Role;
import com.example.backendbatm.model.User;
import com.example.backendbatm.repository.RoleRepository;
import com.example.backendbatm.repository.UserRepository;

@RestController
@RequestMapping("api")
public class UserRestController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @GetMapping("users")
  public List<User> getAll() {
    return userRepository.findAll();
  }

  @GetMapping("users/{id}")
  public User getById(@PathVariable(required = true) Integer id) {
    return userRepository.findById(id).orElse(null);
  }

  @PostMapping("users")
  public boolean save(@RequestBody User user) {
    User result = userRepository.save(user);
    return userRepository.findById(result.getId()).isPresent();
  }

  @PutMapping("users/{id}")
  public boolean updateById(@PathVariable(required = true) Integer id, @RequestBody UserRequestDTO editData) {
    User userData = userRepository.findById(id).orElse(null);

    if (userData == null) {
      return false;
    }

    if (editData.getRoleId() != null) {
      Role role = roleRepository.findById(editData.getRoleId()).get();

      if (role == null) {
        return false;
      }

      userData.setRole(role);
    }

    if (editData.getPassword() != null) {
      if (!passwordEncoder.matches(editData.getPassword(), userData.getPassword())) {
        userData.setPassword(passwordEncoder.encode(editData.getPassword()));
      }
    }

    userRepository.save(userData);
    return true;
  }

  @DeleteMapping("users/{id}")
  public boolean deleteById(@PathVariable(required = true) Integer id) {
    userRepository.deleteById(id);
    return userRepository.findById(id).isEmpty();
  }
}