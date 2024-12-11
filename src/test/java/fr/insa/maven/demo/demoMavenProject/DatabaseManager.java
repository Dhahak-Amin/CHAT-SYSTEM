package fr.insa.maven.demo.demoMavenProject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

/**
 * Classe utilitaire pour la gestion de la base de données.
 * Fournit des méthodes pour vérifier, nettoyer, et exécuter des commandes SQL sur la base de données.
 */
public class DatabaseManager {

    // Informations de connexion au serveur MySQL
    private static final String SERVER_URL = "jdbc:mysql://localhost:3306/";
    private static final String DATABASE_NAME = "test_db"; // Nom de la base de données utilisée pour les tests
    public static final String DB_URL = SERVER_URL + DATABASE_NAME;
    public static final String USER = "root"; // Nom d'utilisateur MySQL
    public static final String PASS = "root"; // Mot de passe MySQL

    // Chemin du fichier SQL d'initialisation
    public static final String SQL_FILE_PATH = "src/test/resources/Sql_Test.sql";

    /**
     * Vérifie que la base de données spécifiée existe, et la crée si nécessaire.
     * En cas d'erreur, une exception Runtime est levée.
     */
    public static void ensureDatabaseExists() {
        try (Connection conn = DriverManager.getConnection(SERVER_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            String createDatabaseQuery = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
            stmt.executeUpdate(createDatabaseQuery);
            System.out.println("Base de données vérifiée ou créée : " + DATABASE_NAME);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la création ou de la vérification de la base de données.");
        }
    }

    /**
     * Exécute un fichier SQL en utilisant la CLI MySQL.
     * Cette méthode gère différents systèmes d'exploitation (Windows, Linux, MacOS).
     *
     * @param sqlFilePath Chemin du fichier SQL à exécuter.
     */
    public static void executeSqlFileWithCli(String sqlFilePath) {
        String osName = System.getProperty("os.name").toLowerCase();
        String[] command;

        if (osName.contains("win")) {
            // Commande pour Windows
            command = new String[]{"cmd.exe", "/c", "\"C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysql.exe\" -u " + USER + " -p" + PASS + " " + DATABASE_NAME + " < " + sqlFilePath};
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("mac")) {
            // Commande pour Linux/MacOS
            command = new String[]{"/bin/sh", "-c", "mysql -u " + USER + " -p" + PASS + " " + DATABASE_NAME + " < " + sqlFilePath};
        } else {
            throw new RuntimeException("Système d'exploitation non supporté : " + osName);
        }

        try {
            Process process = Runtime.getRuntime().exec(command);

            // Capture et affiche la sortie standard et les erreurs de la CLI
            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                 BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

                String line;
                System.out.println("Sortie MySQL :");
                while ((line = outputReader.readLine()) != null) {
                    System.out.println(line);
                }

                System.out.println("Erreurs MySQL :");
                while ((line = errorReader.readLine()) != null) {
                    System.err.println(line);
                }
            }

            // Vérifie le code de sortie pour détecter des erreurs
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Fichier SQL exécuté avec succès via MySQL CLI.");
            } else {
                throw new RuntimeException("Erreur lors de l'exécution du fichier SQL via MySQL CLI. Code de sortie : " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'exécution du fichier SQL via MySQL CLI.");
        }
    }

    /**
     * Retourne une connexion à la base de données spécifiée.
     *
     * @return Connexion SQL active.
     * @throws SQLException Si la connexion échoue.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    /**
     * Vérifie si une table existe dans la base de données.
     *
     * @param tableName Nom de la table à vérifier.
     * @return True si la table existe, sinon False.
     */
    public static boolean checkTableExists(String tableName) {
        String checkTableQuery = "SHOW TABLES LIKE ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(checkTableQuery)) {
            stmt.setString(1, tableName);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Retourne vrai si une correspondance est trouvée
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Nettoie la base de données en supprimant toutes les données des tables spécifiées.
     * Cette méthode est utilisée principalement pour les tests.
     */
    public static void cleanDatabase() {
        String[] cleanSQL = {
                "DELETE FROM avis",
                "DELETE FROM mission",
                "DELETE FROM validateur",
                "DELETE FROM benevole",
                "DELETE FROM demandeur",
                "DELETE FROM user"
        };

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            // Exécute chaque commande DELETE pour vider les tables
            for (String query : cleanSQL) {
                stmt.executeUpdate(query);
            }
            System.out.println("Base de données nettoyée.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
