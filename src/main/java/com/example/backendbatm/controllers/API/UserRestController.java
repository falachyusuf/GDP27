package com.example.backendbatm.controllers.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.example.backendbatm.handler.CustomResponse;
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
  public ResponseEntity<Object> getAll() {
    return CustomResponse.generate(HttpStatus.OK, "Data Retrieved", userRepository.findAll());
  }

  @GetMapping("users/{id}")
  public ResponseEntity<Object> getById(@PathVariable(required = true) Integer id) {
    return CustomResponse.generate(HttpStatus.OK, "Data Retrieved", userRepository.findById(id).orElse(null));
  }

  @PostMapping("users")
  public ResponseEntity<Object> save(@RequestBody User user) {
    User result = userRepository.save(user);
    if (userRepository.findById(result.getId()).isEmpty()) {
      return CustomResponse.generate(HttpStatus.BAD_REQUEST, "Failed Created User");
    }
    return CustomResponse.generate(HttpStatus.CREATED, "User Created");
  }

  @PutMapping("users/{id}")
  public ResponseEntity<Object> updateById(@PathVariable(required = true) Integer id,
      @RequestBody UserRequestDTO editData) {
    User userData = userRepository.findById(id).orElse(null);

    if (userData == null) {
      return CustomResponse.generate(HttpStatus.BAD_REQUEST, "Requested User Data is Not Found");
    }

    if (editData.getRoleId() != null) {
      Role role = roleRepository.findById(editData.getRoleId()).get();

      
      if (role == null) {
        return CustomResponse.generate(HttpStatus.BAD_REQUEST, "Role is Not Found");
      }
      
      userData.setRole(role);
    }

    if (editData.getPassword() != null) {
      if (!passwordEncoder.matches(editData.getPassword(), userData.getPassword())) {
        userData.setPassword(passwordEncoder.encode(editData.getPassword()));
      }
    }

    userRepository.save(userData);
    return CustomResponse.generate(HttpStatus.OK, "User Data Modified");
  }

  @DeleteMapping("users/{id}")
  public ResponseEntity<Object> deleteById(@PathVariable(required = true) Integer id) {
    userRepository.deleteById(id);
    if (!userRepository.findById(id).isEmpty()) {
      return CustomResponse.generate(HttpStatus.BAD_REQUEST, "Failed to Delete user Data");
    }
    return CustomResponse.generate(HttpStatus.OK, "Successfully delete User");
  }
}