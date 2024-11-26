package com.example.beans;

import com.example.model.User;

import java.io.Serializable;
import java.util.List;

import com.example.model.Department;

import jakarta.annotation.ManagedBean;
import jakarta.faces.view.ViewScoped;

import java.util.HashMap;
import java.util.Map;


import javax.inject.Inject;

@ManagedBean
@ViewScoped

public class DashboardBean implements Serializable {
    @Inject
    private DepartmentBean departmentBean;

    @Inject
    private ProfessorBean professorBean;

//    private List<User> users;
//    private int activeUsers;
//    private int newUsers;
//    private double totalRevenue;

    // Map to store number of professors per department
    private Map<Long, Integer> professorCountByDepartment;

    // Method to calculate professors count for each department
    public void calculateProfessorsByDepartment() {
        professorCountByDepartment = new HashMap<>();

        // Retrieve all departments
        List<Department> departments = departmentBean.getDepartments();

        for (Department dept : departments) {
            // Get professors count for each department
            int professorCount = professorBean.getProfessorCountByDepartment(dept.getId());
            professorCountByDepartment.put(dept.getId(), professorCount);
        }
    }

    // Method to get total number of professors across all departments
    public int getTotalProfessorsCount() {
        if (professorCountByDepartment == null) {
            calculateProfessorsByDepartment();
        }

        return professorCountByDepartment.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    // Getter for professor count by department
    public Map<Long, Integer> getProfessorCountByDepartment() {
        if (professorCountByDepartment == null) {
            calculateProfessorsByDepartment();
        }
        return professorCountByDepartment;
    }


}