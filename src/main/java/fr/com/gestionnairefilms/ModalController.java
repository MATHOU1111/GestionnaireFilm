package fr.com.gestionnairefilms;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.simple.JSONArray;

import java.util.Arrays;

public class ModalController {
    @FXML
    TextField titleInput;

    @FXML
    private TextField directorInput;

    @FXML
    private TextField yearInput;

    @FXML
    private TextArea summaryInput;

    @FXML
    private TextField visionneParUtilisateurInput;

    @FXML
    private TextField noteInput;

    @FXML
    private TextField actorsInput;

    private Stage stage;

    private Film selectedFilm;

    @FXML
    Button modifier;


    public void setFilmData(Film film) {
        this.selectedFilm = film;
        titleInput.setText(film.getTitre());
        directorInput.setText(film.getRealisateur());
        yearInput.setText("" + film.getDateSortie());
        visionneParUtilisateurInput.setText("Visionné par l'utilisateur: " + (film.getVisionneParUtilisateur() ? "Oui" : "Non"));
        noteInput.setText("" + film.getNote());
        actorsInput.setText(String.join(", ", film.getActeurs()));
        summaryInput.setText(film.getSummary());
    }

    private void setEditable(boolean editable) {
        titleInput.setEditable(editable);
        directorInput.setEditable(editable);
        yearInput.setEditable(editable);
        summaryInput.setEditable(editable);
        visionneParUtilisateurInput.setEditable(editable);
        noteInput.setEditable(editable);
        actorsInput.setEditable(editable);
    }

    private void updateFields() {
        titleInput.setText(selectedFilm.getTitre());
        directorInput.setText(selectedFilm.getRealisateur());
        yearInput.setText(String.valueOf(selectedFilm.getDateSortie()));
        summaryInput.setText(selectedFilm.getSummary());
        visionneParUtilisateurInput.setText(selectedFilm.getVisionneParUtilisateur() ? "Oui" : "Non");
        noteInput.setText(String.valueOf(selectedFilm.getNote()));
        actorsInput.setText(String.join(", ", selectedFilm.getActeurs()));
    }

    @FXML
    private void modifierFilm() {
        if (selectedFilm != null && modifier.getText().equals("Modifier")) {
            System.out.println("Modification de: " + selectedFilm.getTitre());
            setEditable(true);
            modifier.setText("Sauvegarder");
        } else{
            saveFilmDetails();
            modifier.setText("Modifier");
        }
    }

    @FXML
    private void saveFilmDetails() {
        selectedFilm.setTitre(titleInput.getText());
        selectedFilm.setRealisateur(directorInput.getText());
        selectedFilm.setDateSortie(Integer.parseInt(yearInput.getText()));
        selectedFilm.setSummary(summaryInput.getText());
        selectedFilm.setVisionneParUtilisateur(visionneParUtilisateurInput.getText().equals("Oui"));
        selectedFilm.setNote(Integer.parseInt(noteInput.getText()));
        selectedFilm.setActeurs(Arrays.asList(actorsInput.getText().split(", ")));

        JSONArray filmData = FilmController.getFilms(); // Reload the current films
        setEditable(false); // Turn off edit mode after saving
        updateFields(); // Update fields to reflect saved data
        FilmController.saveFilms(filmData);
    }
}