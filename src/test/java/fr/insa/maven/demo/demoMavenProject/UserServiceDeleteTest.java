package fr.insa.maven.demo.demoMavenProject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNull;

public class UserServiceDeleteTest {

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
    public void testDeleteUser() throws SQLException {
        // Créer et enregistrer un nouvel utilisateur
        User newUser = new User("John", "Doe", "john.doe@example.com", "password123");
        userService.registerUser(newUser);

        // Supprimer l'utilisateur
        userService.deleteUser("john.doe@example.com");

        // Tenter d'authentifier l'utilisateur après suppression
        User deletedUser = userService.authenticateUser("john.doe@example.com", "password123");

        // Vérifier que l'utilisateur n'existe plus
        assertNull(deletedUser);
    }
}
