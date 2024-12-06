package fr.insa.maven.demo.demoMavenProject;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AvisTest {

    @Test
    void testCreateAvis() {
        // Création d'un bénévole
        Benevole benevole = new Benevole("John", "Doe", "john.doe@example.com", "password123", "Jardinage");

        // Création d'un avis
        Avis avis = new Avis(benevole);
        avis.setComment("Très bon travail !");
        avis.setNote(5);

        // Vérifications
        assertEquals("Très bon travail !", avis.getComment(), "Le commentaire devrait être 'Très bon travail !'");
        assertEquals(5, avis.getNote(), "La note devrait être 5");
        assertEquals("John Doe", benevole.getFirstname() + " " + benevole.getLastname(), "Le nom du bénévole devrait être 'John Doe'");
    }

    @Test
    void testAddMultipleAvisToBenevole() {
        // Création d'un bénévole
        Benevole benevole = new Benevole("Jane", "Smith", "jane.smith@example.com", "password123", "Cuisine");

        // Liste d'avis
        List<Avis> avisList = new ArrayList<>();

        // Ajout d'avis
        Avis avis1 = new Avis(benevole);
        avis1.setComment("Très bonne cuisinière !");
        avis1.setNote(4);

        Avis avis2 = new Avis(benevole);
        avis2.setComment("Travail rapide et efficace.");
        avis2.setNote(5);

        avisList.add(avis1);
        avisList.add(avis2);

        // Vérifications
        assertEquals(2, avisList.size(), "Le bénévole devrait avoir 2 avis.");
        assertEquals("Très bonne cuisinière !", avisList.get(0).getComment(), "Le commentaire du premier avis devrait correspondre.");
        assertEquals(5, avisList.get(1).getNote(), "La note du deuxième avis devrait être 5.");
    }

    @Test
    void testSetInvalidNote() {
        // Création d'un bénévole
        Benevole benevole = new Benevole("Alice", "Brown", "alice.brown@example.com", "password123", "Nettoyage");

        // Création d'un avis
        Avis avis = new Avis(benevole);

        // Tentative de définition d'une note invalide
        assertThrows(IllegalArgumentException.class, () -> avis.setNote(6), "La note doit être comprise entre 0 et 5.");
        assertThrows(IllegalArgumentException.class, () -> avis.setNote(-1), "La note doit être comprise entre 0 et 5.");
    }
}
