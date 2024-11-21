package fr.insa.maven.demo.demoMavenProject;

import java.util.ArrayList;
import java.util.List;

public class AllMissions {
    private List<Mission> missions;

    // Constructor
    public AllMissions() {
        this.missions = new ArrayList<>();
    }

    // Method to add a mission
    public void addMission(Mission mission) {
        missions.add(mission);
    }

    // Method to remove a mission by its intitule
    public boolean removeMission(String intitule) {
        return missions.removeIf(mission -> mission.getIntitule().equals(intitule));
    }

    // Method to update a mission's details
    public boolean updateMission(String intitule, String newEtat, String newIntitule, Demandeur demandeur) {
        for (Mission mission : missions) {
            if (mission.getIntitule().equals(intitule)) {
                mission.setEtat(newEtat);
                mission.setIntitule(newIntitule);
                mission.setDemandeur(demandeur);
                return true;
            }
        }
        return false;
    }

    // Method to retrieve the list of missions
    public List<Mission> getMissions() {
        return new ArrayList<>(missions); // Return a copy to prevent external modification
    }

    // Method to find a mission by its intitule
    public Mission findMission(String intitule) {
        for (Mission mission : missions) {
            if (mission.getIntitule().equals(intitule)) {
                return mission;
            }
        }
        return null; // Return null if no mission is found
    }

    // Method to count the total number of missions
    public int countMissions() {
        return missions.size();
    }

    // Method to clear all missions
    public void clearMissions() {
        missions.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("AllMissions:\n");
        for (Mission mission : missions) {
            sb.append("Mission: ").append(mission.getIntitule())
              .append(", Etat: ").append(mission.getEtat())
              .append(", Demandeur: ").append(mission.getDemandeur()).append("\n");
        }
        return sb.toString();
    }
}
