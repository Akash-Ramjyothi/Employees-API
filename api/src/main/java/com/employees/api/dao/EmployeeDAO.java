package com.employees.api.dao;

import com.employees.api.entity.Employee;

import java.util.List;

public interface EmployeeDAO {

    List<Employee> findAll();

}
