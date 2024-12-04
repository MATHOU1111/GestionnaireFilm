package fr.com.gestionnairefilms;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
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


    @FXML
    public void initialize() {
        loadFilms();

        // Ajouter un écouteur d'événements au TableVew
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                Film selectedFilm = tableView.getSelectionModel().getSelectedItem();
                System.out.println(selectedFilm);
                try {
                    showFilmDetails(selectedFilm);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


    public void showFilmDetails(Film selectedFilm) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ModalWindow.fxml"));
        VBox root = loader.load();
        ModalController controller = loader.getController();

        Stage modalStage = new Stage();
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
                String summary = (String) filmJson.get("summary");

                films.add(new Film(titre, note, dateSortie, visionneParUtilisateur, acteurs, realisateur, summary));
            }
        }
        System.out.println(tableView);
        tableView.setItems(films);
    }

}


