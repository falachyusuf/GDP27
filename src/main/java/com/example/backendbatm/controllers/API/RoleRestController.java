package com.example.backendbatm.controllers.API;

// import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backendbatm.handler.CustomResponse;
import com.example.backendbatm.model.Role;
import com.example.backendbatm.repository.RoleRepository;

@RestController
@RequestMapping("api")
public class RoleRestController {
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("roles")
    public ResponseEntity<Object> get() {
        try {
            return CustomResponse.generate(HttpStatus.OK, "Data retrieved successfully", roleRepository.findAll());
        } catch (Exception e) {
            return CustomResponse.generate(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    @GetMapping("roles/{id}")
    public ResponseEntity<Object> getRoleById(@PathVariable(required = true) Integer id) {
        try {
            return CustomResponse.generate(HttpStatus.OK, "Data retrieved successfully", roleRepository.findById(id));
        } catch (Exception e) {
            return CustomResponse.generate(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    @PostMapping("roles")
    public ResponseEntity<Object> save(@RequestBody Role role) {
        try {
            Role result = roleRepository.save(role);
            if(result == null){
                return CustomResponse.generate(HttpStatus.BAD_REQUEST, "Data failed to post");
            }
            return CustomResponse.generate(HttpStatus.OK, "Data successfully saved");
        } catch (Exception e) {
            return CustomResponse.generate(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    @DeleteMapping("roles/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(required = true) Integer id) {
        try {
            roleRepository.deleteById(id);
            return CustomResponse.generate(HttpStatus.OK, "Data successfully deleted");
        } catch (Exception e) {
            return CustomResponse.generate(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    @PutMapping("role/{id}")
    public ResponseEntity<Object> updateById(@PathVariable(required = true) Integer id, @RequestBody Role role) {
        try {
            Role roleData = roleRepository.findById(id).orElse(null);

            if (roleData == null) {
                return CustomResponse.generate(null, null);
            }

            if (role.getName() != null && !role.getName().equals("")) {
                roleData.setName(role.getName());
            }

            roleRepository.save(roleData);
            return CustomResponse.generate(HttpStatus.OK, "Data successfully updated");
        } catch (Exception e) {
            return CustomResponse.generate(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }

    }
}
