
package fr.insa.maven.demo.demoMavenProject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class UserService {

    private Connection conn;

    // Constructeur pour initialiser la connexion
    public UserService(Connection conn) {
        this.conn = conn;
    }

    // Méthode pour enregistrer un nouvel utilisateur
    public void registerUser(User user) throws SQLException {
        String sql = "INSERT INTO User (firstname, lastname, email, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getFirstname());
            stmt.setString(2, user.getLastname());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword()); // Assurez-vous de hacher les mots de passe en production
            stmt.executeUpdate();
        }
    }

    // Méthode pour authentifier un utilisateur
    public User authenticateUser(String email, String password) throws SQLException {
        String sql = "SELECT firstname, lastname, email FROM User WHERE email = ? AND password = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            // Si l'utilisateur est trouvé, retourner un objet User
            if (rs.next()) {
                String firstname = rs.getString("firstname");
                String lastname = rs.getString("lastname");
                return new User(firstname, lastname, email, password);
            } else {
                return null; // Si l'utilisateur n'existe pas ou que le mot de passe est incorrect
            }
        }
    }

    // Méthode pour mettre à jour un utilisateur
    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE User SET firstname = ?, lastname = ?, email = ?, password = ? WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getFirstname());
            stmt.setString(2, user.getLastname());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword()); // Assurez-vous de hacher les mots de passe en production
            stmt.setString(5, user.getEmail());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
               // System.out.println("L'utilisateur a été mis à jour avec succès.");
            } else {
               // System.out.println("Aucune mise à jour n'a été effectuée.");
            }
        }
    }

    // Méthode pour supprimer un utilisateur
    public void deleteUser(String email) throws SQLException {
        String sql = "DELETE FROM User WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
               // System.out.println("L'utilisateur a été supprimé.");
            } else {
                //System.out.println("Aucun utilisateur trouvé avec cet email.");
            }
        }
    }
}
