package fr.insa.maven.demo.demoMavenProject;

import java.util.Scanner;

public class Application {



    private FrameDemandeur frame;



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


*/


    private String getText(String text) {

        Scanner scanner = new Scanner(System.in);
        System.out.print(text);
        String UserText = scanner.nextLine(); // Lire la ligne de texte saisie par l'utilisateur
        scanner.close();
        return UserText ;



    }
}
