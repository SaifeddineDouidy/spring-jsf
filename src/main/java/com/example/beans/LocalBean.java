package com.example.beans;

import com.example.model.Local;
import com.example.repository.LocalRepository;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Component("localBean")
@SessionScoped
public class LocalBean implements Serializable {
    @Autowired
    private LocalRepository localRepository;

    private List<Local> locaux;
    private List<Local> filteredLocaux;

    private String nom;
    private int taille;
    private String type;
    private String filter = "";

    @PostConstruct
    public void init() {
        // Load locaux from the database
        locaux = localRepository.findAll();
        filteredLocaux = locaux;
    }

    public void addLocal() {
        Local newLocal = new Local(nom, taille, type);
        localRepository.save(newLocal);
        locaux = localRepository.findAll();
        filteredLocaux = locaux;

        nom = null;
        taille = 0;
        type = null;
    }


    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
        applyFilter();
    }


    public void applyFilter() {
        if (filter == null || filter.isEmpty()) {
            filteredLocaux = locaux;
        } else {
            String lowerCaseFilter = filter.toLowerCase();
            filteredLocaux = locaux.stream()
                    .filter(local -> local.getNom().toLowerCase().contains(lowerCaseFilter) ||
                            String.valueOf(local.getTaille()).contains(lowerCaseFilter) ||
                            local.getType().toLowerCase().contains(lowerCaseFilter))
                    .collect(Collectors.toList());
        }
    }

    public List<Local> getLocaux() {
        return filteredLocaux;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getTaille() {
        return taille;
    }

    public void setTaille(int taille) {
        this.taille = taille;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
