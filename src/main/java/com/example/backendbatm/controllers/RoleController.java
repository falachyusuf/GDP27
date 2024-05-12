package com.example.backendbatm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.backendbatm.model.Role;
import com.example.backendbatm.repository.RoleRepository;

@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping()
    private String getAllRole(Model model) {
        model.addAttribute("roles", roleRepository.findAll());
        return "role/index";
    }

    @GetMapping("/form")
    public String createRole(Model model) {
        model.addAttribute("role", new Role());
        return "role/form";
    }

    @GetMapping(value = { "form/post", "form/edit/{id}" })
    private String addRole(Model model, @PathVariable(required = false) Integer id) {
        if (id != null) {
            model.addAttribute("role", roleRepository.findById(id));
        } else {
            model.addAttribute("role", new Role());
        }
        model.addAttribute("regionOptions", roleRepository.findAll());
        return "role/form";
    }

    @PostMapping("submit")
    private String submitDepartment(Role role) {
        Role newRole = roleRepository.save(role);
        if (!newRole.equals(null)) {
            return "redirect:/role";
        }
        return "role/index";
    }

    @PostMapping("delete/{id}")
    public String deleteUser(@PathVariable(required = true) Integer id) {
        roleRepository.deleteById(id);
        return "redirect:/role";
    }
}
