package fr.com.gestionnairefilms;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.util.List;

import static fr.com.gestionnairefilms.FilmController.getFilms;


public class HelloController {
    @FXML
    private TableView<Film> tableView;

    private ObservableList<Film> films; // Liste observable des films

    @FXML
    private TextField searchBar; // Barre de recherche

    private FilteredList<Film> filteredFilms; // Liste filtrée des films


    @FXML
    public void initialize() {
        loadFilms();

        // Ajouter un écouteur d'événements au TableVew
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                Film selectedFilm = tableView.getSelectionModel().getSelectedItem();
                try {
                    showFilmDetails(selectedFilm);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        setupSearchBar();
    }

    public void showFilmDetails(Film selectedFilm) throws IOException {
        FXMLLoader loader = new FXMLLoader(ModalController.class.getResource("ModalWindow.fxml"));
        VBox root = loader.load();
        ModalController controller = loader.getController();

        Stage modalStage = new Stage();
        controller.setStage(modalStage);  // Passez l'instance du Stage au contrôleur

        // Passez la liste des films au ModalController
        controller.setFilmsList(films);

        modalStage.setTitle("Détails du Film");
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initOwner(tableView.getScene().getWindow());
        Scene scene = new Scene(root, 600, 600);
        modalStage.setScene(scene);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        controller.setFilmData(selectedFilm);
        modalStage.showAndWait();
    }

    private void loadFilms() {
        films = FXCollections.observableArrayList();
        JSONArray filmsData = FilmController.getFilms();

        for (Object obj : filmsData) {
            if (obj instanceof JSONObject filmJson) {

                String titre = (String) filmJson.get("titre");
                Long noteLong = (Long) filmJson.get("note");
                int note = noteLong.intValue();
                Long dateLong = (Long) filmJson.get("year");
                int dateSortie = dateLong.intValue();

                boolean visionneParUtilisateur = (boolean) filmJson.get("visionneParUtilisateur");
                String realisateur = (String) filmJson.get("director");
                List<String> acteurs = (List<String>) filmJson.get("actors");
                String summary = (String) filmJson.get("summary");

                Long id = (Long) filmJson.get("id");
                int idInt = id.intValue();

                films.add(new Film(titre, note, dateSortie, visionneParUtilisateur, acteurs, realisateur, summary, idInt));
            }
        }

        // Configure la liste filtrée

        // Affiche tous les films par défaut
        filteredFilms = new FilteredList<>(films, b -> true);
        // Lie la liste filtrée au TableView
        tableView.setItems(filteredFilms);
    }

    @FXML
    private void ajouterFilm() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddFilmModal.fxml"));
        VBox root = loader.load();
        ModalController controller = loader.getController();

        Stage modalStage = new Stage();
        controller.setStage(modalStage);
        // la liste pour ajouter le nouveau film
        controller.setFilmsList(films);

        modalStage.setTitle("Ajouter un Film");
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initOwner(tableView.getScene().getWindow());
        Scene scene = new Scene(root, 600, 600);
        modalStage.setScene(scene);
        modalStage.showAndWait();

        // Rafraîchir TableView après ajout
        tableView.refresh();
    }

    private void setupSearchBar() {
        // Ajoute un listener pour filtrer les films à chaque modification de texte
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredFilms.setPredicate(film -> {
                // Si la barre de recherche est vide, afficher tous les films
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Filtrer uniquement par titre
                return film.getTitre().toLowerCase().contains(lowerCaseFilter);
            });
        });
    }
}


