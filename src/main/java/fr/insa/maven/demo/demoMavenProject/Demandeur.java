package fr.insa.maven.demo.demoMavenProject;

import java.util.Scanner;

public class Demandeur extends User {
    private String description;
    private String needs;
    private Place location; // Assurez-vous que le type est Place
    private static final Scanner scanner = new Scanner(System.in); // Scanner unique pour toute la classe

    public Demandeur(String firstname, String lastname, String description, String needs, Place location, String email, String password) {
        super(firstname, lastname, email, password);
        this.description = description;
        this.needs = needs;
        this.location = location; // Cela doit être de type Place
    }

    // Getters and setters
    public String getDescription() {
        return description;
    }

<<<<<<< Updated upstream
    public Mission createMission(String intitule , Place place) {

            // Création d'une nouvelle mission
            Mission mission= new Mission("En attente", intitule,this, place );
            return mission; // Retourner la mission créée
        }
=======
    public void setDescription(String description) {
        this.description = description;
    }

    public String getNeeds() {
        return needs;
    }

    public void setNeeds(String needs) {
        this.needs = needs;
    }
>>>>>>> Stashed changes

    public Place getLocation() { // Correction ici pour le getter
        return location;
    }

<<<<<<< Updated upstream
    private Place choosePlace() {
        Scanner scanner = new Scanner(System.in);
=======
    public void setLocation(Place location) { // Correction ici pour le setter
        this.location = location;
    }

    // Méthode pour créer une mission
    private Mission createMission() {
        String intitule = getText("Quel est ta demande : ");
        Place place = choosePlace();
        return new Mission(intitule, place, MissionEtat.EN_ATTENTE_AFFECTATION, this); // Correction du constructeur de Mission
    }

    // Méthode pour choisir un lieu
    public Place choosePlace() {
>>>>>>> Stashed changes
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
                scanner.next(); // Ignorer l'entrée non valide
            }
            choice = scanner.nextInt();
        } while (choice < 1 || choice > places.length);

        return places[choice - 1]; // Retourner la place choisie
    }

    // Méthode pour obtenir du texte de l'utilisateur
    private String getText(String text) {
        System.out.print(text);
        return scanner.nextLine(); // Lire la ligne de texte saisie par l'utilisateur
    }
}
