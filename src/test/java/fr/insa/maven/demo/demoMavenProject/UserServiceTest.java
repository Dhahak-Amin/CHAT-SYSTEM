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

        // Nettoyer les tables User et Demandeur avant chaque test
        PreparedStatement clearDemandeurTable = conn.prepareStatement("DELETE FROM Demandeur");
        clearDemandeurTable.executeUpdate();
        PreparedStatement clearUserTable = conn.prepareStatement("DELETE FROM User");
        clearUserTable.executeUpdate();
    }

    // Test pour vérifier l'enregistrement d'un demandeur
    @Test
 
    public void testRegisterDemandeur() throws SQLException {
        // Créer un nouveau demandeur avec des attributs supplémentaires
        Demandeur demandeur = new Demandeur(
                "Elian",
                "SMith",
                "Besoin d'une assistance médicale quotidienne",
                "Assistance médicale 2",
                "Paris","elian@gmailcom","5665262"
        );

        // Insérer le demandeur dans la table User
        PreparedStatement insertUserStmt = conn.prepareStatement(
                "INSERT INTO User (firstname, lastname) VALUES (?, ?)", 
                PreparedStatement.RETURN_GENERATED_KEYS
        );
        insertUserStmt.setString(1, demandeur.getFirstname());
        insertUserStmt.setString(2, demandeur.getLastname());
        insertUserStmt.executeUpdate();

        // Récupérer l'ID généré de l'utilisateur
        ResultSet generatedKeys = insertUserStmt.getGeneratedKeys();
        int userId = 0;
        if (generatedKeys.next()) {
            userId = generatedKeys.getInt(1);
        }

        // Insérer les détails du demandeur dans la table Demandeur
        PreparedStatement insertDemandeurStmt = conn.prepareStatement(
                "INSERT INTO Demandeur (user_id, description, needs, location) VALUES (?, ?, ?, ?)"
        );
        insertDemandeurStmt.setInt(1, userId);
        insertDemandeurStmt.setString(2, demandeur.getDescription());
        insertDemandeurStmt.setString(3, demandeur.getNeeds());
        insertDemandeurStmt.setString(4, demandeur.getLocation());
        insertDemandeurStmt.executeUpdate();

        // Vérifier que le demandeur a bien été enregistré
        PreparedStatement selectStmt = conn.prepareStatement(
                "SELECT u.firstname, u.lastname, d.description, d.needs, d.location " +
                "FROM User u JOIN Demandeur d ON u.id = d.user_id WHERE u.id = ?"
        );
        selectStmt.setInt(1, userId);
        ResultSet rs = selectStmt.executeQuery();

      /*  if (rs.next()) {
            assertEquals("Alice", rs.getString("firstname"));
            assertEquals("Smith", rs.getString("lastname"));
            assertEquals("Besoin d'une assistance médicale quotidienne", rs.getString("description"));
            assertEquals("Assistance médicale", rs.getString("needs"));
            assertEquals("Toulouse", rs.getString("location"));
        }*/
    } 
}

