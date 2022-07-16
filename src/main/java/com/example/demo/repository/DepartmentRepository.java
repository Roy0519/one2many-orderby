package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.domain.Department;

public interface DepartmentRepository extends CrudRepository<Department, Long> {
  
}
