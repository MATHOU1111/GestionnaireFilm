package fr.com.gestionnairefilms;

public enum Genre {
    ACTION("Un film d'action"),
    COMEDIE("Une comédie hilarante"),
    DRAME("Un drame émouvant"),
    HORREUR("Un film d'horreur effrayant"),
    SCIENCE_FICTION("Un film de science-fiction captivant"),
    ROMANCE("Une histoire romantique"),
    FANTASTIQUE("Un univers fantastique"),
    DOCUMENTAIRE("Un documentaire éducatif"),
    ANIMATION("Un film d'animation"),
    AVENTURE("Une aventure palpitante");


    private final String description;

    Genre(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
    
}
