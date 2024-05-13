package com.example.backendbatm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.backendbatm.DTO.ChangeDTO;
import com.example.backendbatm.DTO.ForgotDTO;
import com.example.backendbatm.DTO.LoginDTO;
import com.example.backendbatm.DTO.RegisterDTO;
import com.example.backendbatm.model.Employee;
import com.example.backendbatm.model.Role;
import com.example.backendbatm.model.User;
import com.example.backendbatm.repository.EmployeeRepository;
import com.example.backendbatm.repository.RoleRepository;
import com.example.backendbatm.repository.UserRepository;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String index() {
        return "home/index";
    }

    @GetMapping("register/form")
    private String registerForm(Model model) {
        model.addAttribute("register", new RegisterDTO());
        model.addAttribute("roleOptions", roleRepository.findAll());
        return "auth/register/form";
    }

    @PostMapping("register/submit")
    private String registerSubmit(RegisterDTO registerDTO) {
        String name = registerDTO.getName();
        String email = registerDTO.getEmail();
        String password = registerDTO.getPassword();
        String confPassword = registerDTO.getConfPassword();
        Role role = registerDTO.getRole();
        Employee employeeExist = employeeRepository.findEmpByEmail(email);
        if (employeeExist != null) {
            System.out.println("Employee already exist");
            return "redirect:/auth/register/form";
        }
        if (!password.equals(confPassword)) {
            System.out.println("Password not match");
            return "redirect:/auth/register/form";
        }

        Employee employee = new Employee();
        employee.setName(name);
        employee.setEmail(email);
        Employee employeeSaved = employeeRepository.save(employee);

        if (employeeSaved == null) {
            System.out.println("Employee data does not exist");
        } else {
            User user = new User();
            user.setId(employeeSaved.getId());
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(role);
            userRepository.save(user);
        }

        return "redirect:/auth/login/form";
    }

    @GetMapping("login/form")
    public String loginForm(Model model) {
        model.addAttribute("loginData", new LoginDTO());
        return "auth/login/form";
    }

    @PostMapping("login/submit")
    public String loginSubmit(LoginDTO login, Model model) throws Exception {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return "/home/index";
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @GetMapping("forgot-password/form")
    public String forgotPassword(Model model) {
        model.addAttribute("forgotPasswordDTO", new ForgotDTO());
        return "auth/forgot-password/form";
    }

    @PostMapping("forgot-password/submit")
    public String resetPassword(ForgotDTO forgotPassData) {
        String email = forgotPassData.getEmail();
        String newPassword = forgotPassData.getNewPassword();

        Employee employee = employeeRepository.findEmpByEmail(email);
        if (employee == null) {
            return "redirect:/auth/forgot-password/form";
        }

        if (newPassword != "") {
            employee.getUser().setPassword(passwordEncoder.encode(newPassword));
            employeeRepository.save(employee);
            return "redirect:/auth/login/form";
        }
        return "redirect:/auth/forgot-password/form";

    };

    @GetMapping("change/form")
    public String changeForm(Model model) {
        model.addAttribute("auth", new ChangeDTO());
        return "auth/change-password/form";
    }

    @PostMapping("change/submit")
    public String changeSubmit(ChangeDTO changeDTO) {
        String email = changeDTO.getEmail();
        String oldPassword = changeDTO.getOldPassword();
        String newPassword = changeDTO.getNewPassword();
        Employee employee = employeeRepository.findEmpByEmail(email);

        try {
            User user = userRepository.findById(employee.getId()).get();
            if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                return "redirect:/auth/login/form";
            } else {
                return "redirect:/auth/change/form";
            }
        } catch (Exception e) {
            System.out.println("Employee not found");
            e.printStackTrace();
            return "redirect:/auth/change/form";
        }
    }
}
