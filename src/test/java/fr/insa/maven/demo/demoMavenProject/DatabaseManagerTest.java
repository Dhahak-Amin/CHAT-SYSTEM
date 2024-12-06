package fr.insa.maven.demo.demoMavenProject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseManagerTest {

    @Test
    void testExecuteSqlFileWithCli() {
        assertDoesNotThrow(() -> {
            DatabaseManager.ensureDatabaseExists(); // Vérifie que la base existe
            DatabaseManager.executeSqlFileWithCli(DatabaseManager.SQL_FILE_PATH); // Exécute le fichier SQL via CLI
        }, "Erreur lors de l'exécution du fichier SQL via MySQL CLI !");
    }

    @Test
    void testTableExistenceAfterCliExecution() {
        assertDoesNotThrow(() -> {
            try (var conn = DatabaseManager.getConnection();
                 var stmt = conn.createStatement()) {

                String[] tableNames = {"User", "Demandeur", "Benevole", "Validateur", "Mission", "Avis"};
                for (String tableName : tableNames) {
                    String query = "SHOW TABLES LIKE '" + tableName + "'";
                    try (var rs = stmt.executeQuery(query)) {
                        assertTrue(rs.next(), "La table " + tableName + " n'existe pas !");
                      //  System.out.println("Table vérifiée : " + tableName);
                    }
                }
            }
        }, "Erreur lors de la vérification des tables après exécution du fichier SQL via CLI !");
    }
}
