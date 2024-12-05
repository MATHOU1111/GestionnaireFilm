package fr.com.gestionnairefilms;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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

    // Saves the updated JSONArray back to the file
    public static void saveFilms(JSONArray filmData) {
        try (FileWriter file = new FileWriter(DATA_PATH)) {
            file.write(filmData.toJSONString());
            file.flush();
        } catch (IOException e) {
            System.err.println("Error writing JSON file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static JSONObject convertToJsonObject(Film film) {
        JSONObject json = new JSONObject();
        json.put("id", film.getId());
        json.put("titre", film.getTitre());
        json.put("summary", film.getSummary());
        json.put("actors", film.getActeurs());
        json.put("note", film.getNote());
        json.put("visionneParUtilisateur", film.getVisionneParUtilisateur());
        json.put("year", film.getDateSortie());
        json.put("director", film.getRealisateur());
        return json;
    }


    public static void setFilm(int index, Film object) {
        JSONArray films = getFilms();
        if (films == null) {
            System.err.println("Erreur lors du chargement des films.");
            return;
        }
        JSONObject filmJson = convertToJsonObject(object);
        films.set(index, filmJson);
        System.out.println(filmJson.toJSONString());
        saveFilms(films);
    }

    public static void supprimerFilm(int index) {
        JSONArray films = getFilms();
        if (films == null) {
            System.err.println("Erreur lors du chargement des films.");
            return;
        }
        System.out.println("Suppression du film :" + films.get(index));
        films.remove(index);

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