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

        // Nettoyer la table Demandeur avant chaque test pour s'assurer que les données sont propres
        try (PreparedStatement clearDemandeurTable = conn.prepareStatement("DELETE FROM Demandeur")) {
            clearDemandeurTable.executeUpdate();
        }

        // Initialiser AllMissions et un objet Demandeur
        allMissions = new AllMissions();

        // Instanciation de Demandeur avec les paramètres correspondant à la structure de la table
        demandeur = new Demandeur("Alice", "Dupont", "Besoin d'une aide pour le ménage", "Ménage", Place.HOME, "alice@example.com", "password123");

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
            insertDemandeurStmt.setObject(7, demandeur.getLocation().name()); // Pour l'énumération
            insertDemandeurStmt.executeUpdate();
        }
    }

    @Test
    public void testAddMission() {
        // Ajouter une mission
        mission = new Mission("Simple Mission", Place.HOME, MissionEtat.EN_ATTENTE_AFFECTATION, demandeur);
        allMissions.addMission(mission);

        // Vérifier que la mission a été ajoutée
        assertEquals(1, allMissions.countMissions(), "Le nombre de missions devrait être 1 après l'ajout.");
    }

    @Test
    public void testFindMission() {
        // Ajouter une mission et la rechercher
        mission = new Mission("Simple Mission", Place.HOME, MissionEtat.EN_ATTENTE_AFFECTATION, demandeur);
        allMissions.addMission(mission);
<<<<<<< Updated upstream
        assertTrue(allMissions.updateMission("Demande d'aide au jardinage", "En cours", "Aide jardinage", demandeur));

        Mission updatedMission = allMissions.findMission("Aide jardinage");
        assertNotNull(updatedMission);
        assertEquals("En cours", updatedMission.getEtat());
        assertEquals(benevole, updatedMission.getDemandeur());
=======

        Mission foundMission = allMissions.findMission("Simple Mission");
        assertNotNull(foundMission, "La mission ajoutée devrait être trouvée.");
        assertEquals(MissionEtat.EN_ATTENTE_AFFECTATION, foundMission.getEtat(), "L'état de la mission devrait être EN_ATTENTE_AFFECTATION.");
>>>>>>> Stashed changes
    }

  /*  @Test
    public void testRemoveMission() {
        // Ajouter une mission, puis la supprimer
        mission = new Mission("Simple Mission", Place.HOME, MissionEtat.EN_ATTENTE_AFFECTATION, demandeur);
        allMissions.addMission(mission);
        boolean removed = allMissions.removeMission("Simple Mission");

        // Vérifier que la mission a été supprimée
        assertTrue(removed, "La mission devrait être supprimée.");
        assertEquals(0, allMissions.countMissions(), "Le nombre de missions devrait être 0 après la suppression.");
    }

   */
}
