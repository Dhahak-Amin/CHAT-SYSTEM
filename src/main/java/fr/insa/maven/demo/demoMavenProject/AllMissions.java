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

    public Benevole getBenevoleByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM Benevole WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Benevole benevole = new Benevole(
                            rs.getString("firstname"),
                            rs.getString("lastname"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("metier")
                    );

                    // Charger les avis associés
                    benevole.setListeAvis(loadAvisForBenevole(email));
                    return benevole;
                }
            }
        }
        throw new SQLException("Bénévole non trouvé !");
    }

    // Méthode pour obtenir la liste des missions
    public List<Mission> getMissions() {
        return missions;
    }
    public void loadMissionsFromDatabase() {
        try {
            String sql = "SELECT * FROM Mission";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            missions.clear(); // Réinitialise la liste des missions

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

                // Log des missions chargées
               // System.out.println("Mission chargée : " + mission.getIntitule() + ", Demandeur : " + (demandeur != null ? demandeur.getEmail() : "null"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
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


    // Méthode pour trouver une mission par son intitulé
    public Mission findMission(String intitule) {
        for (Mission mission : missions) {
            if (mission.getIntitule().equals(intitule)) {
                return mission;
            }
        }
        return null;
    }

    public void enregistrerAvis(Avis avis) {
        try {
            String sql = "INSERT INTO Avis (comment, note, email_benevole) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, avis.getComment());
            stmt.setInt(2, avis.getNote());
            stmt.setString(3, avis.getBenevole().getEmail());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Avis> loadAvisForBenevole(String emailBenevole) {
        List<Avis> avisList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Avis WHERE email_benevole = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, emailBenevole);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Avis avis = new Avis(getBenevoleByEmail(emailBenevole));
                avis.setComment(rs.getString("comment"));
                avis.setNote(rs.getInt("note"));
                avisList.add(avis);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return avisList;
    }



    // Méthode pour compter le nombre de missions



    public int countMissions() {
        return missions.size();
    }

    // Nouvelle méthode : clearMissions
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
}





