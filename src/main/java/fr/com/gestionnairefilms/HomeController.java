package fr.com.gestionnairefilms;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.util.List;


public class HomeController {
    @FXML
    private TableView<Film> tableView;

    private ObservableList<Film> films; // Liste observable des films

    @FXML
    private TextField searchBar; // Barre de recherche

    private FilteredList<Film> filteredFilms; // Liste filtrée des films

    @FXML
    private TableColumn<Film, String> imageColumn;



    @FXML
    public void initialize() throws ParseException {
        loadFilms();


        imageColumn.setCellFactory(createImageCellFactory());
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

    private Callback<TableColumn<Film, String>, TableCell<Film, String>> createImageCellFactory() {
        // Cette méthode crée une usine de cellules pour une TableColumn qui affiche des images.
        // Elle retourne une TableCell personnalisée contenant un ImageView.

        return (TableColumn<Film, String> param) -> new TableCell<Film, String>() {
            private final ImageView imageView = new ImageView();

            {
                // Configure la taille de l'ImageView pour chaque cellule.
                imageView.setFitHeight(200);
                imageView.setFitWidth(150);
                setGraphic(imageView);
            }

            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);
                // Cette méthode est appelée chaque fois que la cellule doit être mise à jour.

                if (empty || imagePath == null || imagePath.isEmpty()) {
                    // Si la cellule est vide ou si le chemin de l'image est nul/vide, supprimer l'image.
                    imageView.setImage(null);
                } else {
                    // Afficher le chemin du projet dans la console (debug)
                    String projectRoot = System.getProperty("user.dir");
                    System.out.println("Project root: " + projectRoot);

                    // Construire le chemin d'accès à la ressource.
                    String resourcePath = imagePath.startsWith("affiches/") ? imagePath : "/affiches/" + imagePath;
                    System.out.println("resourcePath : " + resourcePath);

                    // Charger l'image à partir des ressources.
                    InputStream imageStream = getClass().getResourceAsStream(resourcePath);
                    if (imageStream != null) {
                        imageView.setImage(new Image(imageStream));
                    } else {
                        // Charger une image par défaut si l'image spécifique n'est pas trouvée.
                        InputStream defaultStream = getClass().getResourceAsStream("/affiches/matrix.jpg");
                        if (defaultStream != null) {
                            // Si l'image par défaut est trouvée, l'utiliser.
                            imageView.setImage(new Image(defaultStream));
                        } else {
                            // Si l'image par défaut n'est pas trouvée, laisser l'ImageView vide.
                            imageView.setImage(null);
                            System.err.println("Image par défaut non trouvée : /affiches/matrix.jpg");
                        }
                    }
                }
            }
        };
    }

    // Apparition de la modale avec les données
    public void showFilmDetails(Film selectedFilm) throws IOException {
        FXMLLoader loader = new FXMLLoader(ModalController.class.getResource("detailModal.fxml"));
        VBox root = loader.load();
        ModalController controller = loader.getController();

        Stage modalStage = new Stage();
        controller.setStage(modalStage);  // Passez l'instance du Stage au contrôleur

        // Passer la liste des films au ModalController
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
                String imagePath = (String) filmJson.get("imagePath");
                Long id = (Long) filmJson.get("id");
                int idInt = id.intValue();

                films.add(new Film(titre, note, date, visionneParUtilisateur, acteurs, realisateur, summary, idInt, genre, imagePath));
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

    @FXML
    private void genererHTML() throws IOException {
        // Appel de la méthode main du fichier FileGenerator
        try {
            String[] args = {};
            FileGenerator.main(args);
            System.out.println("Fichier HTML généré !");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la génération du fichier.");
        }
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


