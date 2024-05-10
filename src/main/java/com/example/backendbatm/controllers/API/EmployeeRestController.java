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

import com.example.backendbatm.model.Employee;
import com.example.backendbatm.repository.EmployeeRepository;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("api")
public class EmployeeRestController {
    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("employee")
    public List<Employee> get() {
        return employeeRepository.findAll();
    }

    @GetMapping("employee/{id}")
    public Employee get(@PathVariable(required = true) Integer id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @PostMapping("employee")
    public boolean save(@RequestBody Employee employee) {
        Employee result = employeeRepository.save(employee);
        return employeeRepository.findById(result.getId()).isPresent();
    }

    @DeleteMapping("employee/{id}")
    public boolean delete(@PathVariable(required = true) Integer id) {
        employeeRepository.deleteById(id);
        return employeeRepository.findById(id).isEmpty();
    }

    @PutMapping("employee/{id}")
    public boolean edit(@PathVariable(required = true) Integer id, @RequestBody Employee employee) {
        Employee employeeData = employeeRepository.findById(id).orElse(null);

        if(employeeData == null){
            return false;
        }

        if (employee.getEmail() != null && !employee.getEmail().equals("")) {
            employeeData.setEmail(employee.getEmail());
        }

        if(employee.getName() != null && !employee.getName().equals("")){
            employeeData.setName(employee.getName());
        }

        employeeRepository.save(employeeData);
        return true;
    }
}