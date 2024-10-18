package fr.insa.maven.demo.demoMavenProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        // Connexion à la base de données
        String dbUrl = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/projet_gei_012";  // Remplacer par ton URL de base de données
        String user = "projet_gei_012";  // Nom d'utilisateur pour la base de données
        String password = "dith1Que";  // Mot de passe pour la base de données

        try {
            // Établir la connexion
            Connection conn = DriverManager.getConnection(dbUrl, user, password);
            System.out.println("Connexion réussie à la base de données.");

            // Instancier le service utilisateur
            UserService userService = new UserService(conn);

            // Créer un nouvel utilisateur
            User newUser = new User("John", "Doe", "john.doe@example.com", "password123");

            // Enregistrer l'utilisateur dans la base de données
            userService.registerUser(newUser);
            System.out.println("Utilisateur enregistré : " + newUser.getFirstname() + " " + newUser.getLastname());

            // Authentifier l'utilisateur
            User authenticatedUser = userService.authenticateUser("john.doe@example.com", "password123");

            if (authenticatedUser != null) {
                System.out.println("Connexion réussie pour " + authenticatedUser.getFirstname() + " " + authenticatedUser.getLastname());
            } else {
                System.out.println("Échec de la connexion.");
            }

            User updatedUser = new User("John", "Doe", "john.doe@example.com", "newpassword123");
            userService.updateUser(updatedUser);

            userService.deleteUser("john.doe@example.com");


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
