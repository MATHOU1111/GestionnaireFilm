package fr.com.gestionnairefilms;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FilmController {

    private static final String DATA_PATH = "C:\\workflow\\ESGI3\\GestionnaireFilms\\src\\main\\resources\\data\\data.json";

    // Retourne un JSONARRAY des éléments de data.json
    public static JSONArray getFilms() {

        JSONArray data = null;
        try {
            JSONParser parser = new JSONParser();
            data = (JSONArray) parser.parse(new FileReader(DATA_PATH));
//            System.out.println(data);
//            for (Object item : data) {
//                System.out.println(((JSONObject) item).get("titre") + " ") ;
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    // Sauvegarde le JSONArray dans le fichier data.json
    public static void saveFilms(JSONArray filmData) {
        try (FileWriter file = new FileWriter(DATA_PATH)) {
            file.write(filmData.toJSONString());
            file.flush();
        } catch (IOException e) {
            System.err.println("Error writing JSON file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // convertir en json le film donné
    public static JSONObject convertToJsonObject(Film film) {

        JSONObject json = new JSONObject();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateSortieString = film.getDateSortie().format(formatter);

        json.put("id", film.getId());
        json.put("titre", film.getTitre());
        json.put("summary", film.getSummary());
        json.put("actors", film.getActeurs());
        json.put("note", film.getNote());
        json.put("visionneParUtilisateur", film.getVisionneParUtilisateur());
        json.put("dateSortie",dateSortieString);
        json.put("director", film.getRealisateur());
        return json;
    }


    // Methode pour mettre à jour le film côté data.json
    public static void setFilm(int id, Film object) {
        JSONArray films = getFilms(); // Charger les films existants
        if (films == null) {
            System.err.println("Erreur lors du chargement des films.");
            return;
        }

        // Convertir le film en JSONObject
        JSONObject filmJson = convertToJsonObject(object);

        // Rechercher le film à mettre à jour par ID
        boolean filmUpdated = false;
        for (int i = 0; i < films.size(); i++) {
            Object element = films.get(i);
            if (element instanceof JSONObject film) {

                // Vérifier si l'ID correspond
                Object filmIdObj = film.get("id");
                if (filmIdObj != null && Integer.parseInt(filmIdObj.toString()) == id) {
                    // Remplacer l'élément existant
                    films.set(i, filmJson);
                    filmUpdated = true;
                    break;
                }
            }
        }

        if (!filmUpdated) {
            System.err.println("Film avec l'ID " + id + " introuvable pour mise à jour.");
            return;
        }

        // Sauvegarder les films mis à jour
        saveFilms(films);
        System.out.println("Film mis à jour : " + filmJson.toJSONString());
    }

    // Methode de suppression d'un film dans le json
    public static void supprimerFilm(String titre) {
        JSONArray films = getFilms();
        if (films == null) {
            System.err.println("Erreur lors du chargement des films.");
            return;
        }
        System.out.println("Suppression du film :" + titre);
        int compteur = Integer.parseInt(Objects.requireNonNull(searchFilmInJson(titre)));
        System.out.println(compteur);
        films.remove(compteur);
        saveFilms(films);
    }

    // Cette fonction va venir rechercher le film donné dans le JSONArray pour renvoyer son compteur
    static String searchFilmInJson(String id) {
        JSONArray films = getFilms(); // Charge la liste des films

        for (Object element : films) {
            if (element instanceof JSONObject film) {

                // Récupération de l'ID en tant que String pour éviter les erreurs de type
                Object filmIdObj = film.get("id");
                if (filmIdObj != null) {
                    String filmId = filmIdObj.toString(); // Convertir l'ID en chaîne de caractères

                    if (filmId.equals(id)) {
                        System.out.println("Film trouvé: " + film);
                        return filmId; // Retourne l'ID trouvé
                    }
                }
            }
        }

        System.out.println("Film non trouvé.");
        return null; // Retourne null si aucun film n'est trouvé
    }


    public static void main(String[] args) {
        JSONArray filmsData = getFilms();
        if (filmsData != null) {
            System.out.println(filmsData.toJSONString());
        } else {
            System.err.println("Failed to load films data.");
        }
    }
}