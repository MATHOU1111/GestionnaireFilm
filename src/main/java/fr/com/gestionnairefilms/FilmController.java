package fr.com.gestionnairefilms;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;

public class FilmController {

    // Retourne un JSONARRAY des éléments de data.json
    public static JSONArray getFilms() {

        JSONArray data = null;
        try {
            JSONParser parser = new JSONParser();
            data = (JSONArray) parser.parse(new FileReader("C:\\workflow\\ESGI3\\GestionnaireFilms\\src\\main\\resources\\data\\data.json"));
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

    public static void main(String[] args) {
        JSONArray filmsData = getFilms();
        System.out.println(filmsData);
    }
}
