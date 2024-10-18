package fr.insa.maven.demo.demoMavenProject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceAuthTest {

    private Connection conn;
    private UserService userService;

    // Informations de connexion à la base de données
    static final String DB_URL = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/projet_gei_012";
    static final String USER = "projet_gei_012";
    static final String PASS = "dith1Que";

    @BeforeEach
    public void setUp() throws SQLException {
        // Établir la connexion à la base de données
        conn = DriverManager.getConnection(DB_URL, USER, PASS);

        // Nettoyer la table User avant chaque test
        PreparedStatement clearUserTable = conn.prepareStatement("DELETE FROM User");
        clearUserTable.executeUpdate();

        // Initialiser le service utilisateur
        userService = new UserService(conn);
    }

    // Test pour vérifier l'authentification d'un utilisateur avec des identifiants corrects
    @Test
    public void testAuthenticateUserSuccess() throws SQLException {
        // Insérer un utilisateur test dans la base de données
        PreparedStatement insertUserStmt = conn.prepareStatement(
                "INSERT INTO User (firstname, lastname, email, password) VALUES (?, ?, ?, ?)"
        );
        insertUserStmt.setString(1, "John");
        insertUserStmt.setString(2, "Doe");
        insertUserStmt.setString(3, "john.doe@example.com");
        insertUserStmt.setString(4, "password123");  //
        insertUserStmt.executeUpdate();

        // Authentifier l'utilisateur
        User user = userService.authenticateUser("john.doe@example.com", "password123");

        // Vérifier que l'authentification a réussi
        assertNotNull(user);
        assertEquals("John", user.getFirstname());
        assertEquals("Doe", user.getLastname());
    }

    // Test pour vérifier l'échec de l'authentification avec des identifiants incorrects
    @Test
    public void testAuthenticateUserFailure() throws SQLException {
        // Insérer un utilisateur test dans la base de données
        PreparedStatement insertUserStmt = conn.prepareStatement(
                "INSERT INTO User (firstname, lastname, email, password) VALUES (?, ?, ?, ?)"
        );
        insertUserStmt.setString(1, "Jane");
        insertUserStmt.setString(2, "Doe");
        insertUserStmt.setString(3, "jane.doe@example.com");
        insertUserStmt.setString(4, "password123");  // À remplacer par un mot de passe haché en production
        insertUserStmt.executeUpdate();

        // Essayer d'authentifier l'utilisateur avec un mauvais mot de passe
        User user = userService.authenticateUser("jane.doe@example.com", "wrongpassword");

        // Vérifier que l'authentification a échoué
        assertNull(user);
    }
}
