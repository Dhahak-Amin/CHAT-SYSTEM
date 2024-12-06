package fr.insa.maven.demo.demoMavenProject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    // Informations de connexion à la base de données
    static final String DB_URL = "jdbc:mysql://localhost:3306/test_db";
    static final String USER = "root";
    static final String PASS = "root";

    @BeforeEach
    public void setUp() throws SQLException {
        // Vérifie la base et initialise les tables via CLI
        DatabaseManager.ensureDatabaseExists();
        DatabaseManager.executeSqlFileWithCli(DatabaseManager.SQL_FILE_PATH);

        // Nettoyer les tables nécessaires
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement clearDemandeurTable = conn.prepareStatement("DELETE FROM Demandeur")) {
            clearDemandeurTable.executeUpdate();
        }
    }

    @Test
    public void testRegisterAndRemoveDemandeur() throws SQLException {
        // Initialiser un Demandeur
        Demandeur demandeur = new Demandeur(
                "Alice",
                "Smith",
                "Besoin de soutien scolaire",
                "Soutien scolaire",
                Place.HOME,
                "alice.smith@example.com",
                "password123"
        );

        // Ajouter le demandeur dans la base via une méthode centralisée
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sqlInsert = "INSERT INTO Demandeur (firstname, lastname, description, needs, location, email, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlInsert)) {
                stmt.setString(1, demandeur.getFirstname());
                stmt.setString(2, demandeur.getLastname());
                stmt.setString(3, demandeur.getDescription());
                stmt.setString(4, demandeur.getNeeds());
                stmt.setString(5, demandeur.getLocation().name());
                stmt.setString(6, demandeur.getEmail());
                stmt.setString(7, demandeur.getPassword());
                stmt.executeUpdate();
            }
        }

        // Vérifier que le demandeur a été ajouté
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sqlSelect = "SELECT * FROM Demandeur WHERE email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlSelect)) {
                stmt.setString(1, demandeur.getEmail());
                try (ResultSet rs = stmt.executeQuery()) {
                    assertTrue(rs.next(), "Le demandeur n'a pas été inséré dans la base de données !");
                    assertEquals("Alice", rs.getString("firstname"));
                    assertEquals("Smith", rs.getString("lastname"));
                    assertEquals("Besoin de soutien scolaire", rs.getString("description"));
                    assertEquals("Soutien scolaire", rs.getString("needs"));
                    assertEquals(Place.HOME.name(), rs.getString("location"));
                }
            }
        }

        // Supprimer le demandeur en utilisant l'email
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sqlDelete = "DELETE FROM Demandeur WHERE email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlDelete)) {
                stmt.setString(1, demandeur.getEmail());
                int rowsDeleted = stmt.executeUpdate();
                assertEquals(1, rowsDeleted, "Le demandeur n'a pas été supprimé !");
            }
        }

        // Vérifier que le demandeur a été supprimé
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sqlSelect = "SELECT * FROM Demandeur WHERE email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlSelect)) {
                stmt.setString(1, demandeur.getEmail());
                try (ResultSet rs = stmt.executeQuery()) {
                    assertFalse(rs.next(), "Le demandeur est toujours présent dans la base de données !");
                }
            }
        }
    }
}
