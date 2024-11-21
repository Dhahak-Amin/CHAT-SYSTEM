package fr.insa.maven.demo.demoMavenProject;


import java.util.Scanner;

public class Demandeur extends User {
    private String description;
    private String needs;
    private String location;

    public Demandeur(String firstname, String lastname, String description, String needs, String location, String email, String password) {
        super(firstname, lastname,email,password);
        this.description = description;
        this.needs = needs;
        this.location = location;
    }

    // Getters and setters
    public String getDescription() {
        return description;
    }

    public Mission createMission(String intitule , Place place) {

            // Création d'une nouvelle mission
            Mission mission= new Mission("En attente", intitule,this, place );
            return mission; // Retourner la mission créée
        }


    private Place choosePlace() {
        Scanner scanner = new Scanner(System.in);
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



        public void setDescription(String description) {
        this.description = description;
    }

    public String getNeeds() {
        return needs;
    }

    public void setNeeds(String needs) {
        this.needs = needs;
    }

    public String getLocation() {
        return location;
    }

    private String getText(String text) {

        Scanner scanner = new Scanner(System.in);
        System.out.print(text);
        String UserText = scanner.nextLine(); // Lire la ligne de texte saisie par l'utilisateur
        scanner.close();
        return UserText ;



    }

    public void setLocation(String location) {
        this.location = location;
    }
    
    
    
    
}
