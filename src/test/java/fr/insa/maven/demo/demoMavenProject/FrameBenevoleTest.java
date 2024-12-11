package fr.insa.maven.demo.demoMavenProject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test pour vérifier le bon fonctionnement de la classe FrameBenevole.
 */
public class FrameBenevoleTest {

    private Benevole benevole;
    private FrameBenevole frame;
    private AllMissions allMissions;

    /**
     * Préparation de l'environnement avant chaque test.
     * Initialise les objets nécessaires et vide les missions pour éviter les interférences entre les tests.
     */
    @BeforeEach
    void setUp() throws Exception {
        // Initialiser AllMissions (singleton) et vider les missions
        DatabaseManager.ensureDatabaseExists();
        DatabaseManager.executeSqlFileWithCli(DatabaseManager.SQL_FILE_PATH);
        allMissions = AllMissions.getInstance();
        allMissions.clear();

        // Création d'un objet Benevole pour les tests
        benevole = new Benevole("Elian", "Boaglio", "elian@example.com", "password123");

        // Initialisation de la fenêtre FrameBenevole avec synchronisation
        CountDownLatch latch = new CountDownLatch(1);
        SwingUtilities.invokeLater(() -> {
            frame = new FrameBenevole(allMissions, benevole);
            latch.countDown();
        });
        latch.await(); // Attendre que l'initialisation soit terminée
    }

    /**
     * Teste l'initialisation de la fenêtre FrameBenevole.
     * Vérifie que le titre de la fenêtre est correctement défini.
     */
    @Test
    void testFrameBenevoleInitialization() {
        // Vérification du titre de la fenêtre
        assertEquals("Missions du Bénévole", frame.getTitle(),
                "Le titre de la fenêtre devrait être 'Missions du Bénévole'.");
    }

    /**
     * Teste les informations associées au bénévole.
     * Vérifie que les données du bénévole sont correctement définies.
     */
    @Test
    void testBenevoleInformation() {
        // Vérification des attributs du bénévole
        assertEquals("Elian", benevole.getFirstname(),
                "Le prénom du bénévole devrait être 'Elian'.");
        assertEquals("Boaglio", benevole.getLastname(),
                "Le nom de famille du bénévole devrait être 'Boaglio'.");
        assertEquals("elian@example.com", benevole.getEmail(),
                "L'email du bénévole devrait être 'elian@example.com'.");
    }
}
