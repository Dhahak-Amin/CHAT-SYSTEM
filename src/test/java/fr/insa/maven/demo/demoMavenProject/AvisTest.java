package fr.insa.maven.demo.demoMavenProject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de tests pour la classe Avis.
 * Cette classe vérifie le bon fonctionnement de la gestion des avis pour les bénévoles.
 */
public class AvisTest {

    private Connection conn;

    /**
     * Initialisation avant chaque test.
     * Vérifie que la base de données est prête et initialise la connexion.
     */
    @BeforeEach
    public void setUp() {
        try {
            // Vérifie que la base de données existe ou la crée si nécessaire
            DatabaseManager.ensureDatabaseExists();

            // Exécute le script SQL d'initialisation
            DatabaseManager.executeSqlFileWithCli(DatabaseManager.SQL_FILE_PATH);

            // Initialise la connexion à la base de données
            conn = DatabaseManager.getConnection();
        } catch (SQLException e) {
            // Lance une exception si l'initialisation échoue
            throw new RuntimeException("Erreur lors de l'initialisation de la base de données : " + e.getMessage(), e);
        }
    }

    /**
     * Teste la création d'un avis pour un bénévole.
     * Vérifie que les données de l'avis sont correctement définies.
     */
    @Test
    void testCreateAvis() {
        // Création d'un bénévole fictif
        Benevole benevole = new Benevole("John", "Doe", "john.doe@example.com", "password123", "Jardinage");

        // Création et configuration d'un avis
        Avis avis = new Avis(benevole);
        avis.setComment("Très bon travail !");
        avis.setNote(5);

        // Vérifie que les données sont correctement définies
        assertEquals("Très bon travail !", avis.getComment(), "Le commentaire devrait être 'Très bon travail !'.");
        assertEquals(5, avis.getNote(), "La note devrait être 5.");
        assertEquals("John Doe", benevole.getFirstname() + " " + benevole.getLastname(), "Le nom complet du bénévole devrait être 'John Doe'.");
    }

    /**
     * Teste l'ajout de plusieurs avis pour un bénévole.
     * Vérifie que la liste des avis est correctement gérée.
     */
    @Test
    void testAddMultipleAvisToBenevole() {
        // Création d'un bénévole fictif
        Benevole benevole = new Benevole("Jane", "Smith", "jane.smith@example.com", "password123", "Cuisine");

        // Création d'une liste d'avis
        List<Avis> avisList = new ArrayList<>();

        // Ajout de deux avis à la liste
        Avis avis1 = new Avis(benevole);
        avis1.setComment("Très bonne cuisinière !");
        avis1.setNote(4);

        Avis avis2 = new Avis(benevole);
        avis2.setComment("Travail rapide et efficace.");
        avis2.setNote(5);

        avisList.add(avis1);
        avisList.add(avis2);

        // Vérifie que les avis ont été correctement ajoutés
        assertEquals(2, avisList.size(), "Le bénévole devrait avoir 2 avis.");
        assertEquals("Très bonne cuisinière !", avisList.get(0).getComment(), "Le commentaire du premier avis devrait correspondre.");
        assertEquals(5, avisList.get(1).getNote(), "La note du deuxième avis devrait être 5.");
    }

    /**
     * Teste la validation des notes attribuées à un avis.
     * Vérifie qu'une exception est levée pour une note invalide.
     */
    @Test
    void testSetInvalidNote() {
        // Création d'un bénévole fictif
        Benevole benevole = new Benevole("Alice", "Brown", "alice.brown@example.com", "password123", "Nettoyage");

        // Création d'un avis associé au bénévole
        Avis avis = new Avis(benevole);

        // Vérifie qu'une exception est levée pour des notes en dehors de l'intervalle autorisé (0-5)
        assertThrows(IllegalArgumentException.class, () -> avis.setNote(6), "La note doit être comprise entre 0 et 5.");
        assertThrows(IllegalArgumentException.class, () -> avis.setNote(-1), "La note doit être comprise entre 0 et 5.");
    }
}
