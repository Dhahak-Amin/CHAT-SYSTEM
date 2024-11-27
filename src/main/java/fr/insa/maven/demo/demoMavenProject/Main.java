package fr.insa.maven.demo.demoMavenProject;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        ConnectionManager connectionManager = new ConnectionManager();

        // Sélection du rôle utilisateur
        String[] roles = {"Bénévole", "Demandeur", "Validateur", "Administrateur"};
        String selectedRole = (String) JOptionPane.showInputDialog(
                null,
                "Choisissez votre rôle :",
                "Sélection de rôle",
                JOptionPane.QUESTION_MESSAGE,
                null,
                roles,
                roles[0]
        );

        if (selectedRole == null) {
            JOptionPane.showMessageDialog(null, "Programme terminé.", "Au revoir", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            // Authentification ou enregistrement selon le rôle
            String email = JOptionPane.showInputDialog("Entrez votre email :");
            String password = JOptionPane.showInputDialog("Entrez votre mot de passe :");

            switch (selectedRole) {
                case "Bénévole":
                    connectionManager.authenticateOrRegisterUser(email, password, "benevole");
                    // L'interface FrameBenevole est déjà gérée dans ConnectionManager
                    break;

                case "Demandeur":
                    connectionManager.authenticateOrRegisterUser(email, password, "demandeur");
                    // L'interface FrameDemandeur est déjà gérée dans ConnectionManager
                    break;

                case "Validateur":
                    connectionManager.authenticateOrRegisterUser(email, password, "validateur");
                    JOptionPane.showMessageDialog(null, "Bienvenue, Validateur ! Fonctionnalité bientôt disponible.", "Info", JOptionPane.INFORMATION_MESSAGE);
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

    private static void launchAdminFrame(ConnectionManager connectionManager) {
        String[] actions = {"Gérer les utilisateurs", "Gérer les missions", "Exporter des données", "Se déconnecter"};
        while (true) {
            String choice = (String) JOptionPane.showInputDialog(
                    null,
                    "Choisissez une action :",
                    "Menu Administrateur",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    actions,
                    actions[0]
            );

            if (choice == null || choice.equals("Se déconnecter")) {
                JOptionPane.showMessageDialog(null, "Déconnexion réussie. Merci !", "Au revoir", JOptionPane.INFORMATION_MESSAGE);
                break;
            }

            switch (choice) {
                case "Gérer les utilisateurs":
                    JOptionPane.showMessageDialog(null, "Gestion des utilisateurs en cours...", "Info", JOptionPane.INFORMATION_MESSAGE);
                    // Ajouter le code pour gérer les utilisateurs
                    break;

                case "Gérer les missions":
                    JOptionPane.showMessageDialog(null, "Gestion des missions en cours...", "Info", JOptionPane.INFORMATION_MESSAGE);
                    // Ajouter le code pour gérer les missions
                    break;

                case "Exporter des données":
                    String tableName = JOptionPane.showInputDialog("Nom de la table à exporter :");
                    String filePath = JOptionPane.showInputDialog("Chemin du fichier CSV :");
                    try {
                        connectionManager.exportToCSV(tableName, filePath);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Erreur lors de l'exportation : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                default:
                    JOptionPane.showMessageDialog(null, "Option inconnue.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        }
    }
}
