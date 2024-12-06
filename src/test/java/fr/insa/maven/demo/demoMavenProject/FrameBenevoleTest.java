package fr.insa.maven.demo.demoMavenProject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

public class FrameBenevoleTest {

    private AllMissions allMissions;
    private Benevole benevole;
    private Demandeur demandeur;
    private FrameBenevole frame;

    @BeforeEach
    void setUp() throws InterruptedException {
        // Initialisation des objets avec le singleton AllMissions
        allMissions = AllMissions.getInstance();

        benevole = new Benevole("Elian", "Boaglio", "elian@example.com", "password123", "Jardinage");
        demandeur = new Demandeur("Alice", "Dupont", "Besoin d'aide", "Jardinage", Place.HOME, "alice@example.com", "password456");

        // Ajout d'exemples de missions
        allMissions.addMission(new Mission(MissionEtat.EN_COURS_DE_VALIDATION, "Aide aux courses", demandeur, Place.HOME));
        allMissions.addMission(new Mission(MissionEtat.EN_ATTENTE_AFFECTATION, "Nettoyage de jardin", demandeur, Place.WORKPLACE));
        allMissions.addMission(new Mission(MissionEtat.TERMINEE, "Déménagement", demandeur, Place.HOME));

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
        // Vérifier que le titre est correct
        assertEquals("Missions du Bénévole", frame.getTitle(), "Le titre de la fenêtre devrait être 'Missions du Bénévole'.");
    }

    @Test
    void testChangeMissionStatus() {
        // Simuler la sélection d'une mission et le changement de statut
        DefaultListModel<Mission> missionListModel = frame.getMissionListModel();
        Mission selectedMission = missionListModel.get(1);

        // Vérifier le statut initial
        assertEquals(MissionEtat.EN_ATTENTE_AFFECTATION, selectedMission.getEtat(), "Le statut initial de la mission devrait être 'EN_ATTENTE_AFFECTATION'.");

        // Simuler le changement de statut
        selectedMission.setEtat(MissionEtat.VALIDEE);

        // Vérifier que le statut a changé
        assertEquals(MissionEtat.VALIDEE, selectedMission.getEtat(), "Le statut de la mission devrait être 'VALIDEE'.");
    }

}
