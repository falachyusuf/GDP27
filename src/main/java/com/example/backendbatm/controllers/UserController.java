package com.example.backendbatm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.backendbatm.model.User;
import com.example.backendbatm.repository.RoleRepository;
import com.example.backendbatm.repository.UserRepository;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

     @GetMapping
    public String index(Model model){
        model.addAttribute("users", userRepository.findAll());
        return "user/index";
    }

    @GetMapping(value = {"/form", "form/{id}"})
    public String form(Model model, @PathVariable(required = false) Integer id){
        model.addAttribute("roles", roleRepository.findAll());
        if(id != null){
            model.addAttribute("user", userRepository.findById(id));
        }else{
            model.addAttribute("user", new User());
        }
        return "user/form";
    }

    @PostMapping("save")
    public String form(User user){
        userRepository.save(user);
        return "redirect:/users";
    }

    @PostMapping("delete/{id}")
	public String deleteUser(@PathVariable(required = true) Integer id) {
		userRepository.deleteById(id);
		return "redirect:/users";
	}
}
