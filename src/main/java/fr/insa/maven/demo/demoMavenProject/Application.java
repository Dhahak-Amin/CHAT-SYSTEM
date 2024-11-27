package fr.insa.maven.demo.demoMavenProject;

import java.util.Scanner;

public class Application {




    private FrameDemandeur frame;



    private User user;

/*
    private FrameDemandeur frame; // Supposition que Frame est une classe utilisée pour gérer l'interface
    private Demandeur demandeur
    private static final Scanner scanner = new Scanner(System.in); // Scanner unique pour toute la classe
>>>>>>> d84b19edeb6e937aad110453e26e87d060aa3a4a

    public Application() {
        frame = new FrameDemandeur(AllMissions allMissions, Demandeur demandeur); // Initialisation de l'objet Frame

        String typeUser = getText("Demandeur, validateur ou benevole ?");

        if (typeUser.equalsIgnoreCase("demandeur")) {
            user = createDemandeur();
        } else if (typeUser.equalsIgnoreCase("benevole")) {
            user = createBenevole();
        } else if (typeUser.equalsIgnoreCase("validateur")) {
            user = createValidateur();
        } else {
            System.out.println("L'utilisateur n'est ni un Demandeur, ni un Benevole, ni un Validateur.");
        }
    }

    // Exemple de création de Demandeur
    private Demandeur createDemandeur() {
        String nom = getText("Quel est ton nom ? ");
        String prenom = getText("Quel est ton prénom ? ");
        String besoin = getText("Quel est ton besoin ? ");
        String service = getText("Quel service cherches-tu ? ");
        String place = getText("Quel est l'emplacement ? (ex: HOME) ");
        String email = getText("Ton email ? ");
        String password = getText("Ton mot de passe ? ");

        return new Demandeur(nom, prenom, besoin, service, Place.valueOf(place.toUpperCase()), email, password);
    }
  /*  private Mission createMission(User user) {
        if (user instanceof Demandeur) {
            String intitule = getText("Quel est ta demande");
            // Création d'une nouvelle mission
            Mission mission = new Mission( intitule, user);
            return mission; // Retourner la mission créée
        } else {
            // Gérer le cas où l'utilisateur n'est pas un Demandeur
            System.out.println("L'utilisateur doit être un Demandeur pour créer une mission.");
            return null; // Ou lancer une exception selon le contexte
        }
    }

    // Exemple de création de Benevole
    private Benevole createBenevole() {
        String nom = getText("Quel est ton nom ? ");
        String prenom = getText("Quel est ton prénom ? ");
        String email = getText("Ton email ? ");
        String password = getText("Ton mot de passe ? ");
        String metier = getText("Quel est ton métier ? ");

        return new Benevole(nom, prenom, email, password, metier);
    }

    // Exemple de création de Validateur



    private Validateur createValidateur() {
        String nom = getText("Quel est ton nom ? ");
        String prenom = getText("Quel est ton prénom ? ");
        String email = getText("Ton email ? ");
        String password = getText("Ton mot de passe ? ");

        return new Validateur(nom, prenom, email, password);
    }

    // Méthode pour demander un texte à l'utilisateur
    private String getText(String text) {
        System.out.print(text);
        return scanner.nextLine(); // Lire la ligne de texte saisie par l'utilisateur
    }
 */

}
