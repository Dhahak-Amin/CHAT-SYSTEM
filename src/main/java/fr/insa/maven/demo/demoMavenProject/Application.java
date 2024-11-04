package fr.insa.maven.demo.demoMavenProject;

import java.util.Scanner;

public class Application {


    private Frame frame;
    private User user;

    public Application() {

        String TypeUser = getText("Demandeur, validateur ou benevole ?");



        if (TypeUser.equals("demandeur") || TypeUser.equals("Demandeur")) {
            user = CreateDemandeur();

        } else if (TypeUser.equals("benevole") || TypeUser.equals("Benevole")) {
            user= CreateBenevole();
        }
        else if (TypeUser.equals("Validateur") || TypeUser.equals("validateur")) {
            user= CreateValidateur();

        }else {
            System.out.println("L'utilisateur n'est ni un Demandeur ni un Benevole.");
        }




    }

    private Demandeur CreateDemandeur() {

        String Nom = getText("Quel est ton nom ?") ;
        String Prenom = getText("Quel est ton Prenom ?") ;
        String description = getText("Décris toi en quelques lignes") ;
        String needs = getText("De quoi as-tu besoin ?") ;
        String location =getText("où habites tu ?") ;
        String email = getText("ton mail ? ") ;
        String password = getText("ur password ?") ;


        Demandeur demandeur = new Demandeur(Nom,Prenom,description,needs,location,email,password);
        return demandeur;
    }
    private Benevole CreateBenevole() {

        String Nom = getText("Quel est ton nom ?") ;
        String Prenom = getText("Quel est ton Prenom ?") ;
        String email = getText("ton mail ? ") ;
        String password = getText("ur password ?") ;


        Benevole benevole = new Benevole(Nom,Prenom,email,password);
        return benevole;
    }
    private Validateur CreateValidateur() {

        String Nom = getText("Quel est ton nom ?") ;
        String Prenom = getText("Quel est ton Prenom ?") ;
        String email = getText("ton mail ? ") ;
        String password = getText("ur password ?") ;

        Validateur validateur = new Validateur(Nom,Prenom,email,password);
        return validateur;
    }




    private String getText(String text) {

        Scanner scanner = new Scanner(System.in);
        System.out.print(text);
        String UserText = scanner.nextLine(); // Lire la ligne de texte saisie par l'utilisateur
        scanner.close();
        return UserText ;



    }
}
