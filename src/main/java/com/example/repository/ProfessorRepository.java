package com.example.repository;

import com.example.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    List<Professor> findByDepartmentId(Long departmentId);
}
