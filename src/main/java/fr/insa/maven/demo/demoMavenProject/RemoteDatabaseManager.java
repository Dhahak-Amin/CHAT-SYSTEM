package fr.insa.maven.demo.demoMavenProject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

/**
 * Classe RemoteDatabaseManager pour gérer la base de données distante.
 * Cette classe utilise le pattern Singleton pour garantir une seule instance active
 * et fournit des méthodes utilitaires pour gérer la connexion, les tables, et l'exécution de fichiers SQL.
 */
public class RemoteDatabaseManager {

    // Informations de connexion au serveur MySQL distant
    private static final String SERVER_URL = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/";
    private static final String DATABASE_NAME = "projet_gei_012";
    public static final String DB_URL = SERVER_URL + DATABASE_NAME;
    public static final String USER = "projet_gei_012";
    public static final String PASS = "dith1Que";

    // Instance unique (Singleton)
    private static RemoteDatabaseManager instance;

    // Chemin du fichier SQL d'initialisation
    public static final String SQL_FILE_PATH = "src/main/resources/remote_init.sql";

    /**
     * Vérifie que la base de données existe, sinon la crée.
     */
    public static void ensureDatabaseExists() {
        try (Connection conn = DriverManager.getConnection(SERVER_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            String createDatabaseQuery = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
            stmt.executeUpdate(createDatabaseQuery);
            System.out.println("Base de données vérifiée ou créée : " + DATABASE_NAME);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la vérification ou création de la base distante.");
        }
    }

    /**
     * Retourne l'instance unique de RemoteDatabaseManager.
     * @return L'instance Singleton.
     */
    public static RemoteDatabaseManager getInstance() {
        if (instance == null) {
            synchronized (RemoteDatabaseManager.class) {
                if (instance == null) {
                    instance = new RemoteDatabaseManager();
                }
            }
        }
        return instance;
    }

    /**
     * Exécute un fichier SQL via l'interface de ligne de commande MySQL (CLI).
     * La commande est adaptée en fonction du système d'exploitation.
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
     * Retourne une connexion à la base de données distante.
     * @return Connexion SQL à la base distante.
     * @throws SQLException En cas d'erreur de connexion.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    /**
     * Vérifie si une table existe dans la base de données.
     * @param tableName Nom de la table à vérifier.
     * @return true si la table existe, false sinon.
     */
    public static boolean checkTableExists(String tableName) {
        String checkTableQuery = "SHOW TABLES LIKE ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(checkTableQuery)) {
            stmt.setString(1, tableName);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Si une table correspond au nom, on retourne true
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Nettoie toutes les données des tables dans la base de données distante.
     */
    public static void cleanDatabase() {
        String[] cleanSQL = {
                "DELETE FROM Avis",
                "DELETE FROM Mission",
                "DELETE FROM Validateur",
                "DELETE FROM Benevole",
                "DELETE FROM Demandeur",
                "DELETE FROM User"
        };

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            // Exécuter chaque requête DELETE séparément
            for (String query : cleanSQL) {
                stmt.executeUpdate(query);  // Exécuter chaque requête pour nettoyer la base de données
            }
            System.out.println("Base de données distante nettoyée.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
