package fr.com.gestionnairefilms;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class FileGenerator {

    public static void main(String[] args) {
        try {
            // Récupération des données
            JSONArray data = FilmController.getFilms();

            if (data == null || data.isEmpty()) {
                System.err.println("Aucune donnée n'a été trouvée dans le fichier JSON.");
                return;
            }

            // Vérifier que tous les objets sont bien des JSONObject
            List<JSONObject> films = new ArrayList<>();
            for (Object obj : data) {
                if (obj instanceof JSONObject) {
                    films.add((JSONObject) obj);
                }
            }

            // 1. Nombre total d'éléments
            long nombreElements = films.size();

            // 2. La plus longue chaîne de caractères
            String plusLongueChaine = (String) films.stream()
                    .flatMap(film -> film.values().stream()) // Récupère toutes les valeurs des JSONObject
                    .filter(value -> value instanceof String) // Filtre les Strings uniquement
                    .map(String::valueOf) // Convertit en String
                    .max(Comparator.comparingInt(String::length)) // Trouve la plus longue
                    .orElse(""); // Par défaut, une chaîne vide

            int longueurPlusLongue = plusLongueChaine.length();

            // 3. Trouver la date la plus récente et son ID
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            JSONObject plusRecente = films.stream()
                    .max(Comparator.comparing(film -> LocalDate.parse((String) film.get("dateSortie"), formatter)))
                    .orElse(null);

            String datePlusRecente = plusRecente != null ? (String) plusRecente.get("dateSortie") : "N/A";
            Long idPlusRecente = plusRecente != null ? (Long) plusRecente.get("id") : null;

            // 4. Éléments avec une date future
            List<JSONObject> elementsDateFuture = films.stream()
                    .filter(film -> LocalDate.parse((String) film.get("dateSortie"), formatter).isAfter(LocalDate.now()))
                    .collect(Collectors.toList());

            // 5. Éléments où "visionneParUtilisateur" est false
            List<JSONObject> elementsBooleenFaux = films.stream()
                    .filter(film -> !(Boolean) film.get("visionneParUtilisateur"))
                    .collect(Collectors.toList());

            // 6. Acteurs les plus fréquents
            Map<String, Long> elementsFrequents = films.stream()
                    .flatMap(film -> ((List<String>) film.get("actors")).stream())
                    .collect(Collectors.groupingBy(actor -> actor, Collectors.counting()));

            Map.Entry<String, Long> plusFrequent = elementsFrequents.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .orElse(null);

            // 7. Valeurs distinctes des listes d'acteurs
            Set<String> valeursDistinctes = films.stream()
                    .flatMap(film -> ((List<String>) film.get("actors")).stream())
                    .collect(Collectors.toSet());

            // Génération du contenu HTML
            StringBuilder htmlContent = new StringBuilder();
            htmlContent.append("<html><head><link rel=\"stylesheet\" href=\"src/resources/css/stream.css\"><title>Analyse des Films</title></head><body>");
            htmlContent.append("<h1>Résultats de l'analyse</h1>");
            htmlContent.append("<p>Il y a ").append(nombreElements).append(" éléments.</p>");
            htmlContent.append("<p>La plus longue chaîne de caractères est '<b>").append(plusLongueChaine)
                    .append("</b>' avec une longueur de ").append(longueurPlusLongue).append(" caractères.</p>");
            htmlContent.append("<p>La date la plus récente est le ").append(datePlusRecente)
                    .append(" correspondant à l'ID ").append(idPlusRecente).append(".</p>");
            htmlContent.append("<p>Les éléments dont la date dépasse la date d'aujourd'hui sont : ")
                    .append(elementsDateFuture).append(".</p>");
            htmlContent.append("<p>Les éléments dont le booléen est faux sont : ").append(elementsBooleenFaux).append(".</p>");
            if (plusFrequent != null) {
                htmlContent.append("<p>L'acteur le plus fréquent est '<b>").append(plusFrequent.getKey())
                        .append("</b>' avec ").append(plusFrequent.getValue()).append(" occurrences.</p>");
            }
            htmlContent.append("<p>Toutes les valeurs distinctes des champs de type liste sont : ")
                    .append(valeursDistinctes).append(".</p>");
            htmlContent.append("</body></html>");

            // Écriture dans un fichier HTML
            try (FileWriter writer = new FileWriter("analyse_films.html")) {
                writer.write(htmlContent.toString());
            }

            System.out.println("Fichier HTML généré avec succès : analyse_films.html");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
