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
        // Création du demandeur et initialisation des missions
        demandeur = new Demandeur("Alice", "Dupont", "Besoin d'aide", "Jardin", Place.HOME, "alice@example.com", "password123");
        allMissions = new AllMissions();

        // Ajout de missions
        allMissions.addMission(new Mission(MissionEtat.EN_COURS, "Aide aux courses", demandeur, Place.HOME));
        allMissions.addMission(new Mission(MissionEtat.EN_ATTENTE_AFFECTATION, "Nettoyage de jardin", demandeur, Place.WORKPLACE));
        allMissions.addMission(new Mission(MissionEtat.TERMINEE, "Déménagement", demandeur, Place.HOME));

        // Initialisation de la fenêtre
        frame = new FrameDemandeur(allMissions, demandeur);
    }

    @Test
    void testFrameDemandeurInitialization() {
        // Vérifier que le titre de la fenêtre est correctement défini
        assertEquals("Missions de Alice Dupont", frame.getTitle(), "Le titre de la fenêtre devrait correspondre au demandeur.");

        // Vérifier que les missions sont correctement affichées dans le modèle
        List<Mission> missions = allMissions.getMissions();
        assertEquals(3, missions.size(), "Le nombre de missions dans la liste devrait être 3.");
    }

    @Test
    void testAddMission() {
        // Simuler l'ajout d'une mission
        String newMissionTitle = "Réparation d'appareils";
        Place newPlace = Place.HOSPITAL;

        // Ajouter une mission
        Mission newMission = demandeur.createMission(newMissionTitle, newPlace);
        allMissions.addMission(newMission);

        // Vérifier que la mission a été ajoutée
        assertEquals(4, allMissions.getMissions().size(), "Le nombre de missions devrait être 4 après ajout.");
        assertTrue(allMissions.getMissions().stream().anyMatch(mission -> mission.getIntitule().equals(newMissionTitle)),
                "La nouvelle mission devrait être présente dans la liste.");
    }

    @Test
    void testDeleteMission() {
        // Supprimer une mission existante
        String missionToDelete = "Aide aux courses";
        boolean removed = allMissions.removeMission(missionToDelete);

        // Vérifier que la mission a été supprimée
        assertTrue(removed, "La mission devrait être supprimée.");
        assertEquals(2, allMissions.getMissions().size(), "Le nombre de missions devrait être 2 après suppression.");
        assertFalse(allMissions.getMissions().stream().anyMatch(mission -> mission.getIntitule().equals(missionToDelete)),
                "La mission supprimée ne devrait plus être présente dans la liste.");
    }

    @Test
    void testDisplayMissionsInUI() {
        // Vérifier que les missions sont affichées dans le modèle de liste
        DefaultListModel<Mission> missionListModel = frame.getMissionListModel();
        assertEquals(3, missionListModel.size(), "Le modèle de liste devrait contenir 3 missions.");

        // Vérifier les détails d'une mission affichée
        Mission firstMission = missionListModel.get(0);
        assertEquals("Aide aux courses", firstMission.getIntitule(), "La première mission affichée devrait correspondre.");
    }
}
