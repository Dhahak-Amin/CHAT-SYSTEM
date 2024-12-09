package fr.insa.maven.demo.demoMavenProject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AllMissions {
    // Instance unique (Singleton)
    private static volatile AllMissions instance;

    private List<Mission> missions;
    private Connection conn;

    // Constructeur privé pour empêcher les instances externes
    private AllMissions() {
        this.missions = new ArrayList<>();
        try {
            // Utilisation de RemoteDatabaseManager pour gérer la connexion
            this.conn = RemoteDatabaseManager.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Obtenir l'instance unique (Singleton)
    public static AllMissions getInstance() {
        if (instance == null) {
            synchronized (AllMissions.class) {
                if (instance == null) {
                    instance = new AllMissions();
                }
            }
        }
        return instance;
    }

    public void enregistrerMission2(Mission mission) {
        try {
            // Vérifie si la mission existe déjà dans la base de données
            String checkSql = "SELECT COUNT(*) FROM Mission WHERE intitule = ? AND demandeur_email = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, mission.getIntitule());
                checkStmt.setString(2, mission.getDemandeur().getEmail());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    rs.next(); // Passe à la première ligne des résultats
                    int count = rs.getInt(1);

                    if (count == 0) {
                        // Si la mission n'existe pas, insère une nouvelle entrée
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
                        // Si la mission existe déjà, effectue une mise à jour
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

            // Recharge les missions depuis la base pour synchroniser avec la mémoire
            loadMissionsFromDatabase();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthodes CRUD et autres fonctionnalités
    public void addMission(Mission mission) {
        missions.add(mission);
    }

    public boolean removeMission(Mission mission) {
        boolean removed = missions.removeIf(m ->
                m.getIntitule().equals(mission.getIntitule()) &&
                        m.getDemandeur().getEmail().equals(mission.getDemandeur().getEmail())
        );

        if (removed) {
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

    public List<Mission> getMissions() {
        return missions;
    }

    public void loadMissionsFromDatabase() {
        try {
            String sql = "SELECT * FROM Mission";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                missions.clear(); // Réinitialiser la liste

                while (rs.next()) {
                    Demandeur demandeur = getDemandeurByEmail(rs.getString("demandeur_email"));
                    Benevole benevole = null;

                    if (rs.getString("benevole_email") != null) {
                        benevole = getBenevoleByEmail(rs.getString("benevole_email"));
                    }

                    Mission mission = new Mission(
                            MissionEtat.valueOf(rs.getString("etat")),
                            rs.getString("intitule"),
                            demandeur,
                            Place.valueOf(rs.getString("place")),
                            benevole
                    );
                    missions.add(mission);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Demandeur getDemandeurByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM Demandeur WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Demandeur(
                            rs.getString("firstname"),
                            rs.getString("lastname"),
                            rs.getString("description"),
                            rs.getString("needs"),
                            Place.valueOf(rs.getString("location")),
                            rs.getString("email"),
                            rs.getString("password")
                    );
                }
            }
        }
        throw new SQLException("Demandeur non trouvé !");
    }

    public Benevole getBenevoleByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM Benevole WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Benevole(
                            rs.getString("firstname"),
                            rs.getString("lastname"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("metier")
                    );
                }
            }
        }
        throw new SQLException("Bénévole non trouvé !");
    }

    // Méthode pour enregistrer une mission dans la base de données
    public void enregistrerMission(Mission mission) {
        try {
            String sql = "INSERT INTO Mission (intitule, place, etat, demandeur_email, benevole_email) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, mission.getIntitule());
            stmt.setString(2, mission.getPlace().name());
            stmt.setString(3, mission.getEtat().name());
            stmt.setString(4, mission.getDemandeur().getEmail());
            stmt.setString(5, mission.getBenevole() != null ? mission.getBenevole().getEmail() : null);
            stmt.executeUpdate();
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
    // Efface uniquement la liste des missions en mémoire
    public void clear() {
        missions.clear();
    }
    // Efface toutes les missions de la liste et de la base de données
    public void clearMissions() {
        missions.clear(); // Vide la liste en mémoire
        try {
            String sql = "DELETE FROM Mission"; // Supprime toutes les missions de la base
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
