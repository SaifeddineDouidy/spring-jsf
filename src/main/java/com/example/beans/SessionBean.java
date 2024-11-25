package com.example.beans;

import com.example.model.Session;
import com.example.service.SessionService;
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

    private Long session_id;
    private Long selectedSessionId;
    private String type;
    private String startDate;
    private String endDate;

    private List<Session> sessions;
    private Session selectedSession;

    @Inject
    private SessionService sessionService;




    public Long getSession_id() {
        return session_id;
    }

    public void setSession_id(Long session_id) {
        this.session_id = session_id;
    }

    public Long getSelectedSessionId() {
        return selectedSessionId;
    }

    public void setSelectedSessionId(Long selectedSessionId) {
        this.selectedSessionId = selectedSessionId;
    }

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

    public Session getSelectedSession() {
        return selectedSession;
    }

    public void setSelectedSession(Session selectedSession) {
        this.selectedSession = selectedSession;
    }

    public void loadSelectedSession(Session session) {
        if (session != null) {
            selectedSession = session;
            System.out.println("Selected session loaded: " + session.getSession_id());
        } else {
            System.out.println("Session is null in loadSelectedSession!");
        }
    }

    public String goToSessionDashboard(Long sessionId) {
        this.selectedSessionId = sessionId;
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("session_id", sessionId);
        return "dashboard?faces-redirect=true";
    }


    /**
     * Save a new session to the database
     */
    public void saveSession() {
        try {
            if (type == null || type.trim().isEmpty()) {
                addMessage("Type est requis.", FacesMessage.SEVERITY_ERROR);
                return;
            }
            if (startDate == null || startDate.trim().isEmpty()) {
                addMessage("Date de début est requise.", FacesMessage.SEVERITY_ERROR);
                return;
            }
            if (endDate == null || endDate.trim().isEmpty()) {
                addMessage("Date de fin est requise.", FacesMessage.SEVERITY_ERROR);
                return;
            }

            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            if (start.isAfter(end)) {
                addMessage("La date de début ne peut pas être après la date de fin.", FacesMessage.SEVERITY_WARN);
                return;
            }

            Session session = new Session();
            session.setType(type);
            session.setStartDate(start);
            session.setEndDate(end);

            sessionService.save(session);

            type = null;
            startDate = null;
            endDate = null;
            sessions = sessionService.findAll();

            addMessage("Session enregistrée avec succès.", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            addMessage("Une erreur s’est produite lors de l’enregistrement de la session.", FacesMessage.SEVERITY_FATAL);
        }
    }


    public void deleteSession(Session session) {
        try {
            // Delete the session from the database
            sessionService.delete(session.getSession_id());

            // Refresh the list of sessions
            sessions = sessionService.findAll();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred while deleting the session.");
        }
    }


    private void addMessage(String message, FacesMessage.Severity severityError) {
        FacesMessage facesMessage;
        if (message.contains("succès")) {
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, message, null);
        } else {
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
        }
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    }
}
