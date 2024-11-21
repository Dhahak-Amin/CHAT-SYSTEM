
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
        // Établir la connexion à la base de données
        conn = DriverManager.getConnection(DB_URL, USER, PASS);

        // Nettoyer les tables Demandeur, Benevole et Mission avant chaque test
        try (PreparedStatement clearMissionTable = conn.prepareStatement("DELETE FROM Mission")) {
            clearMissionTable.executeUpdate();
        }
        try (PreparedStatement clearDemandeurTable = conn.prepareStatement("DELETE FROM Demandeur")) {
            clearDemandeurTable.executeUpdate();
        }
        try (PreparedStatement clearBenevoleTable = conn.prepareStatement("DELETE FROM Benevole")) {
            clearBenevoleTable.executeUpdate();
        }

        // Initialiser le validateur et les entités nécessaires
        validateur = new Validateur("Valentin", "Durand", "valentin123@example.com", "password123", conn);
        demandeur = new Demandeur("amine", "Dupont", "Besoin d'aide pour le ménage", "Ménage", Place.HOME, "alice@example.com", "password123");

        // Insérer le demandeur dans la base de données
        insertDemandeur(demandeur);

        // Initialiser la liste des bénévoles
        benevoles = new ArrayList<>();
        benevole = new Benevole("ELian", "Dupont", "bob@example.com", "password456", "Ménage");
        benevole2 = new Benevole("kyllian", "Mbappe", "kiki@example.com", "password00", "médecin");

        insertBenevole(benevole); // Insérer le bénévole dans la base de données
        insertBenevole(benevole2);
        benevoles.add(benevole);
        benevoles.add(benevole2);

    }

    private void insertDemandeur(Demandeur demandeur) throws SQLException {
        PreparedStatement insertDemandeurStmt = conn.prepareStatement(
                "INSERT INTO Demandeur (lastname, firstname, description, needs, location, email, password) VALUES (?, ?, ?, ?, ?, ?, ?)"
        );
        insertDemandeurStmt.setString(1, demandeur.getLastname());
        insertDemandeurStmt.setString(2, demandeur.getFirstname());
        insertDemandeurStmt.setString(3, demandeur.getDescription());
        insertDemandeurStmt.setString(4, demandeur.getNeeds());
        insertDemandeurStmt.setObject(5, demandeur.getLocation().name());
        insertDemandeurStmt.setString(6, demandeur.getEmail());
        insertDemandeurStmt.setString(7, demandeur.getPassword());
        insertDemandeurStmt.executeUpdate();
    }

    private void insertBenevole(Benevole benevole) throws SQLException {
        PreparedStatement insertBenevoleStmt = conn.prepareStatement(
                "INSERT INTO Benevole (lastname, firstname, email, password, metier) VALUES (?, ?, ?, ?, ?)"
        );
        insertBenevoleStmt.setString(1, benevole.getLastname());
        insertBenevoleStmt.setString(2, benevole.getFirstname());
        insertBenevoleStmt.setString(3, benevole.getEmail());
        insertBenevoleStmt.setString(4, benevole.getPassword());
        insertBenevoleStmt.setString(5, benevole.getMetier());
        insertBenevoleStmt.executeUpdate();
    }
    @Test
    void testValiderMissionAvecBenevoleDisponible() {
        // Création d'une mission avec un besoin spécifique
        mission = new Mission("Ménage", Place.HOME, MissionEtat.EN_ATTENTE_AFFECTATION, demandeur);

        // Ajout d'un bénévole disponible
        benevoles.clear(); // Réinitialiser la liste des bénévoles
        benevoles.add(benevole); // Ajouter un bénévole déjà initialisé et disponible

        // Validation de la mission
        validateur.validerMission(mission, benevoles);

        // Vérifications : La mission doit être validée et attribuée au bénévole
        assertEquals(MissionEtat.VALIDEE, mission.getEtat(), "La mission devrait être validée.");
        assertEquals(benevole, mission.getBenevole(), "Le bénévole devrait être attribué à la mission.");

        // Vérifiez que la mission a été insérée dans la table Mission
        try (PreparedStatement checkMissionStmt = conn.prepareStatement(
                "SELECT * FROM Mission WHERE intitule = ?"
        )) {
            checkMissionStmt.setString(1, mission.getIntitule());
            ResultSet resultSet = checkMissionStmt.executeQuery();
            assertTrue(resultSet.next(), "La mission validée devrait être présente dans la base de données.");
            assertEquals(mission.getEtat().name(), resultSet.getString("etat"), "L'état de la mission doit correspondre.");
            assertEquals(demandeur.getEmail(), resultSet.getString("demandeur_email"), "L'email du demandeur doit correspondre.");
            assertEquals(benevole.getEmail(), resultSet.getString("benevole_email"), "L'email du bénévole doit correspondre.");
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Erreur lors de la vérification de la mission dans la base de données.");
        }
    }


    @Test
    void testValiderMissionSansBenevoleDisponible() {
        mission = new Mission("Ménage", Place.HOME, MissionEtat.EN_ATTENTE_AFFECTATION, demandeur);

        benevoles.clear(); // Aucun bénévole disponible

        validateur.validerMission(mission, benevoles);

        // Vérifiez que la mission reste en attente
        assertEquals(MissionEtat.EN_ATTENTE_AFFECTATION, mission.getEtat(), "La mission ne devrait pas être validée sans bénévole.");
        assertNull(mission.getBenevole(), "Aucun bénévole ne devrait être attribué à la mission.");
    }

    @Test
    void testAjoutValidateurDansBaseDeDonnees() throws SQLException {
        // Insérez le validateur dans la base de données
        PreparedStatement insertValidateurStmt = conn.prepareStatement(
                "INSERT INTO Validateur (lastname, firstname, email, password) VALUES (?, ?, ?, ?)"
        );
        insertValidateurStmt.setString(1, validateur.getLastname());
        insertValidateurStmt.setString(2, validateur.getFirstname());
        insertValidateurStmt.setString(3, validateur.getEmail());
        insertValidateurStmt.setString(4, validateur.getPassword());
        insertValidateurStmt.executeUpdate();

        // Vérifiez si le validateur a bien été inséré
        PreparedStatement checkValidateurStmt = conn.prepareStatement(
                "SELECT * FROM Validateur WHERE email = ?"
        );
        checkValidateurStmt.setString(1, validateur.getEmail());
        ResultSet resultSet = checkValidateurStmt.executeQuery();
        assertTrue(resultSet.next(), "Le validateur devrait être présent dans la base de données.");
        assertEquals(validateur.getLastname(), resultSet.getString("lastname"), "Le nom du validateur doit correspondre.");
        assertEquals(validateur.getFirstname(), resultSet.getString("firstname"), "Le prénom du validateur doit correspondre.");
    }
    @Test
    void testValiderMissionAvecBenevoleSansMetierCorrespondant() {
        // Création d'une mission avec un besoin spécifique
        mission = new Mission("Ménage", Place.HOME, MissionEtat.EN_ATTENTE_AFFECTATION, demandeur);

        // Bénévole avec un métier non correspondant
        Benevole benevoleIncompatible = new Benevole("Charlie", "Johnson", "charlie@example.com", "password789", "Jardinage");
        benevoles.clear(); // Réinitialiser la liste des bénévoles
        benevoles.add(benevoleIncompatible);

        // Validation de la mission avec le bénévole incompatible
        validateur.validerMission(mission, benevoles);

        // Vérifications : La mission ne doit pas être validée
        assertEquals(MissionEtat.EN_ATTENTE_AFFECTATION, mission.getEtat(), "La mission ne devrait pas être validée si le bénévole n'a pas le bon métier.");
        assertNull(mission.getBenevole(), "Aucun bénévole ne devrait être attribué à la mission.");

        // Vérifiez qu'aucune mission n'a été insérée dans la table Mission
        try {
            PreparedStatement checkMissionStmt = conn.prepareStatement(
                    "SELECT * FROM Mission WHERE intitule = ?"
            );
            checkMissionStmt.setString(1, mission.getIntitule());
            ResultSet resultSet = checkMissionStmt.executeQuery();
            assertFalse(resultSet.next(), "La mission ne devrait pas être présente dans la base de données si elle n'est pas validée.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
