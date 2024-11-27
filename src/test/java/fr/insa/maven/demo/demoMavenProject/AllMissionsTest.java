package fr.insa.maven.demo.demoMavenProject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class AllMissionsTest {

    private AllMissions allMissions;
    private Demandeur demandeur;
    private Mission mission;

    // Informations de connexion à la base de données
    static final String DB_URL = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/projet_gei_012";
    static final String USER = "projet_gei_012";
    static final String PASS = "dith1Que";

    @BeforeEach
    public void setUp() throws SQLException {
        // Établir la connexion à la base de données
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

        // Nettoyer la table Mission avant chaque test
        try (PreparedStatement clearMissionTable = conn.prepareStatement("DELETE FROM Mission")) {
            clearMissionTable.executeUpdate();
        }

        // Nettoyer la table Demandeur avant chaque test
        try (PreparedStatement clearDemandeurTable = conn.prepareStatement("DELETE FROM Demandeur")) {
            clearDemandeurTable.executeUpdate();
        }

        // Initialiser AllMissions et un objet Demandeur
        allMissions = new AllMissions();
        demandeur = new Demandeur(
                "Alice",
                "Dupont",
                "Besoin d'une aide pour le ménage",
                "Ménage",
                Place.HOME,
                "alice@example.com",
                "password123"
        );

        // Insérer le demandeur dans la base de données
        try (PreparedStatement insertDemandeurStmt = conn.prepareStatement(
                "INSERT INTO Demandeur (email, lastname, firstname, password, description, needs, location) VALUES (?, ?, ?, ?, ?, ?, ?)"
        )) {
            insertDemandeurStmt.setString(1, demandeur.getEmail());
            insertDemandeurStmt.setString(2, demandeur.getLastname());
            insertDemandeurStmt.setString(3, demandeur.getFirstname());
            insertDemandeurStmt.setString(4, demandeur.getPassword());
            insertDemandeurStmt.setString(5, demandeur.getDescription());
            insertDemandeurStmt.setString(6, demandeur.getNeeds());
            insertDemandeurStmt.setString(7, demandeur.getLocation().name()); // Pour l'énumération
            insertDemandeurStmt.executeUpdate();
        }
    }

    @Test
<<<<<<< HEAD
    void testAddMission() {
        mission = new Mission(MissionEtat.INVALIDE, "Demande d'aide au jardinage", demandeur, Place.HOME);
=======
    public void testAddMission() {
        // Ajouter une mission
        mission = new Mission(
                MissionEtat.EN_ATTENTE_AFFECTATION,
                "Simple Mission",
                demandeur,
                Place.HOME
        );
>>>>>>> d84b19edeb6e937aad110453e26e87d060aa3a4a
        allMissions.addMission(mission);

        // Vérifier que la mission a été ajoutée
        assertEquals(1, allMissions.countMissions(), "Le nombre de missions devrait être 1 après l'ajout.");
    }

    @Test
<<<<<<< HEAD
    void testRemoveMission() {
        mission = new Mission(MissionEtat.EN_COURS, "Demande d'aide au jardinage", demandeur, Place.HOME);
=======
    public void testFindMission() {
        // Ajouter une mission et la rechercher
        mission = new Mission(
                MissionEtat.EN_ATTENTE_AFFECTATION,
                "Simple Mission",
                demandeur,
                Place.HOME
        );
>>>>>>> d84b19edeb6e937aad110453e26e87d060aa3a4a
        allMissions.addMission(mission);

        Mission foundMission = allMissions.findMission("Simple Mission");
        assertNotNull(foundMission, "La mission ajoutée devrait être trouvée.");
        assertEquals(MissionEtat.EN_ATTENTE_AFFECTATION, foundMission.getEtat(), "L'état de la mission devrait être EN_ATTENTE.");
    }

    @Test
<<<<<<< HEAD
    void testUpdateMission() {
        mission = new Mission(MissionEtat.EN_COURS, "Demande d'aide au jardinage", demandeur,Place.HOME);
        allMissions.addMission(mission);
        assertTrue(allMissions.updateMission("Demande d'aide au jardinage", MissionEtat.EN_COURS, "Aide jardinage", demandeur));
=======
    public void testRemoveMission() {
        // Ajouter une mission, puis la supprimer
        mission = new Mission(
                MissionEtat.EN_ATTENTE_AFFECTATION,
                "Simple Mission",
                demandeur,
                Place.HOME
        );
        allMissions.addMission(mission);
        boolean removed = allMissions.removeMission("Simple Mission");
>>>>>>> d84b19edeb6e937aad110453e26e87d060aa3a4a

        // Vérifier que la mission a été supprimée
        assertTrue(removed, "La mission devrait être supprimée.");
        assertEquals(0, allMissions.countMissions(), "Le nombre de missions devrait être 0 après la suppression.");
    }

    @Test
<<<<<<< HEAD
    void testFindMission() {
        mission = new Mission(MissionEtat.TERMINEE, "Demande d'aide au jardinage", demandeur,Place.HOME);
=======
    public void testUpdateMission() {
        // Ajouter une mission, puis la mettre à jour
        mission = new Mission(
                MissionEtat.EN_ATTENTE_AFFECTATION,
                "Simple Mission",
                demandeur,
                Place.HOME
        );
>>>>>>> d84b19edeb6e937aad110453e26e87d060aa3a4a
        allMissions.addMission(mission);

        boolean updated = allMissions.updateMission(
                "Simple Mission",
                "EN_COURS",
                "Updated Mission",
                demandeur
        );

        // Vérifier que la mission a été mise à jour
        assertTrue(updated, "La mission devrait être mise à jour.");
        Mission updatedMission = allMissions.findMission("Updated Mission");
        assertNotNull(updatedMission, "La mission mise à jour devrait être trouvée.");
        assertEquals(MissionEtat.EN_COURS, updatedMission.getEtat(), "L'état de la mission devrait être EN_COURS.");
    }
<<<<<<< HEAD

    @Test
    void testClearMissions() {
        mission = new Mission(MissionEtat.VALIDEE, "Demande d'aide au jardinage", demandeur,Place.HOME);
        allMissions.addMission(mission);
        allMissions.clearMissions();
        assertEquals(0, allMissions.countMissions());
    }


=======
>>>>>>> d84b19edeb6e937aad110453e26e87d060aa3a4a
}
