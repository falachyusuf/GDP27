package com.example.backendbatm.controllers.API;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backendbatm.model.Role;
import com.example.backendbatm.repository.RoleRepository;

@RestController
@RequestMapping("api")
public class RoleRestController {
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("roles")
    public List<Role> get() {
        return roleRepository.findAll();
    }

    @GetMapping("roles/{id}")
    public Role getRoleById(@PathVariable(required = true) Integer id) {
        return roleRepository.findById(id).orElse(null);
    }

    @PostMapping("roles")
    public boolean save(@RequestBody Role role) {
        Role result = roleRepository.save(role);
        return roleRepository.findById(result.getId()).isPresent();
    }

    @DeleteMapping("roles/{id}")
    public boolean deleteById(@PathVariable(required = true) Integer id) {
        roleRepository.deleteById(id);
        return roleRepository.findById(id).isEmpty();
    }

    @PutMapping("role/{id}")
    public boolean updateById(@PathVariable(required = true) Integer id, @RequestBody Role role) {
        Role roleData = roleRepository.findById(id).orElse(null);

        if (roleData == null) {
            return false;
        }

        if (role.getName() != null && !role.getName().equals("")) {
            roleData.setName(role.getName());
        }

        roleRepository.save(roleData);
        return true;
    }
}
