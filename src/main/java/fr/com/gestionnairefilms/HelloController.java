package fr.com.gestionnairefilms;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.util.List;



public class HelloController {
    @FXML
    private TableView<Film> tableView;

    private ObservableList<Film> films; // Liste observable des films

    @FXML
    private TextField searchBar; // Barre de recherche

    private FilteredList<Film> filteredFilms; // Liste filtrée des films


    @FXML
    public void initialize() throws ParseException {
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

    // Apparition de la modale avec les données
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

                // Convertir la chaîne en LocalDate
                String dateString = (String) filmJson.get("dateSortie");
                LocalDate date = null;
                if (dateString != null) {
                    try {
                        // Updated formatter to match "yyyy-MM-dd"
                        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        System.out.println("dateString : " + dateString);
                        date = LocalDate.parse(dateString, dateFormat);
                    } catch (DateTimeParseException e) {
                        System.out.println("Erreur de conversion de la date : " + e.getMessage());
                    }
                }
                boolean visionneParUtilisateur = (boolean) filmJson.get("visionneParUtilisateur");
                String realisateur = (String) filmJson.get("director");
                List<String> acteurs = (List<String>) filmJson.get("actors");
                String summary = (String) filmJson.get("summary");
                Genre genre = Genre.valueOf((String) filmJson.get("genre"));

                Long id = (Long) filmJson.get("id");
                int idInt = id.intValue();

                films.add(new Film(titre, note, date, visionneParUtilisateur, acteurs, realisateur, summary, idInt, genre));
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


