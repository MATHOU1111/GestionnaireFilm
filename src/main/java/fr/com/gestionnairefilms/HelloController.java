package fr.com.gestionnairefilms;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;

import static fr.com.gestionnairefilms.FilmController.getFilms;

public class HelloController {
    @FXML
    private TableView<Film> tableView;

    @FXML
    public void initialize() {
        // Ajouter des films au TableView
        Film nouveauFilm = new Film("Inception", 9, 2010, true, null, "Christopher Nolan");
        tableView.getItems().add(nouveauFilm);
        loadFilms();

        // Ajouter un écouteur d'événements au TableView
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                Film selectedFilm = tableView.getSelectionModel().getSelectedItem();
                if (selectedFilm != null) {
                    afficherDetailsFilm(selectedFilm);
                }
            }
        });
    }

    public void afficherDetailsFilm(Film selectedFilm) {
        System.out.println(selectedFilm);
    }

    private void loadFilms() {
        ObservableList<Film> films = FXCollections.observableArrayList();
        JSONArray filmsData = getFilms();


        // Dont forget to specify the (type) before we use .get() to avoid the error
        for (Object obj : filmsData) {
            if (obj instanceof JSONObject) {
                JSONObject filmJson = (JSONObject) obj;
                String titre = (String) filmJson.get("titre");

                // It is necessary to import it as a long and then convert it to int (Caused by the org.simple.json)
                Long noteLong = (Long) filmJson.get("note");
                int note = noteLong.intValue();
                Long dateLong = (Long) filmJson.get("year");
                int dateSortie = dateLong.intValue();

                boolean visionneParUtilisateur = (boolean) filmJson.get("visionneParUtilisateur");
                String realisateur = (String) filmJson.get("director");
                List<String> acteurs = (List<String>) filmJson.get("actors");

                films.add(new Film(titre, note, dateSortie, visionneParUtilisateur, acteurs, realisateur));
            }
        }
        tableView.setItems(films);
    }
}
