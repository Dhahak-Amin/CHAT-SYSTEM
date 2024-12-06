package fr.insa.maven.demo.demoMavenProject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

public class FrameBenevoleTest {

    private Benevole benevole;
    private FrameBenevole frame;
    private AllMissions allMissions;

    @BeforeEach
    void setUp() throws Exception {
        // Créer une instance vide d'AllMissions
        allMissions = AllMissions.getInstance();
        allMissions.clear(); // S'assurer que les missions sont vides

        // Création d'un objet Benevole
        benevole = new Benevole("Elian", "Boaglio", "elian@example.com", "password123");

        // Initialisation de la fenêtre avec synchronisation
        CountDownLatch latch = new CountDownLatch(1);
        SwingUtilities.invokeLater(() -> {
            frame = new FrameBenevole(allMissions, benevole);
            latch.countDown();
        });
        latch.await(); // Attendre que la fenêtre soit complètement initialisée
    }

    @Test
    void testFrameBenevoleInitialization() {
        // Vérifier que le titre de la fenêtre est correct
        assertEquals("Missions du Bénévole", frame.getTitle(), "Le titre de la fenêtre devrait être 'Missions du Bénévole'.");
    }

    @Test
    void testBenevoleInformation() {
        // Vérifier les informations du bénévole
        assertEquals("Elian", benevole.getFirstname(), "Le prénom du bénévole devrait être 'Elian'.");
        assertEquals("Boaglio", benevole.getLastname(), "Le nom de famille du bénévole devrait être 'Boaglio'.");
        assertEquals("elian@example.com", benevole.getEmail(), "L'email du bénévole devrait être 'elian@example.com'.");
    }
}
