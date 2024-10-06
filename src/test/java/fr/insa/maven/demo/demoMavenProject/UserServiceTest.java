import fr.insa.maven.demo.demoMavenProject.UserService;  
import fr.insa.maven.demo.demoMavenProject.Demandeur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserService();  // Vérifie que cette ligne est bien appelée
    }

    @Test
    public void testRegisterDemandeur() {
        // Création d'un nouvel utilisateur demandeur
        Demandeur demandeur = new Demandeur("Alice", "Smith", "Assistance", "Medical", "Toulouse");

        // Enregistrement de l'utilisateur via le service utilisateur
        userService.registerUser(demandeur);

        // Vérification que l'utilisateur a bien été enregistré
        assertEquals(1, userService.getAllUsers().size(), "La taille de la liste des utilisateurs doit être 1");

        // Vérification des attributs principaux
        Demandeur utilisateurEnregistre = (Demandeur) userService.getAllUsers().get(0);
        assertEquals("Alice", utilisateurEnregistre.getFirstname(), "Le prénom doit être 'Alice'");
        assertEquals("Smith", utilisateurEnregistre.getLastname(), "Le nom de famille doit être 'Smith'");
    }
}
