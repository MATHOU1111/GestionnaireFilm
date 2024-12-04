package fr.com.gestionnairefilms;


import java.util.List;
import java.util.ArrayList;

public class Film {
    private String titre;
    private int note;
    private Integer dateSortie;
    private Boolean visionneParUtilisateur;
    private ArrayList<String> acteurs;  // List of actors
    private String realisateur;  // Director
    private String summary;

    public Film(String titre, int note, Integer dateSortie, Boolean visionneParUtilisateur, List<String> acteurs, String realisateur, String summary) {
        this.titre = titre;
        this.note = note;
        this.dateSortie = dateSortie;
        this.visionneParUtilisateur = visionneParUtilisateur;
        this.acteurs = (ArrayList<String>) acteurs;
        this.realisateur = realisateur;
        this.summary = summary;
    }
    public String getTitre() {
        return titre;
    }


    public int getNote() {
        return note;
    }

    public int getDateSortie() {
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

    // Setters
    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public void setDateSortie(Integer dateSortie) {
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

    @Override
    public String toString(){
        return "titre='" + titre + '\'' +
                ", note=" + note +
                ", dateSortie=" + dateSortie +
                ", visionneParUtilisateur=" + visionneParUtilisateur +
                ", acteurs=" + acteurs +
                ", realisateur='" + realisateur + '\'' +
                ",summary='" + summary + '\'';

    }


}


