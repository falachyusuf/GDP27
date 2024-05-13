package com.example.backendbatm.controllers.API;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backendbatm.DTO.ChangeDTO;
import com.example.backendbatm.DTO.LoginDTO;
import com.example.backendbatm.DTO.RegisterRestDTO;
import com.example.backendbatm.handler.CustomResponse;
import com.example.backendbatm.model.Employee;
import com.example.backendbatm.model.Role;
import com.example.backendbatm.model.User;
import com.example.backendbatm.repository.EmployeeRepository;
import com.example.backendbatm.repository.RoleRepository;
import com.example.backendbatm.repository.UserRepository;

@RestController
@RequestMapping("api")
public class AuthRestController {

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @PostMapping("auth/register")
  public boolean register(@RequestBody RegisterRestDTO register) {
    String name = register.getName();
    String email = register.getEmail();
    String password = register.getPassword();
    String confPassword = register.getConfPassword();
    Integer roleId = register.getRoleId();
    Employee employeeExist = employeeRepository.findEmpByEmail(email);
    if (employeeExist != null) {
      return false;
    }
    if (!password.equals(confPassword)) {
      return false;
    }
    Role role = roleRepository.findById(roleId).orElse(null);
    if (role == null) {
      return false;
    }
    Employee employee = new Employee();
    employee.setName(name);
    employee.setEmail(email);
    Employee employeeSaved = employeeRepository.save(employee);
    if (employeeSaved == null) {
      return false;
    } else {
      User user = new User();
      user.setId(employeeSaved.getId());
      user.setPassword(passwordEncoder.encode(password));
      user.setRole(role);
      userRepository.save(user);
    }
    return userRepository.findById(employeeSaved.getId()).isPresent();
  }

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
  public ResponseEntity<Object> changePassword(@RequestBody ChangeDTO changeDTO) {
    String email = changeDTO.getEmail();
    String oldPassword = changeDTO.getOldPassword();
    String newPassword = changeDTO.getNewPassword();
    Employee employeeByEmail = employeeRepository.findEmpByEmail(email);
    User userById = null;

    try {
      userById = userRepository.findById(employeeByEmail.getId()).orElse(null);
    } catch (Exception e) {
      e.printStackTrace();
      return CustomResponse.generate(HttpStatus.NOT_FOUND, "Email doesn't exist");
    }

    if (!passwordEncoder.matches(oldPassword, userById.getPassword())) {
      return CustomResponse.generate(HttpStatus.UNAUTHORIZED, "Old password and user password is not matches");
    }

    userById.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(userById);
    return CustomResponse.generate(HttpStatus.OK, "Password is changed");
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