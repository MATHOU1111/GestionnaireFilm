package fr.com.gestionnairefilms;


import java.util.Date;
import java.util.List;
import java.util.ArrayList;


import java.io.*;

public class Film {
    private String titre;
    private int note;

    private Date dateSortie;
    private Boolean visionneParUtilisateur;
    private ArrayList<String> acteurs;  // List of actors
    private String realisateur;  // Director

    public Film(String titre, int note, Date dateSortie, Boolean visionneParUtilisateur, List<String> acteurs, String realisateur) {
        this.titre = titre;
        this.note = note;
        this.dateSortie = dateSortie;
        this.visionneParUtilisateur = visionneParUtilisateur;
        this.acteurs = (ArrayList<String>) acteurs;
        this.realisateur = realisateur;
    }

    @Override
    public String toString(){
        return "titre='" + titre + '\'' +
                ", note=" + note +
                ", dateSortie=" + dateSortie +
                ", visionneParUtilisateur=" + visionneParUtilisateur +
                ", acteurs=" + acteurs +
                ", realisateur='" + realisateur + '\'';
    }


}


