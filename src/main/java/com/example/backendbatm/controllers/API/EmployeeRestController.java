package com.example.backendbatm.controllers.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backendbatm.handler.CustomResponse;
import com.example.backendbatm.model.Employee;
import com.example.backendbatm.repository.EmployeeRepository;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("api")
public class EmployeeRestController {
    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("employee")
    public ResponseEntity<Object> getAll() {
        return CustomResponse.generate(HttpStatus.OK, "Data Successfully Retrieved", employeeRepository.findAll());
    }

    @GetMapping("employee/{id}")
    public ResponseEntity<Object> getById(@PathVariable(required = true) Integer id) {
        return CustomResponse.generate(HttpStatus.OK, "Data Successfully Retrieved",
                employeeRepository.findById(id).orElse(null));
    }

    @PostMapping("employee")
    public ResponseEntity<Object> save(@RequestBody Employee employee) {
        Employee result = employeeRepository.save(employee);
        if (employeeRepository.findById(result.getId()).isEmpty()) {
            return CustomResponse.generate(HttpStatus.BAD_REQUEST, "Data Unsuccessfully Created");
        } else {
            return CustomResponse.generate(HttpStatus.OK, "Data Successfully Created");
        }
    }

    @DeleteMapping("employee/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(required = true) Integer id) {
        employeeRepository.deleteById(id);
        if(employeeRepository.findById(id).isEmpty()){
            return CustomResponse.generate(HttpStatus.OK, "Data Successfully Deleted");
        } else return CustomResponse.generate(HttpStatus.BAD_REQUEST, "Data Unsuccessfully Deleted");
    }

    @PutMapping("employee/{id}")
    public ResponseEntity<Object> updateById(@PathVariable(required = true) Integer id,
            @RequestBody Employee employee) {
        Employee employeeData = employeeRepository.findById(id).orElse(null);

        if (employeeData == null) {
            return CustomResponse.generate(HttpStatus.BAD_REQUEST, "Data Not Found");
        }

        if (employee.getEmail() != null && !employee.getEmail().equals("")) {
            employeeData.setEmail(employee.getEmail());
        }

        if (employee.getName() != null && !employee.getName().equals("")) {
            employeeData.setName(employee.getName());
        }

        employeeRepository.save(employeeData);
        return CustomResponse.generate(HttpStatus.OK, "Data Successfully Updated");

    }
}