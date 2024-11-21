package fr.insa.maven.demo.demoMavenProject;

import java.util.Scanner;

public class Application {

<<<<<<< Updated upstream

    private FrameDemandeur frame;
=======
    private Frame frame;
>>>>>>> Stashed changes
    private User user;
    private static final Scanner scanner = new Scanner(System.in); // Scanner unique pour toute la classe

    public Application() {
<<<<<<< Updated upstream

        String TypeUser = getText("Demandeur, validateur ou benevole ?");



        if (TypeUser.equals("demandeur") || TypeUser.equals("Demandeur")) {
            user = CreateDemandeur();

        } else if (TypeUser.equals("benevole") || TypeUser.equals("Benevole")) {
            user= CreateBenevole();
        }
        else if (TypeUser.equals("Validateur") || TypeUser.equals("validateur")) {
            user= CreateValidateur();

        }else {
=======
        frame = new Frame();
        String typeUser = getText("Demandeur, validateur ou benevole ?");

        if (typeUser.equalsIgnoreCase("demandeur")) {
            user = demandeur;
        } else if (typeUser.equalsIgnoreCase("benevole")) {
            user = createBenevole();
        } else if (typeUser.equalsIgnoreCase("validateur")) {
           // user = createValidateur();
        } else {
>>>>>>> Stashed changes
            System.out.println("L'utilisateur n'est ni un Demandeur ni un Benevole.");
        }
    }


        // Exemple de création de Demandeur
        Demandeur demandeur = new Demandeur("Alice", "Dupont", "Besoin d'aide", "Ménage", Place.HOME, "alice@example.com", "password123");


    private Benevole createBenevole() {
        String nom = getText("Quel est ton nom ? ");
        String prenom = getText("Quel est ton Prenom ? ");
        String email = getText("Ton mail ? ");
        String password = getText("Ton mot de passe ? ");
        String metier = getText("Quel est ton métier ? ");
        return new Benevole(nom, prenom, email, password,metier);
    }

    /*private Validateur createValidateur() {
        String nom = getText("Quel est ton nom ? ");
        String prenom = getText("Quel est ton Prenom ? ");
        String email = getText("Ton mail ? ");
        String password = getText("Ton mot de passe ? ");

        Validateur validateur = new Validateur(nom, prenom, email, password);
        return *validateur;
    }*/

    private String getText(String text) {
        System.out.print(text);
        return scanner.nextLine(); // Lire la ligne de texte saisie par l'utilisateur
    }
}
