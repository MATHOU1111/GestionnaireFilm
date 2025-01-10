package fr.com.gestionnairefilms;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

public class Film {
    private String titre;
    private int note;
    private LocalDate dateSortie;
    private Boolean visionneParUtilisateur;
    private ArrayList<String> acteurs;  // List of actors
    private String realisateur;  // Director
    private String summary;

    private final int id;

    public Film(String titre, int note, LocalDate dateSortie, Boolean visionneParUtilisateur, List<String> acteurs, String realisateur, String summary, int id) {
        this.titre = titre;
        this.note = note;
        this.dateSortie = dateSortie;
        this.visionneParUtilisateur = visionneParUtilisateur;
        this.acteurs = (ArrayList<String>) acteurs;
        this.realisateur = realisateur;
        this.summary = summary;
        this.id = id;
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

    public static LocalDate parseDate(String dateText, String inputFormat, String outputFormat) {
        if (dateText == null || dateText.isEmpty()) {
            return null;
        }
        try {
            // Création des formateurs pour les formats d'entrée et de sortie
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(inputFormat, Locale.FRENCH);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(outputFormat, Locale.FRENCH);

            // Parse la chaîne en LocalDate selon le format d'entrée
            LocalDate parsedDate = LocalDate.parse(dateText, inputFormatter);

            // Reformate la date selon le format de sortie (retourne toujours un LocalDate)
            String reformattedDate = parsedDate.format(outputFormatter);

            // Reconvertit la date reformattée en LocalDate
            return LocalDate.parse(reformattedDate, outputFormatter);
        } catch (DateTimeParseException e) {
            System.out.println("Erreur de formatage : " + e.getMessage());
            return null;
        }
    }

}


