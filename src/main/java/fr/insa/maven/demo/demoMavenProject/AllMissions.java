package fr.insa.maven.demo.demoMavenProject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AllMissions {
    // L'instance unique, volatile pour assurer la visibilité correcte entre threads
    private static volatile AllMissions instance;

    private List<Mission> missions;
    private Connection conn;

    // Informations de connexion à la base de données
    static final String DB_URL = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/projet_gei_012";
    static final String USER = "projet_gei_012";
    static final String PASS = "dith1Que";

    // Constructeur privé pour empêcher les instances externes
    private AllMissions() {
        this.missions = new ArrayList<>();
        try {
            this.conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour obtenir l'instance unique de AllMissions
    public static AllMissions getInstance() {
        // Utilisation de "double-checked locking" pour rendre la création de l'instance thread-safe
        if (instance == null) {
            synchronized (AllMissions.class) {
                if (instance == null) {
                    instance = new AllMissions();
                }
            }
        }
        return instance;
    }

    // Méthode pour ajouter une mission
    public void addMission(Mission mission) {
        missions.add(mission);
    }

    // Méthode pour supprimer une mission par intitulé
    public boolean removeMission(Mission mission) {
        // Supprimer la mission de la liste en mémoire
        boolean removed = missions.removeIf(m ->
                m.getIntitule().equals(mission.getIntitule()) &&
                        m.getDemandeur().getEmail().equals(mission.getDemandeur().getEmail())
        );

        if (removed) {
            // Si la mission est supprimée en mémoire, la supprimer de la base de données
            try {
                String sql = "DELETE FROM Mission WHERE intitule = ? AND demandeur_email = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, mission.getIntitule());
                    stmt.setString(2, mission.getDemandeur().getEmail());
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

    // Méthode pour obtenir la liste des missions
    public List<Mission> getMissions() {
        return missions;
    }

    // Méthode pour enregistrer une mission dans la base de données
    public void enregistrerMission(Mission mission) {
        try {
            String sql = "INSERT INTO Mission (intitule, place, etat, demandeur_email, benevole_email) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, mission.getIntitule());
            stmt.setString(2, mission.getPlace().name());
            stmt.setString(3, mission.getEtat().name());
            stmt.setString(4, mission.getDemandeur().getEmail()); // Assurez-vous que l'email du demandeur est utilisé
            stmt.setString(5, mission.getBenevole() != null ? mission.getBenevole().getEmail() : null); // Insérez l'email du bénévole
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void enregistrerMission2(Mission mission) {
        try {
            // Vérifier si la mission existe déjà
            String checkSql = "SELECT COUNT(*) FROM Mission WHERE intitule = ? AND demandeur_email = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, mission.getIntitule());
                checkStmt.setString(2, mission.getDemandeur().getEmail());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    rs.next();
                    int count = rs.getInt(1);

                    if (count == 0) {
                        // La mission n'existe pas, insérer une nouvelle mission
                        String insertSql = "INSERT INTO Mission (intitule, place, etat, demandeur_email, benevole_email) VALUES (?, ?, ?, ?, ?)";
                        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                            insertStmt.setString(1, mission.getIntitule());
                            insertStmt.setString(2, mission.getPlace().name());
                            insertStmt.setString(3, mission.getEtat().name());
                            insertStmt.setString(4, mission.getDemandeur().getEmail());
                            insertStmt.setString(5, mission.getBenevole() != null ? mission.getBenevole().getEmail() : null);
                            insertStmt.executeUpdate();
                        }
                    } else {
                        // La mission existe, la mettre à jour
                        String updateSql = "UPDATE Mission SET place = ?, etat = ?, benevole_email = ? WHERE intitule = ? AND demandeur_email = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                            updateStmt.setString(1, mission.getPlace().name());
                            updateStmt.setString(2, mission.getEtat().name());
                            updateStmt.setString(3, mission.getBenevole() != null ? mission.getBenevole().getEmail() : null);
                            updateStmt.setString(4, mission.getIntitule());
                            updateStmt.setString(5, mission.getDemandeur().getEmail());
                            updateStmt.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Méthode pour trouver une mission par son intitulé
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
}
