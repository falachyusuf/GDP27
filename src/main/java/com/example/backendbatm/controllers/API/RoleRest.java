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


import com.example.backendbatm.model.Role;
import com.example.backendbatm.repository.RoleRepository;

@RestController
@RequestMapping("api")
public class RoleRest {
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("role")
    public List<Role> get(){
        return roleRepository.findAll();
    }

    @GetMapping("role/{id}")
    public Role getRoleById(@PathVariable(required = true) Integer id){
        return roleRepository.findById(id).orElse(null);
    }

    @PostMapping("role")
    public boolean postRole(@RequestBody Role role){
        Role result = roleRepository.save(role);
        return roleRepository.findById(result.getId()).isPresent();
    }

    @DeleteMapping("role/{id}")
    public boolean deleteRole(@PathVariable(required = true) Integer id){
        roleRepository.deleteById(id);
        return roleRepository.findById(id).isEmpty();
    }
}
