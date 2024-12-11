package fr.insa.maven.demo.demoMavenProject;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Classe représentant un demandeur.
 * Un demandeur est un utilisateur qui peut créer des missions et suivre leurs statuts.
 */
public class Demandeur extends User {
    private String description;         // Description du demandeur
    private String needs;              // Besoins spécifiques du demandeur
    private Place location;            // Lieu principal du demandeur
    private static final Scanner scanner = new Scanner(System.in); // Scanner unique pour toute la classe
    private List<Mission> myMissions;  // Liste des missions associées au demandeur

    /**
     * Constructeur pour créer un demandeur.
     *
     * @param firstname  Prénom du demandeur.
     * @param lastname   Nom de famille du demandeur.
     * @param description Description du demandeur.
     * @param needs       Besoins spécifiques du demandeur.
     * @param location    Lieu principal du demandeur.
     * @param email       Email du demandeur.
     * @param password    Mot de passe du demandeur.
     */
    public Demandeur(String firstname, String lastname, String description, String needs, Place location, String email, String password) {
        super(firstname, lastname, email, password);
        this.description = description;
        this.needs = needs;
        this.location = location;
        this.myMissions = createMyMissions(AllMissions.getInstance());
    }

    // Getters et Setters

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNeeds() {
        return needs;
    }

    public void setNeeds(String needs) {
        this.needs = needs;
    }

    public Place getLocation() {
        return location;
    }

    public void setLocation(Place location) {
        this.location = location;
    }

    public List<Mission> getMyMissions() {
        return myMissions;
    }

    public void setMyMissions(List<Mission> missions) {
        this.myMissions = missions;
    }

    // Méthodes principales

    /**
     * Initialise la liste des missions associées au demandeur.
     *
     * @param allMissions Instance globale contenant toutes les missions.
     * @return Liste des missions du demandeur.
     */
    public List<Mission> createMyMissions(AllMissions allMissions) {
        List<Mission> acceptedMissions = new ArrayList<>();
        for (Mission mission : allMissions.getMissions()) {
            if (this.equals(mission.getDemandeur())) {
                acceptedMissions.add(mission);
            }
        }
        return acceptedMissions;
    }

    /**
     * Met à jour la liste des missions associées au demandeur.
     *
     * @param allMissions Instance globale contenant toutes les missions.
     */
    public void updateMyMissions(AllMissions allMissions) {
        for (Mission mission : allMissions.getMissions()) {
            if (this.equals(mission.getDemandeur()) && !myMissions.contains(mission)) {
                this.myMissions.add(mission);
            }
        }
    }

    /**
     * Crée une nouvelle mission.
     *
     * @param intitule Intitulé de la mission.
     * @param place    Lieu de la mission.
     * @return La mission créée.
     */
    public Mission createMission(String intitule, Place place) {
        return new Mission(MissionEtat.EN_ATTENTE_AFFECTATION, intitule, this, place);
    }

    /**
     * Permet au demandeur de choisir un lieu pour une mission.
     *
     * @return Le lieu choisi.
     */
    public Place choosePlace() {
        Place[] places = Place.values();
        System.out.println("Choisissez un endroit pour la mission :");
        for (int i = 0; i < places.length; i++) {
            System.out.println((i + 1) + ". " + places[i]);
        }

        int choice;
        do {
            System.out.print("Entrez le numéro de votre choix : ");
            while (!scanner.hasNextInt()) {
                System.out.println("Veuillez entrer un nombre valide.");
                scanner.next();
            }
            choice = scanner.nextInt();
        } while (choice < 1 || choice > places.length);

        return places[choice - 1];
    }

    /**
     * Demande et retourne une chaîne de texte saisie par l'utilisateur.
     *
     * @param prompt Texte à afficher pour l'utilisateur.
     * @return Texte saisi par l'utilisateur.
     */
    private String getText(String prompt) {
        System.out.print(prompt);
        scanner.nextLine(); // Consommer la nouvelle ligne restante
        return scanner.nextLine();
    }
}
