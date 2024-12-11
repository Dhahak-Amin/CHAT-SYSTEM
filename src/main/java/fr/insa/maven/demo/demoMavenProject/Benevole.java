package fr.insa.maven.demo.demoMavenProject;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

/**
 * Classe représentant un bénévole.
 * Un bénévole est un utilisateur qui peut accepter des missions et recevoir des avis.
 */
public class Benevole extends User {

    private List<Avis> listeAvis;           // Liste des avis reçus par le bénévole
    private float moyenne;                  // Moyenne des notes des avis
    private List<Mission> acceptedMissions; // Liste des missions acceptées par le bénévole
    private String metier;                  // Métier ou spécialité du bénévole

    /**
     * Constructeur principal pour un bénévole avec métier spécifié.
     *
     * @param firstname Prénom du bénévole.
     * @param lastname  Nom de famille du bénévole.
     * @param email     Email du bénévole.
     * @param password  Mot de passe du bénévole.
     * @param metier    Métier ou spécialité du bénévole.
     */
    public Benevole(String firstname, String lastname, String email, String password, String metier) {
        super(firstname, lastname, email, password);
        this.listeAvis = new ArrayList<>();
        this.acceptedMissions = new ArrayList<>();
        this.metier = metier;
    }

    /**
     * Constructeur secondaire pour un bénévole sans métier spécifié.
     *
     * @param firstname Prénom du bénévole.
     * @param lastname  Nom de famille du bénévole.
     * @param email     Email du bénévole.
     * @param password  Mot de passe du bénévole.
     */
    public Benevole(String firstname, String lastname, String email, String password) {
        super(firstname, lastname, email, password);
        this.listeAvis = new ArrayList<>();
        this.acceptedMissions = new ArrayList<>();
    }

    // Getters et Setters

    public List<Avis> getListeAvis() {
        return listeAvis;
    }

    public void setListeAvis(List<Avis> listeAvis) {
        this.listeAvis = listeAvis;
        calculateMoyenne(); // Recalculer la moyenne à chaque mise à jour
    }

    public float getMoyenne() {
        return moyenne;
    }

    public List<Mission> getAcceptedMissions() {
        return acceptedMissions;
    }

    public void setAcceptedMissions(List<Mission> acceptedMissions) {
        this.acceptedMissions = acceptedMissions;
    }

    public String getMetier() {
        return metier;
    }

    public void setMetier(String metier) {
        this.metier = metier;
    }

    // Méthodes principales

    /**
     * Permet au bénévole d'accepter une mission.
     *
     * @param mission Mission à accepter.
     */
    public void acceptMission(Mission mission) {
        if (mission != null && mission.getBenevole() == null) {
            mission.setBenevole(this); // Associer le bénévole à la mission
            acceptedMissions.add(mission); // Ajouter la mission à la liste des missions acceptées

            try {
                String sql = "UPDATE Mission SET benevole_email = ? WHERE intitule = ?";
                PreparedStatement stmt = ConnectionManager.getInstance().getConnection().prepareStatement(sql);
                stmt.setString(1, this.getEmail());
                stmt.setString(2, mission.getIntitule());
                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Mission acceptée et mise à jour : " + mission.getIntitule());
                } else {
                    System.out.println("Échec de la mise à jour pour la mission : " + mission.getIntitule());
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Erreur lors de l'enregistrement de la mission.");
            }
        } else {
            System.out.println("Mission déjà assignée ou invalide.");
        }
    }

    /**
     * Convertit la moyenne des avis en une chaîne lisible.
     *
     * @return Moyenne formatée ou un message par défaut si aucun avis.
     */
    public String MoyennetoString() {
        if (moyenne == 0.0f) {
            return "Pas encore de note.";
        } else {
            return String.format("%.2f/5", moyenne); // Formater avec 2 décimales
        }
    }

    /**
     * Ajoute un avis pour le bénévole.
     *
     * @param avis Avis à ajouter.
     */
    public void addAvis(Avis avis) {
        if (avis != null) {
            this.listeAvis.add(avis);
            saveAvisToDatabase(avis); // Sauvegarder l'avis dans la base de données
            calculateMoyenne(); // Recalculer la moyenne
            System.out.println("Avis ajouté pour : " + this.getFirstname() + " " + this.getLastname());
        }
    }

    /**
     * Charge les avis d'un bénévole à partir de la base de données.
     *
     * @param benevole Le bénévole dont les avis doivent être chargés.
     * @return Liste des avis chargés.
     */
    public static List<Avis> loadAvisForBenevole(Benevole benevole) {
        List<Avis> avisList = new ArrayList<>();
        try {
            Connection conn = ConnectionManager.getInstance().getConnection();
            String sql = "SELECT * FROM Avis WHERE email_benevole = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, benevole.getEmail());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Avis avis = new Avis(benevole);
                avis.setComment(rs.getString("comment"));
                avis.setNote(rs.getInt("note"));
                avisList.add(avis);
            }

            benevole.setListeAvis(avisList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return avisList;
    }

    /**
     * Enregistre un avis dans la base de données.
     *
     * @param avis Avis à enregistrer.
     */
    private void saveAvisToDatabase(Avis avis) {
        try {
            Connection conn = ConnectionManager.getInstance().getConnection();
            String sql = "INSERT INTO Avis (email_benevole, comment, note) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, this.getEmail());
            stmt.setString(2, avis.getComment());
            stmt.setInt(3, avis.getNote());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'ajout de l'avis.");
        }
    }

    /**
     * Recalcule la moyenne des notes à partir des avis.
     */
    private void calculateMoyenne() {
        if (listeAvis.isEmpty()) {
            this.moyenne = 0;
            return;
        }
        int total = listeAvis.stream().mapToInt(Avis::getNote).sum();
        this.moyenne = (float) total / listeAvis.size();
    }
}
