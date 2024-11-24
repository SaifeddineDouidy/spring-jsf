package com.example.service;

import com.example.model.Session;
import com.example.repository.SessionRepository;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class SessionService  implements Serializable {

    @Inject
    private SessionRepository sessionRepository;

    public Session save(Session session) {
        return sessionRepository.save(session);
    }

    public List<Session> findAll() {
        return sessionRepository.findAll();
    }

    public void delete(Long session_id) {
        sessionRepository.deleteById(session_id);
    }

    public Session find(Long session_id) {
        try {
            return sessionRepository.findById(session_id)
                    .orElseThrow(() -> new EntityNotFoundException("Session not found"));
        } catch (Exception e) {
            throw new RuntimeException("Error finding session", e);
        }
    }

    public void update(Session session) {
        try {
            Session existingSession = find(session.getSession_id());
            existingSession.setType(session.getType());
            existingSession.setStartDate(session.getStartDate());
            existingSession.setEndDate(session.getEndDate());

            sessionRepository.save(existingSession);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update session", e);
        }
    }

}
