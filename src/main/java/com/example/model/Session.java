package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long session_id;

    private String type;

    private LocalDate startDate;
    private LocalDate endDate;

    public Session() {}

    // Getters and Setters
    public Long getSession_id() {
        return session_id;
    }

    public void setSession_id(Long sessionId) {
        this.session_id = sessionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
