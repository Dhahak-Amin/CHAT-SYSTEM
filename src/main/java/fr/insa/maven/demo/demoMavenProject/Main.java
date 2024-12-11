package fr.insa.maven.demo.demoMavenProject;

import javax.swing.*;
import static javax.swing.JOptionPane.showInputDialog;

/**
 * Classe principale pour exécuter le système.
 * Gère l'authentification, l'enregistrement et les actions selon le rôle utilisateur.
 */
public class Main {

    public static void main(String[] args) {
        // Initialisation de l'instance de ConnectionManager
        ConnectionManager connectionManager = ConnectionManager.getInstance();

        // Charger les missions depuis la base de données
        AllMissions.getInstance().loadMissionsFromDatabase();

        // Affichage du menu principal pour choisir un rôle utilisateur
        String[] roles = {"Bénévole", "Demandeur", "Validateur", "Administrateur", "Quitter"};
        String selectedRole = (String) showInputDialog(
                null,
                "Choisissez votre rôle :",
                "Sélection de rôle",
                JOptionPane.QUESTION_MESSAGE,
                null,
                roles,
                roles[0]
        );

        // Vérification si l'utilisateur souhaite quitter
        if (selectedRole == null || selectedRole.equals("Quitter")) {
            JOptionPane.showMessageDialog(null, "Programme terminé. Au revoir !", "Au revoir", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }

        try {
            // Authentification ou enregistrement en fonction du rôle choisi
            String email = showInputDialog("Entrez votre email :");
            String password = showInputDialog("Entrez votre mot de passe :");

            switch (selectedRole) {
                case "Bénévole":
                    connectionManager.authenticateOrRegisterUser(email, password, "benevole");
                    break;

                case "Demandeur":
                    connectionManager.authenticateOrRegisterUser(email, password, "demandeur");
                    break;

                case "Validateur":
                    connectionManager.authenticateOrRegisterUser(email, password, "validateur");
                    break;

                case "Administrateur":
                    launchAdminFrame(connectionManager);
                    break;

                default:
                    JOptionPane.showMessageDialog(null, "Rôle inconnu.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Méthode pour gérer le rôle administrateur.
     * Permet de gérer les utilisateurs, les missions et l'exportation des données.
     *
     * @param connectionManager Instance de ConnectionManager pour interagir avec la base de données.
     */
    private static void launchAdminFrame(ConnectionManager connectionManager) {
        boolean keepAdminRunning = true;

        while (keepAdminRunning) {
            // Affichage du menu administrateur
            String[] actions = {"Gérer les utilisateurs", "Gérer les missions", "Exporter des données", "Retour"};
            String choice = (String) showInputDialog(
                    null,
                    "Choisissez une action :",
                    "Menu Administrateur",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    actions,
                    actions[0]
            );

            // Gestion du retour au menu principal
            if (choice == null || choice.equals("Retour")) {
                JOptionPane.showMessageDialog(null, "Retour au menu principal.", "Info", JOptionPane.INFORMATION_MESSAGE);
                keepAdminRunning = false;
                continue;
            }

            try {
                // Actions possibles pour l'administrateur
                switch (choice) {
                    case "Gérer les utilisateurs":
                        JOptionPane.showMessageDialog(null, "Fonctionnalité à implémenter : Gérer les utilisateurs.", "Info", JOptionPane.INFORMATION_MESSAGE);
                        break;

                    case "Gérer les missions":
                        JOptionPane.showMessageDialog(null, "Fonctionnalité à implémenter : Gérer les missions.", "Info", JOptionPane.INFORMATION_MESSAGE);
                        break;

                    case "Exporter des données":
                        handleDataExport(connectionManager);
                        break;

                    default:
                        JOptionPane.showMessageDialog(null, "Option inconnue. Veuillez réessayer.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Une erreur s'est produite : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    /**
     * Méthode pour gérer l'exportation des données.
     * Permet à l'administrateur de sélectionner une table et un chemin pour l'exportation en CSV.
     *
     * @param connectionManager Instance de ConnectionManager pour l'accès à la base de données.
     */
    private static void handleDataExport(ConnectionManager connectionManager) {
        try {
            String tableName = showInputDialog("Nom de la table à exporter :", "Exportation");
            if (tableName == null || tableName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nom de table invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String filePath = showInputDialog("Chemin du fichier CSV :", "Exportation");
            if (filePath == null || filePath.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Chemin de fichier invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            connectionManager.exportToCSV(tableName, filePath);
            JOptionPane.showMessageDialog(null, "Exportation réussie vers : " + filePath, "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de l'exportation : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
