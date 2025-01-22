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
                // Vérification si les données sont disponibles
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

            // 1. Calcul du nombre total d'éléments
            long nombreElements = films.size();

            // 2. Recherche de la plus longue chaîne de caractères dans les valeurs
            String plusLongueChaine = (String) films.stream()
                    .flatMap(film -> film.values().stream()) // Extraire les valeurs des JSONObject
                    .filter(value -> value instanceof String) // Filtrer uniquement les chaînes
                    .map(String::valueOf) // Convertir en String
                    .max(Comparator.comparingInt(String::length)) // Trouver la chaîne la plus longue
                    .orElse(""); // Valeur par défaut

            int longueurPlusLongue = plusLongueChaine.length();

            // 3. Trouver la date de sortie la plus récente et son ID associé
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            JSONObject plusRecente = films.stream()
                    .max(Comparator.comparing(film -> LocalDate.parse((String) film.get("dateSortie"), formatter)))
                    .orElse(null);

            String datePlusRecente = plusRecente != null ? (String) plusRecente.get("dateSortie") : "N/A";
            Long idPlusRecente = plusRecente != null ? (Long) plusRecente.get("id") : null;

            // 4. Filtrer les éléments ayant une date de sortie future
            List<JSONObject> elementsDateFuture = films.stream()
                    .filter(film -> LocalDate.parse((String) film.get("dateSortie"), formatter).isAfter(LocalDate.now()))
                    .collect(Collectors.toList());

            // 5. Filtrer les éléments où "visionneParUtilisateur" est faux
            List<JSONObject> elementsBooleenFaux = films.stream()
                    .filter(film -> !(Boolean) film.get("visionneParUtilisateur"))
                    .collect(Collectors.toList());

            // 6. Identifier les acteurs les plus fréquents dans les listes d'acteurs
            Map<String, Long> elementsFrequents = films.stream()
                    .flatMap(film -> ((List<String>) film.get("actors")).stream())
                    .collect(Collectors.groupingBy(actor -> actor, Collectors.counting()));

            Map.Entry<String, Long> plusFrequent = elementsFrequents.entrySet().stream()
                    .max(Map.Entry.comparingByValue()) // Trouver l'acteur le plus fréquent
                    .orElse(null);

            // 7. Extraire toutes les valeurs distinctes des champs de type liste (acteurs)
            Set<String> valeursDistinctes = films.stream()
                    .flatMap(film -> ((List<String>) film.get("actors")).stream())
                    .collect(Collectors.toSet());

            // Générer le contenu HTML pour les résultats de l'analyse
            StringBuilder htmlContent = new StringBuilder();
            htmlContent.append("<html><head><title>Analyse des Films</title></head><body>");
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
