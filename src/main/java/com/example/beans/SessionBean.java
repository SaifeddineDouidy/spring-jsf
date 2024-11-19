package com.example.beans;

import com.example.model.Session;
import com.example.service.SessionService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Component("sessionBean")
@ViewScoped
public class SessionBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String type;
    private String startDate;
    private String endDate;

    private List<Session> sessions;
    private Session selectedSession;

    @Inject
    private SessionService sessionService;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<Session> getSessions() {
        if (sessions == null) {
            sessions = sessionService.findAll(); // Lazy load sessions
        }
        return sessions;
    }





    /**
     * Save a new session to the database
     */
    public void saveSession() {
        try {
            // Check if all fields are filled
            if (type == null || type.trim().isEmpty()) {
                addMessage("Type is required.");
                return;
            }
            if (startDate == null || startDate.trim().isEmpty()) {
                addMessage("Start date is required.");
                return;
            }
            if (endDate == null || endDate.trim().isEmpty()) {
                addMessage("End date is required.");
                return;
            }

            // Parse dates and validate logic
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            if (start.isAfter(end)) {
                addMessage("Start date cannot be after end date.");
                return;
            }

            // Create and save session
            Session session = new Session();
            session.setType(type);
            session.setStartDate(start);
            session.setEndDate(end);

            sessionService.save(session);

            // Reset fields and update session list
            type = null;
            startDate = null;
            endDate = null;
            sessions = sessionService.findAll();

            addMessage("Session saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            addMessage("An error occurred while saving the session.");
        }
    }




    private void addMessage(String message) {
        FacesMessage facesMessage;
        if (message.contains("successfully")) {
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, message, null);
        } else {
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
        }
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    }
}
