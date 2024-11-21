package fr.insa.maven.demo.demoMavenProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AllMissions {
    private List<Mission> missions;
    private Connection conn;

    // Informations de connexion à la base de données
    static final String DB_URL = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/projet_gei_012";
    static final String USER = "projet_gei_012";
    static final String PASS = "dith1Que";

    // Constructeur pour initialiser la connexion à la base de données
    public AllMissions() {
        this.missions = new ArrayList<>();
        try {
            this.conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour ajouter une mission
    public void addMission(Mission mission) {
        missions.add(mission);
<<<<<<< Updated upstream
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
=======
        try {
            // Remplacer les IDs par les emails pour correspondre à la logique actuelle
            String sql = "INSERT INTO Mission (intitule, place, etat, demandeur_email) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, mission.getIntitule());
                stmt.setString(2, mission.getPlace().toString());
                stmt.setString(3, mission.getEtat().toString());
                stmt.setString(4, mission.getUser().getEmail()); // Utilisez l'email de l'utilisateur
                stmt.executeUpdate();
>>>>>>> Stashed changes
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour compter le nombre de missions
    public int countMissions() {
        return missions.size();
    }

    // Méthode pour trouver une mission par intitulé
    public Mission findMission(String intitule) {
        for (Mission mission : missions) {
            if (mission.getIntitule().equals(intitule)) {
                return mission;
            }
        }
        return null;
    }

    // Méthode pour supprimer une mission
    public boolean removeMission(String intitule) {
        boolean removed = missions.removeIf(mission -> mission.getIntitule().equals(intitule));
        if (removed) {
            try {
                String sql = "DELETE FROM Mission WHERE intitule = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, intitule);
                    stmt.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return removed;
    }

    // Méthode pour mettre à jour une mission
    public boolean updateMission(String intitule, MissionEtat nouvelEtat, String nouvelIntitule, Benevole nouveauBenevole) {
        for (Mission mission : missions) {
<<<<<<< Updated upstream
            sb.append("Mission: ").append(mission.getIntitule())
              .append(", Etat: ").append(mission.getEtat())
              .append(", Demandeur: ").append(mission.getDemandeur()).append("\n");
=======
            if (mission.getIntitule().equals(intitule)) {
                mission.setEtat(nouvelEtat);
                mission.setIntitule(nouvelIntitule);
                mission.setBenevole(nouveauBenevole);
                try {
                    String sql = "UPDATE Mission SET etat = ?, intitule = ?, benevole_email = ? WHERE intitule = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setString(1, nouvelEtat.toString());
                        stmt.setString(2, nouvelIntitule);
                        stmt.setString(3, nouveauBenevole.getEmail()); // Utilisez l'email du bénévole
                        stmt.setString(4, intitule);
                        stmt.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return true;
            }
>>>>>>> Stashed changes
        }
        return false;
    }
}
