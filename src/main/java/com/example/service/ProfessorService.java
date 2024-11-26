package com.example.service;

import com.example.model.Professor;
import com.example.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorService {
    @Autowired
    private ProfessorRepository professorRepository;

    public List<Professor> getProfessorsByDepartment(Long departmentId) {
        return professorRepository.findByDepartmentId(departmentId);
    }

    public int getProfessorCountByDepartment(Long departmentId) {
        return professorRepository.countByDepartmentId(departmentId);
    }

    public void saveProfessor(Professor professor) {
        professorRepository.save(professor);
    }

    public void updateProfessor(Professor professor) {
        professorRepository.save(professor);
    }

    public void deleteProfessor(Professor professor) {
        professorRepository.delete(professor);
    }
}

