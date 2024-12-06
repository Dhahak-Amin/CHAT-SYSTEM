package fr.insa.maven.demo.demoMavenProject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ValidateurTest {

    private Connection conn;
    private Validateur validateur;

    @BeforeEach
    public void setUp() throws Exception {
        // Initialisation de la base de données locale
        DatabaseManager.ensureDatabaseExists();
        DatabaseManager.executeSqlFileWithCli(DatabaseManager.SQL_FILE_PATH);

        // Connexion à la base locale
        conn = DatabaseManager.getConnection();

        // Création du validateur
        validateur = new Validateur("John", "Doe", "validateur@example.com", "password123", conn);
    }

    @Test
    public void testValiderMissionSansBenevole() {
        // Création d'une mission sans bénévole nécessaire
        Mission mission = new Mission("Assistance générale",
                new Demandeur("Alice", "Smith", "Besoin d'aide", "Assistance", Place.OTHER, "alice@example.com", "pass123"),
                Place.OTHER);

        // Valider la mission
        validateur.validerMission(mission, new ArrayList<>());

        // Vérifications
        assertEquals(MissionEtat.VALIDEE, mission.getEtat(), "La mission doit être validée automatiquement pour l'emplacement OTHER.");
        assertNull(mission.getBenevole(), "Aucun bénévole ne doit être assigné.");
    }

    @Test
    public void testValiderMissionAvecBenevole() {
        // Création d'une mission avec un emplacement nécessitant un bénévole
        Mission mission = new Mission("Soins médicaux",
                new Demandeur("Alice", "Smith", "Besoin de soins", "Soins médicaux", Place.HOSPITAL, "alice@example.com", "pass123"),
                Place.HOSPITAL);

        // Liste de bénévoles
        List<Benevole> benevoles = new ArrayList<>();
        benevoles.add(new Benevole("Bob", "Johnson", "bob@example.com", "password", "Soins médicaux"));
        benevoles.add(new Benevole("Tom", "Clark", "tom@example.com", "password", "Ménage"));

        // Valider la mission
        validateur.validerMission(mission, benevoles);

        // Vérifications
        assertEquals(MissionEtat.VALIDEE, mission.getEtat(), "La mission doit être validée.");
        assertNotNull(mission.getBenevole(), "Un bénévole doit être assigné.");
        assertEquals("bob@example.com", mission.getBenevole().getEmail(), "Le bon bénévole doit être assigné.");
    }

    @Test
    public void testValiderMissionAvecEmplacementInvalide() {
        // Création d'une mission avec un emplacement invalide
        Mission mission = new Mission("Mission inconnue",
                new Demandeur("Alice", "Smith", "Besoin d'aide", "Divers", null, "alice@example.com", "pass123"),
                null);

        // Valider la mission
        validateur.validerMission(mission, new ArrayList<>());

        // Vérifications
        assertEquals(MissionEtat.INVALIDE, mission.getEtat(), "La mission doit être invalidée pour un emplacement invalide.");
        assertNull(mission.getBenevole(), "Aucun bénévole ne doit être assigné.");
    }
}
