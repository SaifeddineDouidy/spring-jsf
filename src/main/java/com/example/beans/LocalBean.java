package com.example.beans;

import com.example.model.Local;
import com.example.repository.LocalRepository;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.Part;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
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

    private Part uploadedFile;

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

    public Part getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(Part uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public void uploadFile() throws IOException {
        System.out.println("THIS FILE CONTENT TYPE IS : "+uploadedFile.getContentType());
        if (uploadedFile == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No file selected."));
            return;
        }
        String fileName = uploadedFile.getSubmittedFileName();
        System.out.println("THIS FILES NAME IS : "+fileName);

//        if (!fileName.endsWith(".csv") && !fileName.endsWith(".xsl")) {
//            FacesContext.getCurrentInstance().addMessage(null,
//                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
//                            "Invalid file type", "Only .csv or .xsl files are allowed."));
//            return;
//        }

        System.out.println(uploadedFile.getInputStream());

        try (InputStream input = uploadedFile.getInputStream()) {
            System.out.println("Entered the try");
            // Assuming the file is an Excel sheet
            Workbook workbook = new XSSFWorkbook(input);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Skip header
                }

                String name = row.getCell(0).getStringCellValue();
                int size = (int) row.getCell(1).getNumericCellValue();
                String type = row.getCell(2).getStringCellValue();

                Local newLocal = new Local(name, size, type);
                localRepository.save(newLocal);
            }

            workbook.close();

            locaux = localRepository.findAll(); // Refresh the list
            filteredLocaux = locaux;

            System.out.println("finished the try");

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "File uploaded successfully!"));
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to process the file."));
            e.printStackTrace();
        }
    }

}
