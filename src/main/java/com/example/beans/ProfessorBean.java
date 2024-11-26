package com.example.beans;

import com.example.model.Department;
import com.example.model.Professor;
import com.example.service.DepartmentService;
import com.example.service.ProfessorService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.springframework.stereotype.Component;
import jakarta.enterprise.context.RequestScoped;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@Component("professorBean")
@RequestScoped
public class ProfessorBean implements Serializable {

    @Autowired
    private ProfessorService professorService;
    @Autowired
    private DepartmentService departmentService;
    private Professor selectedProfessor = new Professor();

    private List<Professor> professors;
    private Long selectedDepartmentId;
    private String departmentName;
    private String newProfessorName;
    private String newProfessorEmail;
    private boolean newProfessorDispense;

    public void loadProfessors(Long departmentId, String deptName) {
        this.selectedDepartmentId = departmentId;
        this.departmentName = deptName;
        this.professors = professorService.getProfessorsByDepartment(departmentId);
    }

    public void createProfessor() {
        if (newProfessorName != null && !newProfessorName.trim().isEmpty()
                && newProfessorEmail != null && !newProfessorEmail.trim().isEmpty()) {

            Professor newProfessor = new Professor();
            newProfessor.setName(newProfessorName);
            newProfessor.setEmail(newProfessorEmail);
            newProfessor.setDispense(newProfessorDispense);

            // Set the department
            Department department = departmentService.getDepartmentById(selectedDepartmentId);
            newProfessor.setDepartment(department);

            professorService.saveProfessor(newProfessor);

            // Clear the form
            newProfessorName = null;
            newProfessorEmail = null;
            newProfessorDispense = false;

            // Refresh the list
            loadProfessors(selectedDepartmentId, departmentName);
        }
    }


    // Getters and setters
    public List<Professor> getProfessors() {
        return professors;
    }

    public void setProfessors(List<Professor> professors) {
        this.professors = professors;
    }

    public Long getSelectedDepartmentId() {
        return selectedDepartmentId;
    }

    public void setSelectedDepartmentId(Long selectedDepartmentId) {
        this.selectedDepartmentId = selectedDepartmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    public String getNewProfessorName() {
        return newProfessorName;
    }

    public void setNewProfessorName(String newProfessorName) {
        this.newProfessorName = newProfessorName;
    }

    public String getNewProfessorEmail() {
        return newProfessorEmail;
    }

    public void setNewProfessorEmail(String newProfessorEmail) {
        this.newProfessorEmail = newProfessorEmail;
    }

    public boolean isNewProfessorDispense() {
        return newProfessorDispense;
    }

    public void setNewProfessorDispense(boolean newProfessorDispense) {
        this.newProfessorDispense = newProfessorDispense;
    }

    public int getProfessorCountByDepartment(Long departmentId) {
        if (departmentId == null) {
            return 0; // Handle null case, no department selected
        }
        return professorService.getProfessorCountByDepartment(departmentId);
    }


    public void prepareModifyProfessor(Professor professor) {
        selectedProfessor.setId(professor.getId());
        selectedProfessor.setName(professor.getName());
        selectedProfessor.setEmail(professor.getEmail());
        selectedProfessor.setDispense(professor.isDispense());
        selectedProfessor.setDepartment(professor.getDepartment()); // Ensure department is set
    }



    public void modifyProfessor() {
        // Ensure email is not null
        if (selectedProfessor.getEmail() == null || selectedProfessor.getEmail().trim().isEmpty()) {
            // Handle the error, maybe show a validation message
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email is required", null));
            return;  // Prevent further processing
        }

        // Set department if not already set
        if (selectedProfessor.getDepartment() == null) {
            Department department = departmentService.getDepartmentById(selectedDepartmentId);
            selectedProfessor.setDepartment(department);
        }

        // Proceed with update
        professorService.updateProfessor(selectedProfessor);
        resetForm();
    }



    public void deleteProfessor(Professor professor) throws IOException {
        professorService.deleteProfessor(professor);
        FacesContext.getCurrentInstance().getExternalContext().redirect("professors.xhtml");
        loadProfessors(selectedDepartmentId, departmentName);
    }

    private void resetForm() {
        this.newProfessorName = null;
        this.newProfessorEmail = null;
        this.newProfessorDispense = false;
        this.selectedProfessor = null;
    }
}