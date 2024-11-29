package fr.insa.maven.demo.demoMavenProject;

import java.util.ArrayList;
import java.util.List;

public class Benevole extends User {

    private FrameBenevole frameBenevole ;




    private List<Mission> acceptedMissions;

    //private List<Mission> acceptedMissions;
    private String metier; // Métier du bénévole


    public Benevole(String firstname, String lastname, String email, String password, String metier) {
        super(firstname, lastname, email, password);


        this.acceptedMissions = CreateAcceptedMissions(AllMissions.getInstance());

        //this.frameBenevole= new FrameBenevole(this.acceptedMissions,this);

        //this.acceptedMissions = new ArrayList<>();
        this.metier = metier;

    }



    // Méthode pour que le bénévole accepte une mission
    public void acceptMission(Mission mission) {
        if (mission != null ) {
            acceptedMissions.add(mission);
            System.out.println("Mission accepted: " + mission.getIntitule());
        } else {
            System.out.println("Mission is already accepted or is null.");
        }
    }
    public List<Mission> CreateAcceptedMissions(AllMissions allMissions) {
        // Créer une nouvelle instance d'AllMissions pour stocker les missions acceptées
        List<Mission> acceptedMissions = new ArrayList<>();

        // Parcourir toutes les missions
        for (Mission mission : allMissions.getMissions()) {
            // Vérifier si le bénévole de la mission est celui courant (this)
            if (this.equals(mission.getBenevole())) {
                // Ajouter la mission à la liste des missions acceptées
                acceptedMissions.add(mission);
            }
        }

        // Retourner l'objet AllMissions contenant uniquement les missions du bénévole courant
        return acceptedMissions;
    }

    public void UpdateAcceptedMissions(AllMissions allMissions) {
        // Créer une nouvelle instance d'AllMissions pour stocker les missions acceptées
        AllMissions miss= AllMissions.getInstance();

        // Parcourir toutes les missions
        for (Mission mission : allMissions.getMissions()) {
            // Vérifier si le bénévole de la mission est celui courant (this)
            if (this.equals(mission.getBenevole())) {
                // Ajouter la mission à la liste des missions acceptées
                this.acceptedMissions.add(mission);
            }
        }

        // Retourner l'objet AllMissions contenant uniquement les missions du bénévole courant

    }



    // Method to list all accepted missions

    // Méthode pour obtenir la liste des missions acceptées
    public List<Mission> getAcceptedMissions() {

        return acceptedMissions;
    }
    public void setAcceptedMissions(List<Mission> acceptedMissions) {
        this.acceptedMissions = acceptedMissions;
    }

    // Méthode pour obtenir le métier du bénévole
    public String getMetier() {
        return metier;
    }


    // Méthode pour définir le métier du bénévole, si besoin
    public void setMetier(String metier) {
        this.metier = metier;
    }

}
