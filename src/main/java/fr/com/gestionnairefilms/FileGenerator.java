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
            // Récupération des données des films
            JSONArray data = FilmController.getFilms();

            if (data == null || data.isEmpty()) {
                System.err.println("Aucune donnée n'a été trouvée dans le fichier JSON.");
                return;
            }

            // Filtrer et collecter uniquement les objets JSON valides
            List<JSONObject> films = new ArrayList<>();
            for (Object obj : data) {
                if (obj instanceof JSONObject) {
                    films.add((JSONObject) obj);
                }
            }

            // Début de la construction du contenu HTML
            StringBuilder htmlContent = new StringBuilder();
            htmlContent.append("<html><head><title>Analyse des Films</title></head><body>");
            htmlContent.append("<h1>Résultats de l'analyse</h1>");

            // 1. Calcul du nombre total d'éléments
            long nombreElements = films.size();
            htmlContent.append("<p>Il y a <b>").append(nombreElements).append("</b> éléments.</p>");

            // 2. Recherche de la plus longue chaîne de caractères dans les valeurs
            String plusLongueChaine = (String) films.stream()
                    .flatMap(film -> film.values().stream())
                    .filter(value -> value instanceof String)
                    .map(String::valueOf)
                    .max(Comparator.comparingInt(String::length))
                    .orElse("");

            int longueurPlusLongue = plusLongueChaine.length();
            htmlContent.append("<p>La plus longue chaîne de caractères est '<b>").append(plusLongueChaine)
                    .append("</b>' avec une longueur de <b>").append(longueurPlusLongue).append("</b> caractères.</p>");

            // 3. Trouver la date de sortie la plus récente et son ID associé
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            JSONObject plusRecente = films.stream()
                    .max(Comparator.comparing(film -> LocalDate.parse((String) film.get("dateSortie"), formatter)))
                    .orElse(null);

            String datePlusRecente = plusRecente != null ? (String) plusRecente.get("dateSortie") : "N/A";
            Long idPlusRecente = plusRecente != null ? (Long) plusRecente.get("id") : null;
            htmlContent.append("<p>La date la plus récente est le <b>").append(datePlusRecente)
                    .append("</b> correspondant à l'ID <b>").append(idPlusRecente).append("</b>.</p>");

            // 4. Filtrer les éléments ayant une date de sortie future
            List<JSONObject> elementsDateFuture = films.stream()
                    .filter(film -> LocalDate.parse((String) film.get("dateSortie"), formatter).isAfter(LocalDate.now()))
                    .collect(Collectors.toList());

            htmlContent.append("<p>Les éléments dont la date dépasse la date d'aujourd'hui sont : <pre>")
                    .append(elementsDateFuture).append("</pre>.</p>");

            // 5. Filtrer les éléments où "visionneParUtilisateur" est faux
            List<JSONObject> elementsBooleenFaux = films.stream()
                    .filter(film -> !(Boolean) film.get("visionneParUtilisateur"))
                    .collect(Collectors.toList());

            htmlContent.append("<p>Les éléments dont le booléen est faux sont : <pre>").append(elementsBooleenFaux)
                    .append("</pre>.</p>");

            // 6. Identifier les acteurs les plus fréquents dans les listes d'acteurs
            Map<String, Long> elementsFrequents = films.stream()
                    .flatMap(film -> ((List<String>) film.get("actors")).stream())
                    .collect(Collectors.groupingBy(actor -> actor, Collectors.counting()));

            Map.Entry<String, Long> plusFrequent = elementsFrequents.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .orElse(null);

            if (plusFrequent != null) {
                htmlContent.append("<p>L'acteur le plus fréquent est '<b>").append(plusFrequent.getKey())
                        .append("</b>' avec <b>").append(plusFrequent.getValue()).append("</b> occurrences.</p>");
            }

            // 7. Extraire toutes les valeurs distinctes des champs de type liste (acteurs)
            Set<String> valeursDistinctes = films.stream()
                    .flatMap(film -> ((List<String>) film.get("actors")).stream())
                    .collect(Collectors.toSet());

            htmlContent.append("<p>Toutes les valeurs distinctes des champs de type liste sont : <pre>")
                    .append(valeursDistinctes).append("</pre>.</p>");

            // Fin du contenu HTML
            htmlContent.append("</body></html>");

            // Écrire le contenu généré dans un fichier HTML
            try (FileWriter writer = new FileWriter("analyse_films.html")) {
                writer.write(htmlContent.toString());
            }

            System.out.println("Fichier HTML généré avec succès : analyse_films.html");

        } catch (Exception e) {
            // Gestion des erreurs
            e.printStackTrace();
        }
    }
}