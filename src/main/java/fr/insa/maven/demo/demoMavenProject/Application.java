package fr.insa.maven.demo.demoMavenProject;

import java.util.Scanner;

public class Application {


    private Frame frame;

    public Application() {
        Frame frame = new Frame();
        String TypeUser = getText("Demandeur, validateur ou benevole ?");



        if (TypeUser.equals("demandeur") || TypeUser.equals("Demandeur")) {
            Demandeur demander = CreateDemandeur();

        } else if (TypeUser.equals("benevole") || TypeUser.equals("Benevole")) {
            Benevole benevole = CreateBenevole();
        }
        else if (TypeUser.equals("Validateur") || TypeUser.equals("validateur")) {
            Validateur validateur = CreateValidateur();

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

        Demandeur demandeur = new Demandeur(Nom,Prenom,description,needs,location);
        return demandeur;
    }
    private Benevole CreateBenevole() {

        String Nom = getText("Quel est ton nom ?") ;
        String Prenom = getText("Quel est ton Prenom ?") ;


        Benevole benevole = new Benevole(Nom,Prenom);
        return benevole;
    }
    private Validateur CreateValidateur() {

        String Nom = getText("Quel est ton nom ?") ;
        String Prenom = getText("Quel est ton Prenom ?") ;


        Validateur validateur = new Validateur(Nom,Prenom);
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
