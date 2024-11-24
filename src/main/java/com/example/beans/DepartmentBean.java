package com.example.beans;

import com.example.model.Department;
import com.example.service.DepartmentService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@Component("departmentBean")
@RequestScoped
public class DepartmentBean implements Serializable {

    @Autowired
    private DepartmentService departmentService;

    private List<Department> departments;
    private String newDepartmentName;
    private Department selectedDepartment;

    @PostConstruct
    public void init() {
        refreshDepartments();
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public String getNewDepartmentName() {
        return newDepartmentName;
    }

    public void setNewDepartmentName(String newDepartmentName) {
        this.newDepartmentName = newDepartmentName;
    }

    public Department getSelectedDepartment() {
        return selectedDepartment;
    }

    public void setSelectedDepartment(Department selectedDepartment) {
        this.selectedDepartment = selectedDepartment;
    }

    public void refreshDepartments() {
        departments = departmentService.getAllDepartments();
    }

    public void addDepartment() {
        if (newDepartmentName != null && !newDepartmentName.trim().isEmpty()) {
            Department newDepartment = new Department();
            newDepartment.setDepartmentName(newDepartmentName);
            departmentService.saveDepartment(newDepartment);
            newDepartmentName = ""; // Clear input
            refreshDepartments();
        }
    }

    public void prepareModifyDepartment(Department department) {
        selectedDepartment = department;
        newDepartmentName = department.getDepartmentName();
    }

    public void updateDepartment() {
        if (selectedDepartment != null && newDepartmentName != null && !newDepartmentName.trim().isEmpty()) {
            selectedDepartment.setDepartmentName(newDepartmentName);
            departmentService.updateDepartment(selectedDepartment);
            newDepartmentName = ""; // Clear input
            selectedDepartment = null; // Reset selection
            refreshDepartments();
        }
    }

    public void deleteDepartment(Department department) throws IOException {
        if (department != null) {
            departmentService.deleteDepartment(department);
            FacesContext.getCurrentInstance().getExternalContext().redirect("departments.xhtml");
            refreshDepartments();
        }
    }
}
