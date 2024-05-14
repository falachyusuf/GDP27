package com.example.backendbatm.controllers.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backendbatm.DTO.DepartmentDTO;
import com.example.backendbatm.handler.CustomResponse;
import com.example.backendbatm.model.Department;
import com.example.backendbatm.model.Region;
import com.example.backendbatm.repository.DepartmentRepository;
import com.example.backendbatm.repository.RegionRepository;

@RestController
@RequestMapping("api")
public class DepartmentRestController {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private RegionRepository regionRepository;

    @GetMapping("departments")
    public ResponseEntity<Object> getAll() {
        return CustomResponse.generate(HttpStatus.OK, "Data is retrieved", departmentRepository.findAll());
    }

    @GetMapping("departments/{id}")
    public ResponseEntity<Object> getById(@PathVariable(required = true) Integer id) {
        Department departmentById = departmentRepository.findById(id).orElse(null);

        try {
            if (!departmentById.equals(null)) {
                return CustomResponse.generate(HttpStatus.OK, "Data is exist", departmentById);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return CustomResponse.generate(HttpStatus.NOT_FOUND, "Data is not exist");
    }

    @PostMapping("departments")
    public ResponseEntity<Object> save(@RequestBody DepartmentDTO departmentDTO) {
        Department newDepartment = new Department();
        newDepartment.setName(departmentDTO.getName());

        Region region = regionRepository.findById(departmentDTO.getRegionId()).get();
        newDepartment.setRegion(region);

        departmentRepository.save(newDepartment);

        return CustomResponse.generate(HttpStatus.CREATED, "Data is created");
    }

    @PatchMapping("departments/{id}")
    public ResponseEntity<Object> updateById(@PathVariable(required = true) Integer id,
            @RequestBody DepartmentDTO departmentDTO) {
        Department departmentById = departmentRepository.findById(id).orElse(null);

        try {
            if (!departmentById.equals(null)
                    && (!departmentDTO.getName().equals(null) && departmentDTO.getName().length() != 0)) {
                departmentById.setName(departmentDTO.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (!departmentById.equals(null) && !departmentDTO.getRegionId().equals(null)) {
                Region region = regionRepository.findById(departmentDTO.getRegionId()).get();
                departmentById.setRegion(region);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (!departmentById.equals(null)) {
                departmentRepository.save(departmentById);
                return CustomResponse.generate(HttpStatus.OK, "Data is updated");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return CustomResponse.generate(HttpStatus.NOT_FOUND, "Data is not exist, update is failed");
    }

    @DeleteMapping("departments/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(required = true) Integer id) {
        try {
            Department departmentById = departmentRepository.findById(id).orElse(null);
            if (!departmentById.equals(null)) {
                departmentRepository.deleteById(id);
            }

            return CustomResponse.generate(HttpStatus.OK, "Data is deleted");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return CustomResponse.generate(HttpStatus.NOT_FOUND, "Data is not exist, delete is failed");
    }
}
