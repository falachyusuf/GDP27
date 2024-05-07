package com.example.backendbatm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.backendbatm.repository.UserRepository;

@Controller
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    private String getAllUser(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "api/v1/users";
    }
}
