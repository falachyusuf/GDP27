package com.example.backendbatm.controllers.API;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backendbatm.DTO.ChangeDTO;
import com.example.backendbatm.DTO.LoginDTO;
import com.example.backendbatm.DTO.RegisterRestDTO;
import com.example.backendbatm.config.MyUserDetails;
import com.example.backendbatm.config.jwt.JwtTokenUtil;
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

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private MyUserDetails myUserDetails;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @PostMapping("auth/register")
  public ResponseEntity<Object> register(@RequestBody RegisterRestDTO register) {
    try {
      String name = register.getName();
      String email = register.getEmail();
      String password = register.getPassword();
      String confPassword = register.getConfPassword();
      Integer roleId = register.getRoleId();
      Employee employeeExist = employeeRepository.findEmpByEmail(email);
      if (employeeExist != null) {
        return CustomResponse.generate(HttpStatus.CONFLICT, "Employee already exists");
      }
      if (!password.equals(confPassword)) {
        return CustomResponse.generate(HttpStatus.BAD_REQUEST, "Confirm Password is incorrect");
      }
      Role role = roleRepository.findById(roleId).orElse(null);
      if (role == null) {
        return CustomResponse.generate(HttpStatus.BAD_REQUEST, "Role not found");
      }
      Employee employee = new Employee();
      employee.setName(name);
      employee.setEmail(email);
      Employee employeeSaved = employeeRepository.save(employee);
      if (employeeSaved == null) {
        return CustomResponse.generate(HttpStatus.BAD_REQUEST, "Register failed");
      } else {
        User user = new User();
        user.setId(employeeSaved.getId());
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        userRepository.save(user);
      }
      return CustomResponse.generate(HttpStatus.OK, "Register successful");
    } catch (Exception e) {
      return CustomResponse.generate(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
    }
  }

  @PostMapping("auth/login")
  public ResponseEntity<Object> login(@RequestBody LoginDTO login) {
    Employee employee = employeeRepository.findEmpByEmail(login.getEmail());

    if (employee == null) {
      return CustomResponse.generate(HttpStatus.UNAUTHORIZED, "Email or Password Wrong!");
    }

    Optional<User> optional = userRepository.findById(employee.getId());

    if (optional.isEmpty()) {
      return CustomResponse.generate(HttpStatus.UNAUTHORIZED, "Email or Password Wrong!");
    }

    User user = optional.get();

    if (!passwordEncoder.matches(login.getPassword(), user.getPassword())) {
      return CustomResponse.generate(HttpStatus.UNAUTHORIZED, "Email or Password Wrong!");
    }

    Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));
    
    final UserDetails userDetails = myUserDetails.loadUserByUsername(login.getEmail());
    final String token = jwtTokenUtil.generateToken(userDetails);
    return CustomResponse.generate(HttpStatus.OK, "Success Login", token);
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
      return CustomResponse.generate(HttpStatus.UNAUTHORIZED, "Email doesn't exist");
    }

    if (!passwordEncoder.matches(oldPassword, userById.getPassword())) {
      return CustomResponse.generate(HttpStatus.UNAUTHORIZED, "Old password is invalid");
    }

    userById.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(userById);
    return CustomResponse.generate(HttpStatus.OK, "Password is changed");
  }

  @PutMapping("auth/forgot-password")
  public ResponseEntity<Object> forgotPassword(@RequestBody LoginDTO login) {
    String newPassword = login.getPassword();
    Employee employee = employeeRepository.findEmpByEmail(login.getEmail());

    if (employee == null) {
      return CustomResponse.generate(HttpStatus.BAD_REQUEST, "Data Not Found");
    }

    if (newPassword != "") {
      employee.getUser().setPassword(passwordEncoder.encode(newPassword));
      employeeRepository.save(employee);
      return CustomResponse.generate(HttpStatus.OK, "Data Successfully Updated");
    } else
      return CustomResponse.generate(HttpStatus.BAD_REQUEST, "Empty Password Field");
  }
}