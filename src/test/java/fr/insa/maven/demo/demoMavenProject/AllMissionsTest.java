package fr.insa.maven.demo.demoMavenProject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AllMissionsTest {

    private AllMissions allMissions;
    private Demandeur demandeur;
    private Benevole benevole;
    private Mission mission;

    @BeforeEach
    void setUp() {
        allMissions = new AllMissions();
        demandeur = new Demandeur("Alice", "Dupont", "Aide pour le jardinage", "Jardin", "Paris", "alice@example.com", "password123");
        benevole = new Benevole("Bob", "Martin", "bob@example.com", "password456");
    }

    @Test
    void testAddMission() {
        mission = new Mission("En attente", "Demande d'aide au jardinage", demandeur, Place.HOME);
        allMissions.addMission(mission);

        assertEquals(1, allMissions.countMissions());
        assertEquals(mission, allMissions.findMission("Demande d'aide au jardinage"));
    }

    @Test
    void testRemoveMission() {
        mission = new Mission("En attente", "Demande d'aide au jardinage", demandeur, Place.HOME);
        allMissions.addMission(mission);
        assertTrue(allMissions.removeMission("Demande d'aide au jardinage"));
        assertNull(allMissions.findMission("Demande d'aide au jardinage"));
        assertEquals(0, allMissions.countMissions());
    }

    @Test
    void testUpdateMission() {
        mission = new Mission("En attente", "Demande d'aide au jardinage", demandeur,Place.HOME);
        allMissions.addMission(mission);
        assertTrue(allMissions.updateMission("Demande d'aide au jardinage", "En cours", "Aide jardinage", demandeur));

        Mission updatedMission = allMissions.findMission("Aide jardinage");
        assertNotNull(updatedMission);
        assertEquals("En cours", updatedMission.getEtat());
        assertEquals(benevole, updatedMission.getDemandeur());
    }

    @Test
    void testFindMission() {
        mission = new Mission("En attente", "Demande d'aide au jardinage", demandeur,Place.HOME);
        allMissions.addMission(mission);
        assertNotNull(allMissions.findMission("Demande d'aide au jardinage"));
        assertNull(allMissions.findMission("Mission inconnue"));
    }

    @Test
    void testClearMissions() {
        mission = new Mission("En attente", "Demande d'aide au jardinage", demandeur,Place.HOME);
        allMissions.addMission(mission);
        allMissions.clearMissions();
        assertEquals(0, allMissions.countMissions());
    }


}
