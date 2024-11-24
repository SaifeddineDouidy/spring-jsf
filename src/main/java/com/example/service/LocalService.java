package com.example.service;

import com.example.model.Local;
import com.example.repository.LocalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocalService {

    @Autowired
    private LocalRepository localRepository;

    public List<Local> getAllLocaux() {
        return localRepository.findAll();
    }

    public Local addLocal(Local local) {
        return localRepository.save(local);
    }

    public void deleteLocal(Local local) {
        localRepository.delete(local);
    }

    public Local updateLocal(Local local) {
        return localRepository.save(local);
    }
}
