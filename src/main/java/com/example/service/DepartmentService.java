package com.example.service;

import com.example.model.Department;
import com.example.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class DepartmentService {
    private static final Logger logger = LoggerFactory.getLogger(DepartmentService.class);

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public void saveDepartment(Department department) {
        departmentRepository.save(department);
    }

    public void updateDepartment(Department department) {
        if (departmentRepository.existsById(department.getId())) {
            departmentRepository.save(department);
        } else {
            logger.warn("Cannot update. Department not found with ID: {}", department.getId());
        }
    }

    public void deleteDepartment(Department department) {
        departmentRepository.delete(department);
    }

    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
    }
}
