

import fr.insa.maven.demo.demoMavenProject.*;
import fr.insa.maven.demo.demoMavenProject.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserServiceTest {

    private UserService userService;

    // Méthode exécutée avant chaque test pour initialiser les objets nécessaires
    @BeforeEach
    public void setUp() {
        userService = new UserService();
    }

    // Un test simple pour vérifier l'enregistrement d'un utilisateur bénévole
   /*  @Test
    public void testRegisterUser() {
        // Création d'un nouvel utilisateur bénévole
        Benevole benevole = new Benevole("John", "Jacque");

        // Enregistrement de l'utilisateur via le service
        userService.registerUser(benevole);

        // Vérification que la taille de la liste des utilisateurs est 1
        assertEquals(1, userService.getAllUsers().size());

        // Vérification que le prénom de l'utilisateur est bien "John"
        assertEquals("John", userService.getAllUsers().get(0).getFirstname());
    }
    */
    
    
 // Test pour vérifier l'enregistrement d'un demandeur
    @Test
    public void testRegisterDemandeur() {
        // Création d'un nouveau demandeur avec des attributs supplémentaires
        Demandeur demandeur = new Demandeur(
            "Alice", 
            "Smith", 
            "Besoin d'une assistance médicale quotidienne", 
            "Assistance médicale", 
            "Toulouse"
        );

        userService.registerUser(demandeur);

        assertEquals(1, userService.getAllUsers().size());
        assertEquals("Alice", userService.getAllUsers().get(0).getFirstname());
        assertEquals("Smith", userService.getAllUsers().get(0).getLastname());
        assertEquals("Besoin d'une assistance médicale quotidienne", ((Demandeur) userService.getAllUsers().get(0)).getDescription());
        assertEquals("Assistance médicale", ((Demandeur) userService.getAllUsers().get(0)).getNeeds());
        assertEquals("Toulouse", ((Demandeur) userService.getAllUsers().get(0)).getLocation());
    }
    
    
}
