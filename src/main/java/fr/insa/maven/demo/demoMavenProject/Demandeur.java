package fr.insa.maven.demo.demoMavenProject;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Demandeur extends User {
    private String description;
    private String needs;
    private Place location; // Assurez-vous que Place est un enum ou une classe valide
    private static final Scanner scanner = new Scanner(System.in); // Scanner unique pour toute la classe



    private List<Mission> Mymissions;

    private FrameDemandeur frameDemandeur;





    public Demandeur(String firstname, String lastname, String description, String needs,Place location, String email, String password) {
        super(firstname, lastname,email,password);
        this.description = description;
        this.needs = needs;
        this.location = location;
        this.Mymissions= CreateMymissions(AllMissions.getInstance());
    }


    // Getters et setters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }







    public Place getLocation() {
        return location;
    }

    public void setLocation(Place location) {
        this.location = location;
    }

    // Méthode pour créer une mission

    public List<Mission> CreateMymissions(AllMissions allMissions) {
        // Créer une nouvelle instance d'AllMissions pour stocker les missions acceptées
        List<Mission>acceptedMissions = new ArrayList<>();

        // Parcourir toutes les missions
        for (Mission mission : allMissions.getMissions()) {
            // Vérifier si le bénévole de la mission est celui courant (this)
            if (this.equals(mission.getDemandeur())) {
                // Ajouter la mission à la liste des missions acceptées
                acceptedMissions.add(mission);
            }
        }

        // Retourner l'objet AllMissions contenant uniquement les missions du bénévole courant
        return acceptedMissions;
    }

    public void UpdateMymissions(AllMissions allMissions) {
        // Créer une nouvelle instance d'AllMissions pour stocker les missions acceptées


        // Parcourir toutes les missions
        for (Mission mission : allMissions.getMissions()) {
            // Vérifier si le bénévole de la mission est celui courant (this)
            if (this.equals(mission.getDemandeur())) {
                // Ajouter la mission à la liste des missions acceptées
                this.Mymissions.add(mission);
            }

        }


        // Retourner l'objet AllMissions contenant uniquement les missions du bénévole courant

    }
    public Mission createMission(String intitule, Place place) {
        // Création d'une nouvelle mission
        Mission mission = new Mission(MissionEtat.EN_ATTENTE_AFFECTATION, intitule, this, place);
        return mission; // Retourner la mission créée
    }

    // Méthode pour choisir un lieu
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
                scanner.next(); // Ignorer l'entrée non valide
            }
            choice = scanner.nextInt();
        } while (choice < 1 || choice > places.length);

        return places[choice - 1]; // Retourner la place choisie
    }





    public String getNeeds() {
        return needs;
    }

    public void setNeeds(String needs) {
        this.needs = needs;
    }



    public List<Mission> getMissions() {
        return Mymissions;
    }

    public void setMissions(List<Mission> missions) {
        this.Mymissions = missions;
    }


    private String getText(String text) {
        System.out.print(text);
        scanner.nextLine(); // Consommer la nouvelle ligne restante
        return scanner.nextLine(); // Lire la ligne de texte saisie par l'utilisateur
    }






    

}
