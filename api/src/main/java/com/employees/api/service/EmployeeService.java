package com.employees.api.service;

import com.employees.api.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> findAll();
}
