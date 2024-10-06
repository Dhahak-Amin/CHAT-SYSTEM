import fr.insa.maven.demo.demoMavenProject.UserService;  
import fr.insa.maven.demo.demoMavenProject.Demandeur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserServiceTest {

    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserService();  
    }

    @Test
    public void testRegisterDemandeur() {
        // Création d'un nouvel utilisateur demandeur
        Demandeur demandeur = new Demandeur("Alice", "Smith", "Besoin d'assistance", "Assistance médicale", "Toulouse");

        // Enregistrement de l'utilisateur via le service utilisateur
        userService.registerUser(demandeur);

        // Vérification que l'utilisateur a bien été enregistré
        assertEquals(1, userService.getAllUsers().size());
        assertEquals("Alice", userService.getAllUsers().get(0).getFirstname());
        assertEquals("Smith", userService.getAllUsers().get(0).getLastname());
    }
}
