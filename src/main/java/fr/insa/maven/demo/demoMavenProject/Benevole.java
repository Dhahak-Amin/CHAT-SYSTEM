package fr.insa.maven.demo.demoMavenProject;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;
public class Benevole extends User {

    private List<Avis> listeAvis; // Liste des avis pour le bénévole
    private float moyenne;        // Moyenne des notes des avis
    private List<Mission> acceptedMissions; // Missions acceptées
    private String metier;        // Métier du bénévole

    // Constructeur
    public Benevole(String firstname, String lastname, String email, String password, String metier) {
        super(firstname, lastname, email, password);
        this.listeAvis = new ArrayList<>();
        this.acceptedMissions = new ArrayList<>();
        this.metier = metier;
    }

    // Getter pour la liste des avis
    public List<Avis> getListeAvis() {
        return listeAvis;
    }

    public void acceptMission(Mission mission) {
        if (mission != null && mission.getBenevole() == null) {
            // Associer le bénévole à la mission
            mission.setBenevole(this);

            // Initialiser la liste des missions acceptées si nécessaire
            if (acceptedMissions == null) {
                acceptedMissions = new ArrayList<>();
            }

            // Ajouter la mission à la liste des missions acceptées
            acceptedMissions.add(mission);

            // Enregistrer la mise à jour dans la base de données
            try {
                String sql = "UPDATE Mission SET benevole_email = ? WHERE intitule = ?";
                PreparedStatement stmt = ConnectionManager.getInstance().getConnection().prepareStatement(sql);
                stmt.setString(1, this.getEmail());
                stmt.setString(2, mission.getIntitule());
                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Mission acceptée et mise à jour dans la base de données : " + mission.getIntitule());
                } else {
                    System.out.println("Échec de la mise à jour dans la base de données pour la mission : " + mission.getIntitule());
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Erreur lors de l'enregistrement de la mission dans la base de données.");
            }
        } else {
            System.out.println("La mission est déjà assignée ou invalide.");
        }
    }
    public String MoyennetoString() {
        if (moyenne == -1.0f || moyenne == 0.0f) {
            return "Pas encore de note.";
        } else {
            return String.format("%.2f/5", moyenne); // Formate la moyenne avec 2 décimales
        }
    }


    // Méthode pour ajouter un avis
    public void addAvis(Avis avis) {
        if (avis != null) {
            this.listeAvis.add(avis); // Ajoute l'avis à la liste
            saveAvisToDatabase(avis); // Sauvegarde l'avis dans la base de données
            calculateMoyenne(); // Recalcule la moyenne
            System.out.println("Avis ajouté pour le bénévole : " + this.getFirstname() + " " + this.getLastname());
        }
    }


    // Setter pour la liste des avis
    public void setListeAvis(List<Avis> listeAvis) {
        this.listeAvis = listeAvis;
        // Recalculer la moyenne à chaque fois que la liste est mise à jour
        calculateMoyenne();
    }

    // Méthode pour charger les avis d'un bénévole à partir de la base de données
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

            benevole.setListeAvis(avisList); // Met à jour la liste des avis du bénévole
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return avisList;
    }


    // Méthode pour ajouter un avis à la base de données
    private void saveAvisToDatabase(Avis avis) {
        try {
            Connection conn = ConnectionManager.getInstance().getConnection();
            String sql = "INSERT INTO Avis (email_benevole, comment, note) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, this.getEmail()); // Email du bénévole
            stmt.setString(2, avis.getComment()); // Commentaire
            stmt.setInt(3, avis.getNote()); // Note

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
               // System.out.println("Avis ajouté à la base de données avec succès.");
            } else {
               /// System.out.println("Échec de l'ajout de l'avis à la base de données.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'ajout de l'avis à la base de données.");
        }
    }



    // Getter pour la moyenne
    public float getMoyenne() {
        return moyenne;
    }

    // Calculer la moyenne des notes des avis
    private void calculateMoyenne() {
        if (listeAvis.isEmpty()) {
            this.moyenne = 0;
            return;
        }
        int total = listeAvis.stream().mapToInt(Avis::getNote).sum();
        this.moyenne = (float) total / listeAvis.size();
    }

    // Getter pour les missions acceptées
    public List<Mission> getAcceptedMissions() {
        return acceptedMissions;
    }

    // Setter pour les missions acceptées
    public void setAcceptedMissions(List<Mission> acceptedMissions) {
        this.acceptedMissions = acceptedMissions;
    }

    // Getter pour le métier
    public String getMetier() {
        return metier;
    }

    // Setter pour le métier
    public void setMetier(String metier) {
        this.metier = metier;
    }
}
