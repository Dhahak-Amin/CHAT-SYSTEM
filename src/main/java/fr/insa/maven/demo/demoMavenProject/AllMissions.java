package fr.insa.maven.demo.demoMavenProject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Singleton pour gérer toutes les missions. Cette classe permet d'interagir
 * avec la base de données et de maintenir une liste synchronisée des missions en mémoire.
 */
public class AllMissions {

    // Instance unique (Singleton)
    private static volatile AllMissions instance;

    // Liste des missions en mémoire
    private List<Mission> missions;
    private Connection conn;

    /**
     * Constructeur privé pour empêcher les instanciations externes.
     */
    private AllMissions() {
        this.missions = new ArrayList<>();
        try {
            this.conn = RemoteDatabaseManager.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retourne l'instance unique de la classe.
     * @return L'instance unique de AllMissions.
     */
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

    /**
     * Enregistre ou met à jour une mission dans la base de données.
     * Recharge les missions après chaque modification.
     * @param mission La mission à enregistrer ou mettre à jour.
     */
    public void enregistrerMission2(Mission mission) {
        try {
            String checkSql = "SELECT COUNT(*) FROM Mission WHERE intitule = ? AND demandeur_email = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, mission.getIntitule());
                checkStmt.setString(2, mission.getDemandeur().getEmail());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    rs.next();
                    int count = rs.getInt(1);

                    if (count == 0) {
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
            loadMissionsFromDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ajoute une mission à la liste en mémoire.
     * @param mission La mission à ajouter.
     */
    public void addMission(Mission mission) {
        missions.add(mission);
    }

    /**
     * Supprime une mission de la liste en mémoire et de la base de données.
     * @param mission La mission à supprimer.
     * @return true si la mission a été supprimée, false sinon.
     */
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

    /**
     * Retourne la liste des missions en mémoire.
     * @return La liste des missions.
     */
    public List<Mission> getMissions() {
        return missions;
    }

    /**
     * Recharge toutes les missions depuis la base de données dans la liste en mémoire.
     */
    public void loadMissionsFromDatabase() {
        try {
            String sql = "SELECT * FROM Mission";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                missions.clear();
                while (rs.next()) {
                    Demandeur demandeur = getDemandeurByEmail(rs.getString("demandeur_email"));
                    Benevole benevole = rs.getString("benevole_email") != null ? getBenevoleByEmail(rs.getString("benevole_email")) : null;
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

    /**
     * Retourne un demandeur à partir de son email.
     * @param email Email du demandeur.
     * @return L'objet Demandeur correspondant.
     * @throws SQLException Si aucun demandeur n'est trouvé.
     */
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

    /**
     * Retourne un bénévole à partir de son email.
     * @param email Email du bénévole.
     * @return L'objet Benevole correspondant.
     * @throws SQLException Si aucun bénévole n'est trouvé.
     */
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

    /**
     * Efface toutes les missions de la base de données et de la liste en mémoire.
     */
    public void clearMissions() {
        missions.clear();
        try {
            String sql = "DELETE FROM Mission";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.executeUpdate();
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
    // Efface uniquement la liste des missions en mémoire
    public void clear() {
        missions.clear();
    }

}
