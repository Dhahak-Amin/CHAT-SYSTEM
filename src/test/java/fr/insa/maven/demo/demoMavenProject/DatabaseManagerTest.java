package fr.insa.maven.demo.demoMavenProject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test pour les fonctionnalités de la classe DatabaseManager.
 */
public class DatabaseManagerTest {

    /**
     * Méthode exécutée avant chaque test.
     * Nettoie la base de données pour garantir un état initial cohérent.
     */
    @BeforeEach
    public void setUp() {

        try {
            // Nettoyage de la base de données avant chaque test
            DatabaseManager.cleanDatabase();
        } catch (Exception e) {
            fail("Erreur lors du nettoyage de la base de données : " + e.getMessage());
        }
    }

    /**
     * Teste la création de la base de données et des tables nécessaires.
     * Vérifie si les tables "User" et "Demandeur" existent après l'initialisation.
     */
    @Test
    public void testDatabaseCreation() {
        try {
            // S'assurer que la base de données existe
            DatabaseManager.ensureDatabaseExists();

            // Vérification des tables essentielles
            assertTrue(DatabaseManager.checkTableExists("User"), "La table 'User' n'existe pas.");
            assertTrue(DatabaseManager.checkTableExists("Demandeur"), "La table 'Demandeur' n'existe pas.");
        } catch (Exception e) {
            fail("Erreur lors de la vérification des tables : " + e.getMessage());
        }
    }
}
