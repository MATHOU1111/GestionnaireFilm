package fr.com.gestionnairefilms;

import org.json.simple.JSONArray;
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
//            for (Object item : data) {
//                System.out.println(
//                        "titre='" + ((JSONObject) item).get("titre") + '\'' +
//                                ", note=" + ((JSONObject) item).get("note") +
//                                ", dateSortie=" + ((JSONObject) item).get("year") +
//                                ", résumé=" + ((JSONObject) item).get("summary") +
//                                ", visionneParUtilisateur=" + ((JSONObject) item).get("visionneParUtilisateur") +
//                                ", acteurs=" + ((JSONObject) item).get("actors") +
//                                ", realisateur='" + ((JSONObject) item).get("year") + '\''
//                );
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

    public static void main(String[] args) {
        JSONArray filmsData = getFilms();
        if (filmsData != null) {
            System.out.println(filmsData.toJSONString());
            // Simulate modifying the data somehow
            saveFilms(filmsData); // Call this after modifications
        } else {
            System.err.println("Failed to load films data.");
        }
    }
}