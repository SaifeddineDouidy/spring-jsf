package com.example.beans;

import com.example.model.Local;
import com.example.repository.LocalRepository;
import com.example.service.LocalService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.servlet.http.Part;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Component("localBean")
@ViewScoped
public class LocalBean implements Serializable {

    @Autowired
    private LocalService localService;

    @Autowired
    private LocalRepository localRepository;

    private List<Local> locaux;
    private List<Local> filteredLocaux;
    private Local selectedLocal = new Local();

    private String nom;
    private int taille;
    private String type;
    private String filter = "";

    private Part uploadedFile;

    @PostConstruct
    public void init() {
        locaux = localService.getAllLocaux();
        filteredLocaux = locaux;
    }

    public String addLocal() {
        Local newLocal = new Local(nom, taille, type);
        localService.addLocal(newLocal);
        reloadLocaux();
        clearFields();
        return "locaux?faces-redirect=true";
    }

    public void deleteLocal(Local local) {
        try {
            localService.deleteLocal(local);
            reloadLocaux();
            FacesContext.getCurrentInstance().getExternalContext().redirect("locaux.xhtml");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error deleting local", e.getMessage()));
        }
    }

    public void updateLocal() {
        try {
            if (selectedLocal == null || selectedLocal.getId() == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No local selected for update"));
                return;
            }

            localService.updateLocal(selectedLocal);
            reloadLocaux();
            clearFields();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Local updated successfully"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error updating local", e.getMessage()));
        }
    }

    private void reloadLocaux() {
        locaux = localService.getAllLocaux();
        applyFilter();
    }

    private void clearFields() {
        nom = null;
        taille = 0;
        type = null;
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

    public void prepareUpdate(Local local) {
        // Creation de nouvelle instance pour eviter les conflicts
        this.selectedLocal = new Local();
        this.selectedLocal.setId(local.getId());
        this.selectedLocal.setNom(local.getNom());
        this.selectedLocal.setTaille(local.getTaille());
        this.selectedLocal.setType(local.getType());


        FacesContext context = FacesContext.getCurrentInstance();
        context.getPartialViewContext().getRenderIds().add("updateForm");

        System.out.println("Preparing update for local: " +
                "ID=" + selectedLocal.getId() +
                ", Nom=" + selectedLocal.getNom() +
                ", Taille=" + selectedLocal.getTaille() +
                ", Type=" + selectedLocal.getType());
    }


    public void prepareUpdate(Long id, String nom, int taille, String type) {
        this.selectedLocal = new Local();
        this.selectedLocal.setId(id);
        this.selectedLocal.setNom(nom);
        this.selectedLocal.setTaille(taille);
        this.selectedLocal.setType(type);

        System.out.println("Preparing update for local: " +
                "ID=" + id +
                ", Nom=" + nom +
                ", Taille=" + taille +
                ", Type=" + type);
    }

    public List<Local> getLocaux() {
        return filteredLocaux;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
        applyFilter();
    }

    public Local getSelectedLocal() {
        return selectedLocal;
    }

    public void setSelectedLocal(Local selectedLocal) {
        this.selectedLocal = selectedLocal;
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
