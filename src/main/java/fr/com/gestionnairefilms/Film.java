package fr.com.gestionnairefilms;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;


public class Film {
    private String titre;
    private int note;
    private LocalDate dateSortie;
    private Boolean visionneParUtilisateur;
    private ArrayList<String> acteurs;
    private String realisateur;
    private String summary;
    private Genre genre;
    private String imagePath;

    private final int id;

    public Film(String titre, int note, LocalDate dateSortie, Boolean visionneParUtilisateur, List<String> acteurs, String realisateur, String summary, int id, Genre genre, String imagePath) {
        this.titre = titre;
        this.note = note;
        this.dateSortie = dateSortie;
        this.visionneParUtilisateur = visionneParUtilisateur;
        this.acteurs = (ArrayList<String>) acteurs;
        this.realisateur = realisateur;
        this.summary = summary;
        this.id = id;
        this.genre = genre;
        this.imagePath = imagePath;

    }
    public String getTitre() {
        return titre;
    }


    public int getNote() {
        return note;
    }

    public LocalDate getDateSortie() {
        return dateSortie;
    }

    public Boolean getVisionneParUtilisateur() {
        return visionneParUtilisateur;
    }

    public List<String> getActeurs() {
        return acteurs;
    }

    public String getRealisateur() {
        return realisateur;
    }

    public String getSummary(){ return summary;}

    public int getId(){return id;}

    // Setters
    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public void setDateSortie(LocalDate dateSortie) {
        this.dateSortie = dateSortie;
    }

    public void setVisionneParUtilisateur(Boolean visionneParUtilisateur) {
        this.visionneParUtilisateur = visionneParUtilisateur;
    }

    public void setActeurs(List<String> acteurs) {
        this.acteurs = new ArrayList<>(acteurs);
    }

    public void setRealisateur(String realisateur) {
        this.realisateur = realisateur;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    @Override
    public String toString() {
        return "titre='" + titre + '\'' +
                ", note=" + note +
                ", dateSortie=" + dateSortie +
                ", visionneParUtilisateur=" + visionneParUtilisateur +
                ", acteurs=" + acteurs +
                ", realisateur='" + realisateur + '\'' +
                ", summary='" + summary + '\'' +
                ", genre=" + genre
                + ", imagePath=" + imagePath;

    }

}


