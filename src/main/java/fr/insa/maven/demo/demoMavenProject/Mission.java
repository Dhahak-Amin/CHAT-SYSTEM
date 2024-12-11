package fr.insa.maven.demo.demoMavenProject;

/**
 * Classe représentant une mission.
 * Une mission est caractérisée par son état, son intitulé, son lieu, le demandeur,
 * le bénévole assigné (s'il y en a un), et un motif associé (s'il est défini).
 */
public class Mission {

    private MissionEtat etat;      // État actuel de la mission
    private Motif motif;           // Motif associé à la mission (en cas d'invalidation)
    private Benevole benevole;     // Bénévole assigné à la mission
    private Place place;           // Lieu de la mission
    private String intitule;       // Intitulé ou description de la mission
    private Demandeur demandeur;   // Demandeur ayant créé la mission

    /**
     * Constructeur pour créer une mission avec un état par défaut.
     *
     * @param intitule  Intitulé de la mission.
     * @param demandeur Demandeur ayant créé la mission.
     * @param place     Lieu de la mission.
     */
    public Mission(String intitule, Demandeur demandeur, Place place) {
        this.etat = MissionEtat.EN_ATTENTE_AFFECTATION;
        this.place = place;
        this.intitule = intitule;
        this.demandeur = demandeur;
    }

    /**
     * Constructeur pour créer une mission avec un état spécifique.
     *
     * @param etat      État initial de la mission.
     * @param intitule  Intitulé de la mission.
     * @param demandeur Demandeur ayant créé la mission.
     * @param place     Lieu de la mission.
     */
    public Mission(MissionEtat etat, String intitule, Demandeur demandeur, Place place) {
        this.etat = etat;
        this.place = place;
        this.intitule = intitule;
        this.demandeur = demandeur;
    }

    /**
     * Constructeur pour créer une mission avec un bénévole assigné.
     *
     * @param etat      État initial de la mission.
     * @param intitule  Intitulé de la mission.
     * @param demandeur Demandeur ayant créé la mission.
     * @param place     Lieu de la mission.
     * @param benevole  Bénévole assigné à la mission.
     */
    public Mission(MissionEtat etat, String intitule, Demandeur demandeur, Place place, Benevole benevole) {
        this.etat = etat;
        this.place = place;
        this.intitule = intitule;
        this.demandeur = demandeur;
        this.benevole = benevole;
    }

    // Getters et Setters

    /**
     * Retourne l'intitulé de la mission.
     *
     * @return L'intitulé de la mission.
     */
    public String getIntitule() {
        return intitule;
    }

    /**
     * Définit l'intitulé de la mission.
     *
     * @param intitule Le nouvel intitulé.
     */
    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    /**
     * Retourne le lieu de la mission.
     *
     * @return Le lieu de la mission.
     */
    public Place getPlace() {
        return place;
    }

    /**
     * Définit le lieu de la mission.
     *
     * @param place Le nouveau lieu.
     */
    public void setPlace(Place place) {
        this.place = place;
    }

    /**
     * Retourne l'état actuel de la mission.
     *
     * @return L'état de la mission.
     */
    public MissionEtat getEtat() {
        return etat;
    }

    /**
     * Définit l'état de la mission.
     *
     * @param etat Le nouvel état.
     */
    public void setEtat(MissionEtat etat) {
        this.etat = etat;
    }

    /**
     * Retourne le demandeur ayant créé la mission.
     *
     * @return Le demandeur.
     */
    public Demandeur getDemandeur() {
        return demandeur;
    }

    /**
     * Définit le demandeur ayant créé la mission.
     *
     * @param demandeur Le nouveau demandeur.
     */
    public void setDemandeur(Demandeur demandeur) {
        this.demandeur = demandeur;
    }

    /**
     * Retourne le motif associé à la mission.
     *
     * @return Le motif de la mission.
     */
    public Motif getMotif() {
        return motif;
    }

    /**
     * Définit le motif associé à la mission.
     *
     * @param motif Le nouveau motif.
     */
    public void setMotif(Motif motif) {
        this.motif = motif;
    }

    /**
     * Retourne le bénévole assigné à la mission.
     *
     * @return Le bénévole assigné.
     */
    public Benevole getBenevole() {
        return benevole;
    }

    /**
     * Définit le bénévole assigné à la mission.
     *
     * @param benevole Le nouveau bénévole.
     */
    public void setBenevole(Benevole benevole) {
        this.benevole = benevole;
    }
}
