package fr.insa.maven.demo.demoMavenProject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    // Informations de connexion au serveur MySQL
    private static final String SERVER_URL = "jdbc:mysql://localhost:3306/";
    private static final String DATABASE_NAME = "test_db";
    static final String DB_URL = SERVER_URL + DATABASE_NAME;
     static final String USER = "root";
     static final String PASS = "root";

    // Chemin du fichier SQL d'initialisation
    public static final String SQL_FILE_PATH = "src/test/resources/Sql_Test.sql";

    // Méthode pour s'assurer que la base de données existe
    public static void ensureDatabaseExists() {
        try (Connection conn = DriverManager.getConnection(SERVER_URL, USER, PASS);
             var stmt = conn.createStatement()) {
            String createDatabaseQuery = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
            stmt.executeUpdate(createDatabaseQuery);
            System.out.println("Base de données vérifiée ou créée : " + DATABASE_NAME);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la création ou de la vérification de la base de données.");
        }
    }

    // Méthode pour exécuter un fichier SQL via MySQL CLI
    public static void executeSqlFileWithCli(String sqlFilePath) {
        String mysqlPath = "\"C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysql.exe\"";
        String command = String.format("%s -u %s -p%s %s < %s", mysqlPath, USER, PASS, DATABASE_NAME, sqlFilePath);

        try {
            Process process = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", command});

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

    // Méthode pour obtenir une connexion
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }
}
