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
    }

    // Méthode pour supprimer une mission par intitulé
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
    public boolean updateMission(String intitule, String newEtat, String newIntitule, Demandeur demandeur) {
        for (Mission mission : missions) {
            if (mission.getIntitule().equals(intitule)) {
                mission.setEtat(MissionEtat.valueOf(newEtat));
                mission.setIntitule(newIntitule);
                mission.setDemandeur(demandeur);
                try {
                    String sql = "UPDATE Mission SET etat = ?, intitule = ?, demandeur_email = ? WHERE intitule = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setString(1, newEtat);
                        stmt.setString(2, newIntitule);
                        stmt.setString(3, demandeur.getEmail());
                        stmt.setString(4, intitule);
                        stmt.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
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

    // Méthode pour compter le nombre de missions
    public int countMissions() {
        return missions.size();
    }
    public List<Mission> getMissions() {
        return missions;
    }
}
