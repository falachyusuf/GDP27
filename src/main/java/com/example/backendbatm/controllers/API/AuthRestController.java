package com.example.backendbatm.controllers.API;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backendbatm.DTO.ChangeDTO;
import com.example.backendbatm.DTO.LoginDTO;
import com.example.backendbatm.model.Employee;
import com.example.backendbatm.model.User;
import com.example.backendbatm.repository.EmployeeRepository;
import com.example.backendbatm.repository.UserRepository;

@RestController
@RequestMapping("api")
public class AuthRestController {

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @PostMapping("auth/login")
  public boolean login(@RequestBody LoginDTO login) {
    Employee employee = employeeRepository.findEmpByEmail(login.getEmail());

    if (employee == null) {
      return false;
    }

    Optional<User> optional = userRepository.findById(employee.getId());

    if (optional.isEmpty()) {
      return false;
    }

    User user = optional.get();

    if (!passwordEncoder.matches(login.getPassword(), user.getPassword())) {
      return false;
    }

    return true;
  }

  @PostMapping("auth/change-password")
  public boolean changePassword(@RequestBody ChangeDTO changeDTO) {
    String email = changeDTO.getEmail();
    String oldPassword = changeDTO.getOldPassword();
    String newPassword = changeDTO.getNewPassword();
    Employee employee = employeeRepository.findEmpByEmail(email);
    User user = userRepository.findById(employee.getId()).get();
    if (passwordEncoder.matches(oldPassword, user.getPassword())) {
      user.setPassword(passwordEncoder.encode(newPassword));
      userRepository.save(user);
      return true;
    }
    return false;
  }

  @PutMapping("auth/forgot-password")
  public boolean forgotPassword(@RequestBody LoginDTO login) {
    String newPassword = login.getPassword();
    Employee employee = employeeRepository.findEmpByEmail(login.getEmail());

    if (employee == null) {
      return false;
    }

    if (newPassword != "") {
      employee.getUser().setPassword(passwordEncoder.encode(newPassword));
      employeeRepository.save(employee);
      return true;
    }

    return false;
  }
}