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


