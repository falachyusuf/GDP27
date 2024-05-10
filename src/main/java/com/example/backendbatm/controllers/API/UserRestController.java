package com.example.backendbatm.controllers.API;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backendbatm.model.User;
import com.example.backendbatm.repository.UserRepository;

@RestController
@RequestMapping("api/users")
public class UserRestController {

  @Autowired
  private UserRepository userRepository;

  @GetMapping
  public List<User> get() {
    return userRepository.findAll();
  }

  @GetMapping("/{id}")
  public User get(@PathVariable(required = true) Integer id) {
    return userRepository.findById(id).orElse(null);
  }

  @PostMapping
  public boolean save(@RequestBody User user) {
    User result = userRepository.save(user);
    return userRepository.findById(result.getId()).isPresent();
  }

  @DeleteMapping("/{id}")
  public boolean delete(@PathVariable(required=true) Integer id) {
    userRepository.deleteById(id);
    return userRepository.findById(id).isEmpty();
  }
}