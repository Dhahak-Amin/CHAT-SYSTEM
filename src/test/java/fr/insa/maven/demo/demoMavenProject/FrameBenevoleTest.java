package fr.insa.maven.demo.demoMavenProject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import static org.junit.jupiter.api.Assertions.*;

public class FrameBenevoleTest {

    private AllMissions allMissions;
    private Benevole benevole;
    private Demandeur demandeur;
    private FrameBenevole frame;

    @BeforeEach
    void setUp() {
        // Initialisation des objets
        allMissions = new AllMissions();
        benevole = new Benevole("Elian", "Boaglio", "elian@example.com", "password123", "Jardinage");
        demandeur = new Demandeur("Alice", "Dupont", "Besoin d'aide", "Jardinage", Place.HOME, "alice@example.com", "password456");

        // Exemple de missions
        allMissions.addMission(new Mission(MissionEtat.EN_COURS, "Aide aux courses", demandeur, Place.HOME));
        allMissions.addMission(new Mission(MissionEtat.EN_ATTENTE_AFFECTATION, "Nettoyage de jardin", demandeur, Place.WORKPLACE));
        allMissions.addMission(new Mission(MissionEtat.TERMINEE, "Déménagement", demandeur, Place.HOME));

        // Initialisation de la fenêtre
        frame = new FrameBenevole(allMissions, benevole);
    }

    @Test
    void testFrameBenevoleInitialization() {
        // Vérifier que le titre est correct
        assertEquals("Missions du Bénévole", frame.getTitle(), "Le titre de la fenêtre devrait être 'Missions du Bénévole'.");

        // Vérifier que la liste des missions est initialisée correctement
        DefaultListModel<Mission> missionListModel = frame.getMissionListModel();
        assertEquals(3, missionListModel.size(), "Le modèle de liste devrait contenir 3 missions.");
    }

    @Test
    void testChangeMissionStatus() {
        // Simuler la sélection d'une mission et le changement de statut
        DefaultListModel<Mission> missionListModel = frame.getMissionListModel();
        Mission selectedMission = missionListModel.get(0);

        // Vérifier le statut initial
        assertEquals(MissionEtat.EN_COURS, selectedMission.getEtat(), "Le statut initial de la mission devrait être 'EN_COURS'.");

        // Simuler le changement de statut
        selectedMission.setEtat(MissionEtat.VALIDEE);

        // Vérifier que le statut a changé
        assertEquals(MissionEtat.VALIDEE, selectedMission.getEtat(), "Le statut de la mission devrait être 'VALIDEE'.");
    }

    @Test
    void testDisplayMissionsInUI() {
        // Vérifier que toutes les missions sont affichées correctement
        DefaultListModel<Mission> missionListModel = frame.getMissionListModel();
        assertEquals(3, missionListModel.size(), "Le modèle de liste devrait contenir 3 missions.");

        // Vérifier les détails de la première mission affichée
        Mission firstMission = missionListModel.get(0);
        assertEquals("Aide aux courses", firstMission.getIntitule(), "La première mission devrait correspondre à 'Aide aux courses'.");
    }
}
