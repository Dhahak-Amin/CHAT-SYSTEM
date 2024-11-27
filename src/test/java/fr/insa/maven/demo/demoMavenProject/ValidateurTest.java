package fr.insa.maven.demo.demoMavenProject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class ValidateurTest {

    private Connection conn;

    // Informations de connexion à la base de données
    static final String DB_URL = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/projet_gei_012";
    static final String USER = "projet_gei_012";
    static final String PASS = "dith1Que";

    @BeforeEach
    public void setUp() throws SQLException {
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        conn.setAutoCommit(false); // Commence une transaction pour isoler les tests

        // Nettoyer la table Validateur avant chaque test
        try (PreparedStatement clearStmt = conn.prepareStatement("TRUNCATE TABLE Validateur")) {
            clearStmt.executeUpdate();
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (conn != null) {
            conn.rollback(); // Annule les modifications pour ce test
            conn.close();
        }
    }

    @Test
    public void testAjoutValidateurDansBaseDeDonnees() throws SQLException {
        // Création d'un validateur
        Validateur validateur = new Validateur("Test", "User", "bruh@example.com", "password123", conn);

        // Ajout dans la base de données
        try (PreparedStatement insertStmt = conn.prepareStatement(
                "INSERT INTO Validateur (email, firstname, lastname, password) VALUES (?, ?, ?, ?)")) {
            insertStmt.setString(1, validateur.getEmail());
            insertStmt.setString(2, validateur.getFirstname());
            insertStmt.setString(3, validateur.getLastname());
            insertStmt.setString(4, validateur.getPassword());
            int rowsInserted = insertStmt.executeUpdate();
            assertEquals(1, rowsInserted, "Une seule ligne doit être insérée");
        }

        // Vérification que le validateur a été ajouté
        try (PreparedStatement selectStmt = conn.prepareStatement(
                "SELECT firstname, lastname FROM Validateur WHERE email = ?")) {
            selectStmt.setString(1, validateur.getEmail());
            try (ResultSet rs = selectStmt.executeQuery()) {
                assertTrue(rs.next(), "Le validateur doit être présent dans la base");
                assertEquals(validateur.getFirstname(), rs.getString("firstname"));
                assertEquals(validateur.getLastname(), rs.getString("lastname"));
            }
        }
    }

    @Test
    public void testAjoutValidateurAvecEmailDuplique() throws SQLException {
        // Création d'un validateur
        Validateur validateur = new Validateur("Test", "User", "duplicate@example.com", "password123", conn);

        // Ajouter un validateur pour la première fois
        try (PreparedStatement insertStmt = conn.prepareStatement(
                "INSERT INTO Validateur (email, firstname, lastname, password) VALUES (?, ?, ?, ?)")) {
            insertStmt.setString(1, validateur.getEmail());
            insertStmt.setString(2, validateur.getFirstname());
            insertStmt.setString(3, validateur.getLastname());
            insertStmt.setString(4, validateur.getPassword());
            int rowsInserted = insertStmt.executeUpdate();
            assertEquals(1, rowsInserted, "Une seule ligne doit être insérée");
        }

        // Tentative d'ajout d'un validateur avec le même email
        try (PreparedStatement insertStmt = conn.prepareStatement(
                "INSERT INTO Validateur (email, firstname, lastname, password) VALUES (?, ?, ?, ?)")) {
            insertStmt.setString(1, validateur.getEmail());
            insertStmt.setString(2, "Duplicate");
            insertStmt.setString(3, "User");
            insertStmt.setString(4, "password456");
            assertThrows(SQLException.class, insertStmt::executeUpdate,
                    "Une exception SQL doit être levée en cas de duplication d'email");
        }
    }

    @Test
    public void testSuppressionValidateur() throws SQLException {
        // Création d'un validateur
        Validateur validateur = new Validateur("Test", "User", "delete@example.com", "password123", conn);

        // Ajouter un validateur
        try (PreparedStatement insertStmt = conn.prepareStatement(
                "INSERT INTO Validateur (email, firstname, lastname, password) VALUES (?, ?, ?, ?)")) {
            insertStmt.setString(1, validateur.getEmail());
            insertStmt.setString(2, validateur.getFirstname());
            insertStmt.setString(3, validateur.getLastname());
            insertStmt.setString(4, validateur.getPassword());
            insertStmt.executeUpdate();
        }

        // Supprimer le validateur
        try (PreparedStatement deleteStmt = conn.prepareStatement(
                "DELETE FROM Validateur WHERE email = ?")) {
            deleteStmt.setString(1, validateur.getEmail());
            int rowsDeleted = deleteStmt.executeUpdate();
            assertEquals(1, rowsDeleted, "Une seule ligne doit être supprimée");
        }

        // Vérification que le validateur a été supprimé
        try (PreparedStatement selectStmt = conn.prepareStatement(
                "SELECT firstname, lastname FROM Validateur WHERE email = ?")) {
            selectStmt.setString(1, validateur.getEmail());
            try (ResultSet rs = selectStmt.executeQuery()) {
                assertFalse(rs.next(), "Le validateur ne doit plus être présent dans la base");
            }
        }
    }
}
