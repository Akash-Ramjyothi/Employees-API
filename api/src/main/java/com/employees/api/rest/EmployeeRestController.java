package com.employees.api.rest;


import com.employees.api.entity.Employee;
import com.employees.api.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EmployeeRestController {

    private EmployeeService employeeService;
    private ObjectMapper objectMapper;

    public EmployeeRestController(EmployeeService theEmployeeService, ObjectMapper theObjectMapper) {
        employeeService = theEmployeeService;
        objectMapper = theObjectMapper;
    }

    @GetMapping("/employees")
    public List<Employee> findAll() {
        return employeeService.findAll();
    }

    @GetMapping("/employees/{employeeId}")
    public Employee getEmployee(@PathVariable int employeeId) {
        Employee theEmployee = employeeService.findById(employeeId);

        if (theEmployee == null) {
            throw new RuntimeException("🚫 Employee ID not found: " + employeeId);
        }

        return theEmployee;
    }

    @PostMapping("/employees")
    public Employee addEmployee(@RequestBody Employee theEmployee) {
        theEmployee.setId(0);
        Employee dbEmployee = employeeService.save(theEmployee);
        return dbEmployee;
    }

    @PutMapping("/employees")
    public Employee updateEmployee(@RequestBody Employee theEmployee) {
        Employee dbEmployee = employeeService.save(theEmployee);
        return dbEmployee;
    }

    @PatchMapping("/employees/{employeeId}")
    public Employee patchEmployee(@PathVariable int employeeId, @RequestBody Map<String, Object> patchPayload) {
        Employee tempEmployee = employeeService.findById(employeeId);
        System.out.println("🩹 patchPayload = " + patchPayload);

        if (tempEmployee == null) {
            throw new RuntimeException("🚫 Employee ID not found: " + employeeId);
        }

        if (patchPayload.containsKey("id")) {
            throw new RuntimeException("⛔️ Employee ID not allowed in Request Body");
        }

        Employee patchedEmployee = apply(patchPayload, tempEmployee);
        Employee dbEmployee = employeeService.save(patchedEmployee);

        return dbEmployee;
    }

    private Employee apply(Map<String, Object> patchPayload, Employee tempEmployee) {
        ObjectNode employeeNode = objectMapper.convertValue(tempEmployee, ObjectNode.class);
        System.out.println("🏢 employeeNode = " + employeeNode);

        ObjectNode patchNode = objectMapper.convertValue(patchPayload, ObjectNode.class);
        System.out.println("🤿 patchNode = " + patchNode);

        employeeNode.setAll(patchNode);

        Employee patchedEmployee = objectMapper.convertValue(employeeNode, Employee.class);
        return patchedEmployee;
    }

    @DeleteMapping("/employees/{employeeId}")
    public String deleteEmployee(@PathVariable int employeeId) {
        Employee tempEmployee = employeeService.findById(employeeId);

        if (tempEmployee == null) {
            throw new RuntimeException("🚫 Employee ID not found: " + employeeId);
        }

        employeeService.deleteById(employeeId);
        return "🗑️ Deleted Employee ID: " + employeeId;
    }
}
