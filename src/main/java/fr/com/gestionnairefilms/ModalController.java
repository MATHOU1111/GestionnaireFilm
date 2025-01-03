package fr.com.gestionnairefilms;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static fr.com.gestionnairefilms.FilmController.searchFilmInJson;

public class ModalController {
    private static Stage stage;
    private ObservableList<Film> filmsList; // Référence à la liste des films dans le TableView
    private Film selectedFilm;

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
        ModalController.stage = stage;
        System.out.println("Stage défini avec succès.");
    }

    public void setFilmsList(ObservableList<Film> filmsList) {
        this.filmsList = filmsList; // Référence à la liste des films
    }

    public void setFilmData(Film film) {
        selectedFilm = film;
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
            areYouSureShowModal();
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
            FilmController.supprimerFilm(selectedFilm.getTitre());
            if (filmsList != null) {
                filmsList.remove(selectedFilm); // Supprimer le film de la liste observable
            }
            stage.close();
        } else {
            System.out.println("Erreur dans la suppression, selectedFilm : " + selectedFilm);
        }
    }

    @FXML
    private void saveFilmDetails() {
        try {
            // Mettre à jour les détails du film
            selectedFilm.setTitre(titleInput.getText());
            selectedFilm.setRealisateur(directorInput.getText());
            selectedFilm.setDateSortie(Integer.parseInt(yearInput.getText()));
            selectedFilm.setSummary(summaryInput.getText());
            selectedFilm.setVisionneParUtilisateur(visionneParUtilisateurInput.getText().equals("Oui"));
            selectedFilm.setNote(Integer.parseInt(noteInput.getText()));
            selectedFilm.setActeurs(Arrays.asList(actorsInput.getText().split(", ")));

            // Mise à jour dans le fichier JSON
            int index = Integer.parseInt(FilmController.searchFilmInJson(String.valueOf(selectedFilm.getId())));
            FilmController.setFilm(index, selectedFilm);

            // Mise à jour dans la liste observable
            if (filmsList != null) {
                int listIndex = filmsList.indexOf(selectedFilm);
                if (listIndex != -1) {
                    filmsList.set(listIndex, selectedFilm);
                }
            }

            setEditable(false);
            updateFields();

        } catch (NumberFormatException e) {
            System.out.println("Erreur de format : " + e.getMessage());
        }
    }

    public void areYouSureShowModal() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("areYouSurePanel.fxml"));
        VBox root = loader.load();

        Stage modalStage = new Stage();
        modalStage.setTitle("Êtes-vous sûr ?");
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initOwner(stage.getScene().getWindow());
        Scene scene = new Scene(root, 400, 200);
        modalStage.setScene(scene);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        // Ne pas oublier de set le stage pour pouvoir fermer la modale
        setStage(modalStage);

        modalStage.showAndWait();
    }

    @FXML
    private void ajouterFilmAction() {
        try {
            // Valider les entrées utilisateur
            if (titleInput.getText().isEmpty() || directorInput.getText().isEmpty() || yearInput.getText().isEmpty() || noteInput.getText().isEmpty()) {
                System.out.println("Tous les champs obligatoires doivent être remplis.");
                return;
            }

            // Convertir la liste des acteurs en ArrayList explicite
            ArrayList<String> acteurs = new ArrayList<>(Arrays.asList(actorsInput.getText().split(", ")));

            // Créer un nouvel objet Film
            Film newFilm = new Film(
                    titleInput.getText(),
                    Integer.parseInt(noteInput.getText()),
                    Integer.parseInt(yearInput.getText()),
                    visionneParUtilisateurInput.getText().equalsIgnoreCase("Oui"),
                    acteurs, // Utilise la conversion explicite ici
                    directorInput.getText(),
                    summaryInput.getText(),
                    generateUniqueId()
            );

            // Ajouter le film à la liste observable
            if (filmsList != null) {
                filmsList.add(newFilm);
            }

            // Ajouter le film dans le fichier JSON
            JSONArray films = FilmController.getFilms();
            films.add(FilmController.convertToJsonObject(newFilm));
            FilmController.saveFilms(films);

            // Fermer la fenêtre
            stage.close();
        } catch (NumberFormatException e) {
            System.out.println("Erreur : Veuillez entrer des données valides. " + e.getMessage());
        }
    }

    @FXML
    private void annulerAjoutFilm() {
        stage.close(); // Ferme la modale sans ajouter de film
    }

    // Générer un ID unique basé sur l'ID maximum existant
    private int generateUniqueId() {
        JSONArray films = FilmController.getFilms();
        int maxId = films.stream()
                .filter(obj -> obj instanceof JSONObject)
                .mapToInt(obj -> Integer.parseInt(((JSONObject) obj).get("id").toString()))
                .max()
                .orElse(0);
        return maxId + 1;
    }
}
