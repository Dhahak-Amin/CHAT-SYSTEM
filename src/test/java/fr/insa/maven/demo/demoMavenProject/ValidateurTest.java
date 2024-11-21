package fr.insa.maven.demo.demoMavenProject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidateurTest {

    private Validateur validateur;
    private Demandeur demandeur;
    private Benevole benevole;
    private Benevole benevole2;
    private Mission mission;
    private List<Benevole> benevoles;
    private Connection conn;

    // Informations de connexion à la base de données
    static final String DB_URL = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/projet_gei_012";
    static final String USER = "projet_gei_012";
    static final String PASS = "dith1Que";

    @BeforeEach
    void setUp() throws SQLException {
        conn = DriverManager.getConnection(DB_URL, USER, PASS);

        // Nettoyer les tables avant chaque test
        try (PreparedStatement clearMissionTable = conn.prepareStatement("DELETE FROM Mission")) {
            clearMissionTable.executeUpdate();
        }
        try (PreparedStatement clearDemandeurTable = conn.prepareStatement("DELETE FROM Demandeur")) {
            clearDemandeurTable.executeUpdate();
        }
        try (PreparedStatement clearBenevoleTable = conn.prepareStatement("DELETE FROM Benevole")) {
            clearBenevoleTable.executeUpdate();
        }

        // Initialisation des entités
        validateur = new Validateur("Ali", "pope", "tsahawasasas6568@example.com", "password123", conn);
        demandeur = new Demandeur(
                "Amine",
                "Dupont",
                "Besoin d'aide pour le ménage",
                "Ménage",
                Place.HOME,
                "alice@example.com",
                "password123"
        );

        // Insérer le demandeur dans la base de données
        insertDemandeur(demandeur);

        // Initialisation des bénévoles
        benevoles = new ArrayList<>();
        benevole = new Benevole("Elian", "Dupont", "bob@example.com", "password456", "Ménage");
        benevole2 = new Benevole("Kylian", "Mbappé", "kiki@example.com", "password00", "Médecin");

        insertBenevole(benevole);
        insertBenevole(benevole2);
        benevoles.add(benevole);
        benevoles.add(benevole2);

        System.out.println("SetUp complet : les entités ont été initialisées et insérées dans la base de données.");
    }

    private void insertDemandeur(Demandeur demandeur) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO Demandeur (lastname, firstname, description, needs, location, email, password) VALUES (?, ?, ?, ?, ?, ?, ?)"
        )) {
            stmt.setString(1, demandeur.getLastname());
            stmt.setString(2, demandeur.getFirstname());
            stmt.setString(3, demandeur.getDescription());
            stmt.setString(4, demandeur.getNeeds());
            stmt.setObject(5, demandeur.getLocation().name());
            stmt.setString(6, demandeur.getEmail());
            stmt.setString(7, demandeur.getPassword());
            stmt.executeUpdate();
            System.out.println("Demandeur inséré : " + demandeur.getFirstname() + " " + demandeur.getLastname());
        }
    }

    private void insertBenevole(Benevole benevole) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO Benevole (lastname, firstname, email, password, metier) VALUES (?, ?, ?, ?, ?)"
        )) {
            stmt.setString(1, benevole.getLastname());
            stmt.setString(2, benevole.getFirstname());
            stmt.setString(3, benevole.getEmail());
            stmt.setString(4, benevole.getPassword());
            stmt.setString(5, benevole.getMetier());
            stmt.executeUpdate();
            System.out.println("Bénévole inséré : " + benevole.getFirstname() + " " + benevole.getLastname());
        }
    }

    @Test
    void testValiderMissionAvecBenevoleDisponible() {
        System.out.println("Début du test : testValiderMissionAvecBenevoleDisponible");
        mission = new Mission(MissionEtat.EN_ATTENTE_AFFECTATION, "Ménage", demandeur, Place.HOME);

        validateur.validerMission(mission, benevoles);

        assertEquals(MissionEtat.VALIDEE, mission.getEtat(), "La mission devrait être validée.");
        assertEquals(benevole, mission.getBenevole(), "Le bénévole devrait être attribué.");
        System.out.println("Mission validée avec succès : " + mission.getIntitule());

        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM Mission WHERE intitule = ?"
        )) {
            stmt.setString(1, mission.getIntitule());
            ResultSet resultSet = stmt.executeQuery();
            assertTrue(resultSet.next(), "La mission validée devrait être présente dans la base.");
            assertEquals(mission.getEtat().name(), resultSet.getString("etat"));
            assertEquals(demandeur.getEmail(), resultSet.getString("demandeur_email"));
            assertEquals(benevole.getEmail(), resultSet.getString("benevole_email"));
            System.out.println("Vérification en base réussie pour la mission : " + mission.getIntitule());
        } catch (SQLException e) {
            fail("Erreur SQL : " + e.getMessage());
        }
    }

    @Test
    void testValiderMissionSansBenevoleDisponible() {
        System.out.println("Début du test : testValiderMissionSansBenevoleDisponible");
        mission = new Mission(MissionEtat.EN_ATTENTE_AFFECTATION, "Ménage", demandeur, Place.HOME);

        benevoles.clear();

        validateur.validerMission(mission, benevoles);

        assertEquals(MissionEtat.EN_ATTENTE_AFFECTATION, mission.getEtat(), "La mission ne devrait pas être validée.");
        assertNull(mission.getBenevole(), "Aucun bénévole ne devrait être attribué.");
        System.out.println("Test réussi : aucun bénévole disponible, mission non validée.");
    }

    @Test
    void testAjoutValidateurDansBaseDeDonnees() throws SQLException {
        System.out.println("Début du test : testAjoutValidateurDansBaseDeDonnees");
        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO Validateur (lastname, firstname, email, password) VALUES (?, ?, ?, ?)"
        )) {
            stmt.setString(1, validateur.getLastname());
            stmt.setString(2, validateur.getFirstname());
            stmt.setString(3, validateur.getEmail());
            stmt.setString(4, validateur.getPassword());
            stmt.executeUpdate();
            System.out.println("Validateur inséré : " + validateur.getFirstname() + " " + validateur.getLastname());
        }

        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM Validateur WHERE email = ?"
        )) {
            stmt.setString(1, validateur.getEmail());
            ResultSet resultSet = stmt.executeQuery();
            assertTrue(resultSet.next(), "Le validateur devrait être présent.");
            assertEquals(validateur.getLastname(), resultSet.getString("lastname"));
            assertEquals(validateur.getFirstname(), resultSet.getString("firstname"));
            System.out.println("Validateur trouvé en base : " + validateur.getFirstname() + " " + validateur.getLastname());
        }
    }

    @Test
    void testValiderMissionAvecBenevoleSansMetierCorrespondant() {
        System.out.println("Début du test : testValiderMissionAvecBenevoleSansMetierCorrespondant");
        mission = new Mission(MissionEtat.EN_ATTENTE_AFFECTATION, "Ménage", demandeur, Place.HOME);

        benevoles.clear();
        benevoles.add(new Benevole("Charlie", "Johnson", "charlie@example.com", "password789", "Jardinage"));

        validateur.validerMission(mission, benevoles);

        assertEquals(MissionEtat.EN_ATTENTE_AFFECTATION, mission.getEtat(), "La mission ne devrait pas être validée.");
        assertNull(mission.getBenevole(), "Aucun bénévole ne devrait être attribué.");
        System.out.println("Test réussi : aucun bénévole avec le métier correspondant, mission non validée.");
    }
}
