package fr.com.gestionnairefilms;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static fr.com.gestionnairefilms.FilmController.getFilms;

public class ModalController {
    private static Stage stage;
    private ObservableList<Film> filmsList;
    private Film selectedFilm;

    @FXML
    TextField titleInput;

    @FXML
    private TextField directorInput;

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
    private DatePicker dateSortie;

    @FXML
    private ComboBox<Genre> genrebox1;

    @FXML
    private ComboBox<Genre> genreComboBox;

    @FXML
    public void initialize() {
        // c'est dingue les enums quand même
        if(genreComboBox != null){
            genreComboBox.getItems().addAll(Genre.values());
        }
        if(genrebox1 != null){
            genrebox1.getItems().addAll(Genre.values());
        }
    }

    public void setStage(Stage stage) {
        if (stage == null) {
            System.out.println("Attention : un Stage null a été fourni à setStage.");
        }
        ModalController.stage = stage;
        System.out.println("Stage défini avec succès.");
    }

    public void setFilmsList(ObservableList<Film> filmsList) {
        this.filmsList = filmsList;
    }

    // Insérer les données dans selectedFilm pour la modale
    public void setFilmData(Film film) {
        selectedFilm = film;
        titleInput.setText(film.getTitre());
        directorInput.setText(film.getRealisateur());
        dateSortie.setValue(film.getDateSortie());
        visionneParUtilisateurInput.setText(film.getVisionneParUtilisateur() ? "Oui" : "Non");
        noteInput.setText(String.valueOf(film.getNote()));
        actorsInput.setText(String.join(", ", film.getActeurs()));
        summaryInput.setText(film.getSummary());
        genrebox1.setValue(film.getGenre());
    }


    // Action lorsqu'on appuie sur le bouton modifier de la modale
    // Impossible d'utiliser un stream à cause de javaFX
    private void setEditable(boolean editable) {
        titleInput.setEditable(editable);
        directorInput.setEditable(editable);
        dateSortie.setEditable(editable);
        summaryInput.setEditable(editable);
        visionneParUtilisateurInput.setEditable(editable);
        noteInput.setEditable(editable);
        actorsInput.setEditable(editable);
        dateSortie.setEditable(editable);
        genrebox1.setEditable(editable);
    }

    // On actualise les champs post modification
    private void updateFields() {
        titleInput.setText(selectedFilm.getTitre());
        directorInput.setText(selectedFilm.getRealisateur());
        dateSortie.setValue(selectedFilm.getDateSortie());
        summaryInput.setText(selectedFilm.getSummary());
        visionneParUtilisateurInput.setText(selectedFilm.getVisionneParUtilisateur() ? "Oui" : "Non");
        noteInput.setText(String.valueOf(selectedFilm.getNote()));
        actorsInput.setText(String.join(", ", selectedFilm.getActeurs()));
        genrebox1.setValue(selectedFilm.getGenre());
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
        System.out.println(dateSortie.getValue());
    }

    @FXML
    private void supprimerFilm() throws IOException {
        if (selectedFilm != null) {
            areYouSureShowModal();
        }
    }

    @FXML
    private void supprimerFilmAction() {
        if (stage == null) {
            System.out.println("Erreur: Stage non initialisé.");
            return;
        }
        System.out.println(selectedFilm);
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
            selectedFilm.setSummary(summaryInput.getText());
            selectedFilm.setVisionneParUtilisateur(visionneParUtilisateurInput.getText().equals("Oui"));
            selectedFilm.setNote(Integer.parseInt(noteInput.getText()));
            selectedFilm.setActeurs(Arrays.asList(actorsInput.getText().split(", ")));
            selectedFilm.setDateSortie(dateSortie.getValue());


            // Je ne sais pas pourquoi, mais je suis obligé de faire ça sinon je me tape une erreur sur le genre
            // genrebox1.getValue() renvoie un Genre mais si je l'assigne directement j'ai une erreur comme quoi c une string :-)
            // Par conséquent qu'il en soit ainsi
            String genreString = String.valueOf(genrebox1.getValue());
            Genre genre = Genre.valueOf(genreString.toUpperCase());
            selectedFilm.setGenre(genre);


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


    // Afficher une modale de confirmation
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
            // on évite les erreurs à la con (à compléter)
            if (titleInput.getText().isEmpty() || directorInput.getText().isEmpty()  || noteInput.getText().isEmpty()) {
                System.out.println("Tous les champs obligatoires doivent être remplis.");
                return;
            }

            // Convertir liste acteurs en AL
            ArrayList<String> acteurs = new ArrayList<>(Arrays.asList(actorsInput.getText().split(", ")));

            // convertir l'entrée en localDate
            LocalDate date = dateSortie.getValue();

            Film newFilm = new Film(
                    titleInput.getText(),
                    Integer.parseInt(noteInput.getText()),
                    date, // Utilise l'objet Date
                    visionneParUtilisateurInput.getText().equalsIgnoreCase("Oui"),
                    acteurs, // Utilise la conversion explicite ici
                    directorInput.getText(),
                    summaryInput.getText(),
                    generateUniqueId(),
                    Genre.valueOf(String.valueOf(genreComboBox.getValue()))
            );

            if (filmsList != null) {
                filmsList.add(newFilm);
            }

            // Ajouter le film dans le fichier JSON
            JSONArray films = getFilms();
            films.add(FilmController.convertToJsonObject(newFilm));
            FilmController.saveFilms(films);

            // Fermer la fenêtre (important)
            stage.close();
        } catch (NumberFormatException e) {
            System.out.println("Erreur : Veuillez entrer des données valides. " + e.getMessage());
        }
    }

    @FXML
    private void annulerAjoutFilm() {
        stage.close();
    }

    // Générer un ID unique basé sur l'ID maximum
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


