package fr.insa.maven.demo.demoMavenProject;

import java.util.ArrayList;
import java.util.List;

public class Benevole extends User {

    private FrameBenevole frameBenevole ;



    private AllMissions acceptedMissions;

    //private List<Mission> acceptedMissions;
    private String metier; // Métier du bénévole


    public Benevole(String firstname, String lastname, String email, String password, String metier) {
        super(firstname, lastname, email, password);

        this.acceptedMissions = new AllMissions();
        //this.frameBenevole= new FrameBenevole(this.acceptedMissions,this);

        //this.acceptedMissions = new ArrayList<>();
        this.metier = metier;

    }

    // Méthode pour que le bénévole accepte une mission
    public void acceptMission(Mission mission) {
        if (mission != null ) {
            acceptedMissions.addMission(mission);
            System.out.println("Mission accepted: " + mission.getIntitule());
        } else {
            System.out.println("Mission is already accepted or is null.");
        }
    }


    // Method to list all accepted missions

    // Méthode pour obtenir la liste des missions acceptées
    public AllMissions getAcceptedMissions() {

        return acceptedMissions;
    }
    public void setAcceptedMissions(AllMissions acceptedMissions) {
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
