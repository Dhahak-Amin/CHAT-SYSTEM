package fr.insa.maven.demo.demoMavenProject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FrameDemandeurTest {

    private AllMissions allMissions;
    private Demandeur demandeur;
    private FrameDemandeur frame;

    @BeforeEach
    void setUp() {

        DatabaseManager.ensureDatabaseExists();
        DatabaseManager.executeSqlFileWithCli(DatabaseManager.SQL_FILE_PATH);
        // Création du demandeur
        demandeur = new Demandeur("Alice", "Dupont", "Besoin d'aide", "Jardin", Place.HOME, "alice@example.com", "password123");

        // Initialisation et nettoyage des missions
        allMissions = AllMissions.getInstance();
        allMissions.clear(); // Suppression des missions existantes

        // Ajout de missions
        allMissions.addMission(new Mission(MissionEtat.EN_COURS_DE_VALIDATION, "Aide aux courses", demandeur, Place.HOME));
        allMissions.addMission(new Mission(MissionEtat.EN_ATTENTE_AFFECTATION, "Nettoyage de jardin", demandeur, Place.WORKPLACE));
        allMissions.addMission(new Mission(MissionEtat.TERMINEE, "Déménagement", demandeur, Place.HOME));

        // Initialisation de la fenêtre
       // SwingUtilities.invokeLater(() -> frame = new FrameDemandeur(demandeur));
    }

    @Test
    void testFrameDemandeurInitialization() {
        SwingUtilities.invokeLater(() -> assertEquals("Missions de Alice Dupont", frame.getTitle(),
                "Le titre de la fenêtre devrait correspondre au demandeur."));

        List<Mission> missions = allMissions.getMissions();
        assertEquals(3, missions.size(), "Le nombre de missions dans la liste devrait être 3.");
    }

    @Test
    void testAddMission() {
        String newMissionTitle = "Réparation d'appareils";
        Place newPlace = Place.HOSPITAL;

        Mission newMission = demandeur.createMission(newMissionTitle, newPlace);
        allMissions.addMission(newMission);

        assertEquals(4, allMissions.getMissions().size(), "Le nombre de missions devrait être 4 après ajout.");
        assertTrue(allMissions.getMissions().stream().anyMatch(mission -> mission.getIntitule().equals(newMissionTitle)),
                "La nouvelle mission devrait être présente dans la liste.");
    }

    @Test
    void testDeleteMission() {
        Mission missionToDelete = allMissions.findMission("Aide aux courses");
        boolean removed = allMissions.removeMission(missionToDelete);

        assertTrue(removed, "La mission devrait être supprimée.");
        assertEquals(2, allMissions.getMissions().size(), "Le nombre de missions devrait être 2 après suppression.");
        assertFalse(allMissions.getMissions().stream().anyMatch(mission -> mission.getIntitule().equals("Aide aux courses")),
                "La mission supprimée ne devrait plus être présente dans la liste.");
    }

    @Test
    void testDisplayMissionsInUI() {
        SwingUtilities.invokeLater(() -> {
            DefaultListModel<Mission> missionListModel = frame.getMissionListModel();
            assertEquals(3, missionListModel.size(), "Le modèle de liste devrait contenir 3 missions.");

            Mission firstMission = missionListModel.get(0);
            assertEquals("Aide aux courses", firstMission.getIntitule(), "La première mission affichée devrait correspondre.");
        });
    }
}
