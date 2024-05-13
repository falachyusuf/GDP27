package com.example.backendbatm.controllers.API;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backendbatm.DTO.DepartmentDTO;
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
    public List<Department> getAll() {
        return departmentRepository.findAll();
    }

    @GetMapping("departments/{id}")
    public Department getById(@PathVariable(required = true) Integer id) {
        return departmentRepository.findById(id).orElse(null);
    }

    @PostMapping("departments")
    public boolean save(@RequestBody DepartmentDTO departmentDTO) {
        Department newDepartment = new Department();
        newDepartment.setName(departmentDTO.getName());

        Region region = regionRepository.findById(departmentDTO.getRegionId()).get();
        newDepartment.setRegion(region);

        Department result = departmentRepository.save(newDepartment);
        return departmentRepository.findById(result.getId()).isPresent();
    }

    @PatchMapping("departments/{id}")
    public boolean updateById(@PathVariable(required = true) Integer id, @RequestBody DepartmentDTO departmentDTO) {
        Department departmentById = departmentRepository.findById(id).orElse(null);

        if (departmentById == null) {
            return false;
        }

        try {
            if (!departmentDTO.getName().equals(null) && departmentDTO.getName().length() != 0) {
                departmentById.setName(departmentDTO.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (!departmentDTO.getRegionId().equals(null)) {
                Region region = regionRepository.findById(departmentDTO.getRegionId()).get();
                departmentById.setRegion(region);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Department updateDepartment = departmentRepository.save(departmentById);
        return departmentRepository.findById(updateDepartment.getId()).isPresent();
    }

    @DeleteMapping("departments/{id}")
    public boolean deleteById(@PathVariable(required = true) Integer id) {
        departmentRepository.deleteById(id);
        return departmentRepository.findById(id).isEmpty();
    }
}
