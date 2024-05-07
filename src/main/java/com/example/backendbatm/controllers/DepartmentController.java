package com.example.backendbatm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.backendbatm.model.Department;
import com.example.backendbatm.repository.DepartmentRepository;
import com.example.backendbatm.repository.RegionRepository;

@Controller
@RequestMapping("/api/v1/department")
public class DepartmentController {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private RegionRepository regionRepository;

    @GetMapping()
    private String getDepartment(Model model) {
        model.addAttribute("departments", departmentRepository.findAll());
        return "department/index";
    }
    @GetMapping(value = {"form/post", "form/edit/{id}"})
    private String addDepartment(Model model, @PathVariable(required = false) Integer id){
        if(id != null){
            model.addAttribute("department", departmentRepository.findById(id));
        }else{
            model.addAttribute("department", new Department());
        }
        model.addAttribute("regionOptions", regionRepository.findAll());
        return "department/form";
    }
    
    @PostMapping("submit")
    private String submitDepartment(Department department){
        Department newDepartment = departmentRepository.save(department);
        if (newDepartment != null) {
            return "redirect:/api/v1/department";
        }
        return "department/index";
    }

}
