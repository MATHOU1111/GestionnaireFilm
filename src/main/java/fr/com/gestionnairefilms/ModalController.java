package fr.com.gestionnairefilms;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;



public class ModalController {
    @FXML
    private Label titleLabel;

    @FXML
    private Label directorLabel;

    @FXML
    private Label yearLabel;

    @FXML
    private Label summaryLabel;

    @FXML
    private Label visionneParUtilisateurLabel;

    @FXML
    private Label noteLabel;

    @FXML
    private Label actorsLabel;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void closeWindow() {
        stage.close();
    }

    public void setFilmData(Film film) {
        titleLabel.setText(film.getTitre());
        directorLabel.setText(film.getRealisateur());
        yearLabel.setText("Année: " + film.getDateSortie());
        visionneParUtilisateurLabel.setText("Visionné par l'utilisateur: " + (film.getVisionneParUtilisateur() ? "Oui" : "Non"));
        noteLabel.setText("Note: " + film.getNote());
        actorsLabel.setText(String.join(", ", film.getActeurs()));
        summaryLabel.setText(film.getSummary());
    }
}
