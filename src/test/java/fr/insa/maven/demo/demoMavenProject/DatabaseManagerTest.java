package fr.insa.maven.demo.demoMavenProject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.SQLException;


public class DatabaseManagerTest {

    @BeforeEach
    public void setUp() {
        // Nettoie la base de données avant chaque test
        DatabaseManager.cleanDatabase();
    }

    @Test
    public void testDatabaseCreation() {
        // Vérifie que la base de données a bien été créée
        assertTrue(DatabaseManager.checkTableExists("User"), "La table User n'existe pas.");
        assertTrue(DatabaseManager.checkTableExists("Demandeur"), "La table Demandeur n'existe pas.");
    }
}
