package com.example.employeeapp.employeeRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.employeeapp.employeeEntity.Employee;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Integer> {
}
