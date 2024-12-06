package fr.insa.maven.demo.demoMavenProject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AllMissionsTest {

    private AllMissions allMissions;
    private Demandeur demandeur;

    @BeforeEach
    public void setUp() {
        // Initialisation de l'instance et nettoyage des missions
        allMissions = AllMissions.getInstance();
        allMissions.clearMissions();

        // Création d'un demandeur simple
        demandeur = new Demandeur(
                "Alice",
                "Dupont",
                "Besoin d'aide",
                "Ménage",
                Place.HOME,
                "alice@example.com",
                "password123"
        );
    }

    @Test
    public void testAddMission() {
        // Création et ajout d'une mission
        Mission mission = new Mission(
                MissionEtat.EN_ATTENTE_AFFECTATION,
                "Nettoyer la maison",
                demandeur,
                Place.HOME
        );
        allMissions.enregistrerMission(mission);

        // Vérification que la mission a été ajoutée
        allMissions.loadMissionsFromDatabase();
        assertEquals(1, allMissions.countMissions(), "La mission n'a pas été correctement ajoutée.");
        assertNotNull(allMissions.findMission("Nettoyer la maison"), "La mission ajoutée est introuvable.");
    }

    @Test
    public void testRemoveMission() {
        // Création et ajout d'une mission
        Mission mission = new Mission(
                MissionEtat.EN_ATTENTE_AFFECTATION,
                "Réparer la toiture",
                demandeur,
                Place.WORKPLACE
        );
        allMissions.enregistrerMission(mission);

        // Vérification de l'ajout
        allMissions.loadMissionsFromDatabase();
        assertEquals(1, allMissions.countMissions(), "La mission n'a pas été correctement ajoutée.");

        // Suppression de la mission
        boolean removed = allMissions.removeMission(mission);
        allMissions.loadMissionsFromDatabase();

        // Vérification de la suppression
        assertTrue(removed, "La mission n'a pas été supprimée.");
        assertEquals(0, allMissions.countMissions(), "La mission existe encore après suppression.");
    }



    @Test
    public void testLoadMissionsForDemandeur() {
        // Création de plusieurs missions
        Mission mission1 = new Mission(
                MissionEtat.EN_ATTENTE_AFFECTATION,
                "Mission 1",
                demandeur,
                Place.HOME
        );
        Mission mission2 = new Mission(
                MissionEtat.EN_ATTENTE_AFFECTATION,
                "Mission 2",
                demandeur,
                Place.WORKPLACE
        );

        allMissions.enregistrerMission(mission1);
        allMissions.enregistrerMission(mission2);

        // Chargement des missions
        allMissions.loadMissionsFromDatabase();
        int count = (int) allMissions.getMissions().stream()
                .filter(m -> m.getDemandeur().getEmail().equals(demandeur.getEmail()))
                .count();

        // Vérification du nombre de missions associées
        assertEquals(2, count, "Le nombre de missions pour le demandeur est incorrect.");
    }
}
