package com.example.beans;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class NavigationBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public String goToDashboard() {
        return "dashboard?faces-redirect=true";
    }

    public String goToExams() {
        return "exams?faces-redirect=true";
    }

    public String goToSurveillance() {
        return "surveillance?faces-redirect=true";
    }

    public String goToEmploi() {
        return "emploi?faces-redirect=true";
    }

    public String goToOptions() {
        return "options?faces-redirect=true";
    }

    public String goToDepartements() {
        return "departments?faces-redirect=true";
    }

    public String goToLocaux() {
        return "locaux?faces-redirect=true";
    }

    public String goToProfile() {
        return "profile?faces-redirect=true";
    }

    public String goToSettings() {
        return "settings?faces-redirect=true";
    }

    public String logout() {
        // Add your logout logic here
        return "login?faces-redirect=true";
    }
}