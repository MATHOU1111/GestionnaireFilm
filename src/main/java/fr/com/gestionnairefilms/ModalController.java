package fr.com.gestionnairefilms;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.util.Arrays;

public class ModalController {
    private static Stage stage;

    private static Film selectedFilm;

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

    @FXML
    private Button modifier;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    public void setStage(Stage stage) {
        if (stage == null) {
            System.out.println("Attention : un Stage null a été fourni à setStage.");
        }
        this.stage = stage;
        System.out.println("Stage défini avec succès.");
    }

    public void setFilmData(Film film) {
        this.selectedFilm = film;
        titleInput.setText(film.getTitre());
        directorInput.setText(film.getRealisateur());
        yearInput.setText("" + film.getDateSortie());
        visionneParUtilisateurInput.setText((film.getVisionneParUtilisateur() ? "Oui" : "Non"));
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
        } else {
            saveFilmDetails();
            modifier.setText("Modifier");
        }
    }
    @FXML
    private void supprimerFilm() throws IOException {
        if (selectedFilm != null) {
            int index = selectedFilm.getId();
            areYouSureShowModal(index);
            stage.close();
        }
    }
    @FXML
    private void supprimerFilmAction() {
        if (stage == null) {
            System.out.println("Erreur: Stage non initialisé.");
            return;
        }
        if (selectedFilm != null) {
            FilmController.supprimerFilm(selectedFilm.getId());
            stage.close();
        } else {
            System.out.println("Erreur dans la suppression, selectedFilm : " + selectedFilm);
        }
    }

    @FXML
    private void saveFilmDetails() {
        try {
            selectedFilm.setTitre(titleInput.getText());
            selectedFilm.setRealisateur(directorInput.getText());
            selectedFilm.setDateSortie(Integer.parseInt(yearInput.getText()));
            selectedFilm.setSummary(summaryInput.getText());
            selectedFilm.setVisionneParUtilisateur(visionneParUtilisateurInput.getText().equals("Oui"));
            selectedFilm.setNote(Integer.parseInt(noteInput.getText()));
            selectedFilm.setActeurs(Arrays.asList(actorsInput.getText().split(", ")));

            FilmController.setFilm(selectedFilm.getId(), selectedFilm);
            setEditable(false);
            updateFields();
        } catch (NumberFormatException e) {
            System.out.println("erreur!");
        }
    }

    public void areYouSureShowModal(int index) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("areYouSurePanel.fxml"));
        VBox root = loader.load();

        Stage modalStage = new Stage();
        modalStage.setTitle("Êtes-vous sûr ?");
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initOwner(stage.getScene().getWindow());  // Utilise `stage` qui doit être le stage principal
        Scene scene = new Scene(root, 400, 200);
        modalStage.setScene(scene);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        setStage(modalStage);

        modalStage.showAndWait();
    }
}