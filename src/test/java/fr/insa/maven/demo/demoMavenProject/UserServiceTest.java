package fr.insa.maven.demo.demoMavenProject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserServiceTest {

    private Connection conn;

    // Informations de connexion à la base de données
    static final String DB_URL = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/projet_gei_012";
    static final String USER = "projet_gei_012";
    static final String PASS = "dith1Que";

    // Méthode exécutée avant chaque test pour initialiser les objets nécessaires
    @BeforeEach
    public void setUp() throws SQLException {
        // Établir la connexion à la base de données
        conn = DriverManager.getConnection(DB_URL, USER, PASS);

        // Nettoyer la table Demandeur avant chaque test
        PreparedStatement clearDemandeurTable = conn.prepareStatement("DELETE FROM Demandeur");
        clearDemandeurTable.executeUpdate();

        // Nettoyer la table Mission avant chaque test
        PreparedStatement clearMissionTable = conn.prepareStatement("DELETE FROM Mission");
        clearMissionTable.executeUpdate();
    }

    // Test pour vérifier l'enregistrement d'un demandeur
    @Test
    public void testRegisterDemandeur() throws SQLException {
        // Créer un nouveau demandeur avec des attributs supplémentaires
        Demandeur demandeur = new Demandeur(
                "Elian",
                "Smith",
                "Besoin d'une assistance médicale quotidienne",
                "Assistance médicale",
                Place.OTHER,
                "elian@gmail.com",
                "5665262"
        );

        // Insérer le demandeur dans la table Demandeur
        PreparedStatement insertDemandeurStmt = conn.prepareStatement(
                "INSERT INTO Demandeur (lastname, firstname, description, needs, location, email, password) VALUES (?, ?, ?, ?, ?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS
        );
        insertDemandeurStmt.setString(1, demandeur.getLastname());
        insertDemandeurStmt.setString(2, demandeur.getFirstname());
        insertDemandeurStmt.setString(3, demandeur.getDescription());
        insertDemandeurStmt.setString(4, demandeur.getNeeds());
        insertDemandeurStmt.setObject(5, demandeur.getLocation().name()); // Utilisation du type enum
        insertDemandeurStmt.setString(6, demandeur.getEmail());
        insertDemandeurStmt.setString(7, demandeur.getPassword());
        insertDemandeurStmt.executeUpdate();

        // Vérifier que le demandeur a bien été enregistré
        PreparedStatement selectStmt = conn.prepareStatement(
                "SELECT lastname, firstname, description, needs, location, email FROM Demandeur WHERE email = ?"
        );
        selectStmt.setString(1, demandeur.getEmail());
        ResultSet rs = selectStmt.executeQuery();

        if (rs.next()) {
            assertEquals("Elian", rs.getString("firstname"));
            assertEquals("Smith", rs.getString("lastname"));
            assertEquals("Besoin d'une assistance médicale quotidienne", rs.getString("description"));
            assertEquals("Assistance médicale", rs.getString("needs"));
            assertEquals(Place.OTHER.name(), rs.getString("location")); // Utilisation de name() pour comparer avec la chaîne
        }
    }
}
