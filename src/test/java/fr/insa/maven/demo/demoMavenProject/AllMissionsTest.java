package fr.insa.maven.demo.demoMavenProject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class AllMissionsTest {

    private AllMissions allMissions;
    private Demandeur demandeur;

    // Informations de connexion MySQL local
    static final String DB_URL = "jdbc:mysql://localhost:3306/testdb";
    static final String USER = "root"; // Remplacez par votre utilisateur MySQL
    static final String PASS = "password"; // Remplacez par votre mot de passe MySQL
    static final String SQL_INIT_FILE = "Sql_Test.sql";

    @BeforeEach
    public void setUp() throws SQLException, IOException, InterruptedException {
        // Charger le fichier SQL pour créer les tables
        executeSqlFile(SQL_INIT_FILE);

        // Initialiser AllMissions et un objet Demandeur
        allMissions = AllMissions.getInstance();
        demandeur = new Demandeur(
                "Alice",
                "Dupont",
                "Besoin d'une aide pour le ménage",
                "Ménage",
                Place.HOME,
                "alice@example.com",
                "password123"
        );

        // Insérer le demandeur dans la base
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO Demandeur (email, lastname, firstname, password, description, needs, location) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, demandeur.getEmail());
            stmt.setString(2, demandeur.getLastname());
            stmt.setString(3, demandeur.getFirstname());
            stmt.setString(4, demandeur.getPassword());
            stmt.setString(5, demandeur.getDescription());
            stmt.setString(6, demandeur.getNeeds());
            stmt.setString(7, demandeur.getLocation().name());
            stmt.executeUpdate();
        }
    }

    @Test
    public void testAddMission() {
        Mission mission = new Mission(
                MissionEtat.EN_ATTENTE_AFFECTATION,
                "Mission A",
                demandeur,
                Place.HOME
        );

        allMissions.enregistrerMission(mission);
        allMissions.loadMissionsFromDatabase();

        assertEquals(1, allMissions.countMissions(), "La mission n'a pas été correctement ajoutée.");
    }

    @Test
    public void testRemoveMission() {
        Mission mission = new Mission(
                MissionEtat.EN_ATTENTE_AFFECTATION,
                "Mission B",
                demandeur,
                Place.HOME
        );

        allMissions.enregistrerMission(mission);
        allMissions.loadMissionsFromDatabase();
        allMissions.removeMission(mission);

        allMissions.loadMissionsFromDatabase();
        assertEquals(0, allMissions.countMissions(), "La mission n'a pas été correctement supprimée.");
    }

    @Test
    public void testFindMission() {
        Mission mission = new Mission(
                MissionEtat.EN_ATTENTE_AFFECTATION,
                "Mission C",
                demandeur,
                Place.HOME
        );

        allMissions.enregistrerMission(mission);
        allMissions.loadMissionsFromDatabase();

        Mission foundMission = allMissions.findMission("Mission C");
        assertNotNull(foundMission, "Mission introuvable.");
        assertEquals(MissionEtat.EN_ATTENTE_AFFECTATION, foundMission.getEtat(), "L'état de la mission est incorrect.");
    }

    @Test
    public void testUpdateMission() {
        Mission mission = new Mission(
                MissionEtat.EN_ATTENTE_AFFECTATION,
                "Mission D",
                demandeur,
                Place.HOME
        );

        allMissions.enregistrerMission(mission);
        allMissions.loadMissionsFromDatabase();

        boolean updated = allMissions.updateMission("Mission D", "VALIDEE", "Mission Updated", demandeur);
        allMissions.loadMissionsFromDatabase();

        Mission updatedMission = allMissions.findMission("Mission Updated");
        assertTrue(updated, "Mise à jour échouée.");
        assertNotNull(updatedMission, "Mission mise à jour introuvable.");
        assertEquals(MissionEtat.VALIDEE, updatedMission.getEtat(), "L'état de la mission mise à jour est incorrect.");
    }

    private void executeSqlFile(String sqlFilePath) throws IOException, InterruptedException {
        // Utilisation du ClassLoader pour récupérer le chemin du fichier dans les ressources
        String path = AllMissionsTest.class.getClassLoader().getResource(sqlFilePath).getPath();

        // Commande pour exécuter le fichier SQL
        String command = String.format("mysql -u%s -p%s testdb < %s", USER, PASS, path);
        Process process = Runtime.getRuntime().exec(command);

        // Capture des erreurs dans l'exécution de la commande
        try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String line;
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IOException("Erreur lors de l'exécution du fichier SQL. Code de sortie : " + exitCode);
        }
    }
}
