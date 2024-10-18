package fr.insa.maven.demo.demoMavenProject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserServiceUpdateTest {

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
        conn.prepareStatement("DELETE FROM User").executeUpdate();

        // Initialiser le service utilisateur
        userService = new UserService(conn);
    }

    @Test
    public void testUpdateUser() throws SQLException {
        // Créer et enregistrer un nouvel utilisateur
        User newUser = new User("John", "Doe", "john.doe@example.com", "password123");
        userService.registerUser(newUser);

        // Mettre à jour les informations de l'utilisateur
        User updatedUser = new User("Jane", "Doe", "john.doe@example.com", "newpassword123");
        userService.updateUser(updatedUser);

        // Authentifier et vérifier que les changements ont bien été appliqués
        User authenticatedUser = userService.authenticateUser("john.doe@example.com", "newpassword123");

        // Vérifier que les informations ont été mises à jour
        assertEquals("Jane", authenticatedUser.getFirstname());
        assertEquals("Doe", authenticatedUser.getLastname());
        assertEquals("john.doe@example.com", authenticatedUser.getEmail());
    }
}
