package fr.insa.maven.demo.demoMavenProject;
import java.util.ArrayList;
import java.util.List;

public class Benevole extends User {
    private FrameBenevole frameBenevole ;



    private AllMissions acceptedMissions;

    public Benevole(String firstname, String lastname, String email, String password) {
        super(firstname, lastname, email, password);
        this.acceptedMissions = new AllMissions();
        this.frameBenevole= new FrameBenevole(this.acceptedMissions,this);
    }

    // Method for the volunteer to accept a mission
    public void acceptMission(Mission mission) {
        if (mission != null ) {
            acceptedMissions.addMission(mission);
            System.out.println("Mission accepted: " + mission.getIntitule());
        } else {
            System.out.println("Mission is already accepted or is null.");
        }
    }

    // Method to list all accepted missions
    public AllMissions getAcceptedMissions() {
        return acceptedMissions;
    }
    public void setAcceptedMissions(AllMissions acceptedMissions) {
        this.acceptedMissions = acceptedMissions;
    }



}
