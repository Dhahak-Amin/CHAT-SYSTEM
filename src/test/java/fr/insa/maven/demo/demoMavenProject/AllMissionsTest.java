package fr.insa.maven.demo.demoMavenProject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe AllMissions.
 * Vérifie les fonctionnalités principales liées à la gestion des missions.
 */
public class AllMissionsTest {

    private AllMissions allMissions; // Instance de gestion des missions
    private Demandeur demandeur;    // Demandeur utilisé dans les tests

    /**
     * Initialisation avant chaque test.
     * Nettoie les missions existantes et configure un demandeur simple.
     */
    @BeforeEach
    public void setUp() {
        try {
            // Initialisation de l'instance et nettoyage des missions
            allMissions = AllMissions.getInstance();
            allMissions.clearMissions();

            // Création d'un demandeur pour les tests
            demandeur = new Demandeur(
                    "Alice",
                    "Dupont",
                    "Besoin d'aide",
                    "Ménage",
                    Place.HOME,
                    "alice@example.com",
                    "password123"
            );
        } catch (Exception e) {
            fail("Erreur lors de l'initialisation : " + e.getMessage());
        }
    }

    /**
     * Vérifie l'ajout d'une mission et sa persistance dans la base de données.
     */
    @Test
    public void testAddMission() {
        try {
            // Création et ajout d'une mission
            Mission mission = new Mission(
                    MissionEtat.EN_ATTENTE_AFFECTATION,
                    "Nettoyer la maison",
                    demandeur,
                    Place.HOME
            );
            allMissions.enregistrerMission2(mission);

            // Vérification que la mission a été ajoutée
            allMissions.loadMissionsFromDatabase();
            assertEquals(1, allMissions.countMissions(), "La mission n'a pas été correctement ajoutée.");
            assertNotNull(allMissions.findMission("Nettoyer la maison"), "La mission ajoutée est introuvable.");
        } catch (Exception e) {
            fail("Erreur lors de l'ajout de la mission : " + e.getMessage());
        }
    }

    /**
     * Vérifie la suppression d'une mission et son retrait de la base de données.
     */
    @Test
    public void testRemoveMission() {
        try {
            // Création et ajout d'une mission
            Mission mission = new Mission(
                    MissionEtat.EN_ATTENTE_AFFECTATION,
                    "Réparer la toiture",
                    demandeur,
                    Place.WORKPLACE
            );
            allMissions.enregistrerMission2(mission);

            // Vérification de l'ajout
            allMissions.loadMissionsFromDatabase();
            assertEquals(1, allMissions.countMissions(), "La mission n'a pas été correctement ajoutée.");

            // Suppression de la mission
            boolean removed = allMissions.removeMission(mission);
            allMissions.loadMissionsFromDatabase();

            // Vérification de la suppression
            assertTrue(removed, "La mission n'a pas été supprimée.");
            assertEquals(0, allMissions.countMissions(), "La mission existe encore après suppression.");
        } catch (Exception e) {
            fail("Erreur lors de la suppression de la mission : " + e.getMessage());
        }
    }

    /**
     * Vérifie le chargement des missions associées à un demandeur spécifique.
     */
    @Test
    public void testLoadMissionsForDemandeur() {
        try {
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

            allMissions.enregistrerMission2(mission1);
            allMissions.enregistrerMission2(mission2);

            // Chargement des missions
            allMissions.loadMissionsFromDatabase();
            long count = allMissions.getMissions().stream()
                    .filter(m -> m.getDemandeur().getEmail().equals(demandeur.getEmail()))
                    .count();

            // Vérification du nombre de missions associées
            assertEquals(2, count, "Le nombre de missions pour le demandeur est incorrect.");
        } catch (Exception e) {
            fail("Erreur lors du chargement des missions : " + e.getMessage());
        }
    }
}
