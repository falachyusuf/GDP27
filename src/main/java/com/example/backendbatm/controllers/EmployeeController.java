package com.example.backendbatm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.backendbatm.model.Employee;
import com.example.backendbatm.repository.EmployeeRepository;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

  @Autowired
  private EmployeeRepository employeeRepository;

  @GetMapping
  public String index(Model model) {
    model.addAttribute("employees", employeeRepository.findAll());
    return "employee/index";
  }

  @GetMapping(value = { "form", "form/{id}" })
  public String form(@PathVariable(required = false) Integer id, Model model) {
    if (id != null) {
      model.addAttribute("employee", employeeRepository.findById(id).get());
    } else {
      model.addAttribute("employee", new Employee());
    }
    return "employee/form";
  }

  @PostMapping("submit")
  public String insertEmployee(Employee employee) {
    try {
      employeeRepository.save(employee);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "redirect:/employee";
  }

  @PostMapping("delete/{id}")
  public String deleteEmployee(@PathVariable("id") Integer id) {
    try {
      employeeRepository.deleteById(id);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "redirect:/employee";
  }
}
