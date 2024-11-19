package com.example.service;

import com.example.model.Session;
import com.example.repository.SessionRepository;
import jakarta.inject.Inject;
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

    public void delete(Session session) {
        sessionRepository.delete(session);
    }
}
