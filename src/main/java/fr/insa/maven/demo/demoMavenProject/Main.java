package fr.insa.maven.demo.demoMavenProject;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        ConnectionManager connectionManager = new ConnectionManager();

        // Charger les missions depuis la base de données
        AllMissions.getInstance().loadMissionsFromDatabase();

        boolean keepRunning = true;

        while (keepRunning) {
            // Menu principal
            String[] roles = {"Bénévole", "Demandeur", "Validateur", "Administrateur", "Quitter"};
            String selectedRole = (String) JOptionPane.showInputDialog(
                    null,
                    "Choisissez votre rôle ou quittez :",
                    "Menu principal",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    roles,
                    roles[0]
            );

            if (selectedRole == null || selectedRole.equals("Quitter")) {
                // Si l'utilisateur quitte, affiche un message d'au revoir et termine la boucle
                JOptionPane.showMessageDialog(null, "Programme terminé. Au revoir !", "Au revoir", JOptionPane.INFORMATION_MESSAGE);
                keepRunning = false;
                break;
            }

            // Gestion des rôles
            try {
                String email = showInputDialog("Entrez votre email :", "Authentification");
                if (email == null) {
                    // Retour au menu principal si "Annuler"
                    continue;
                }

                String password = showInputDialog("Entrez votre mot de passe :", "Authentification");
                if (password == null) {
                    // Retour au menu principal si "Annuler"
                    continue;
                }

                switch (selectedRole) {
                    case "Bénévole":
                        connectionManager.authenticateOrRegisterUser(email, password, "benevole");
                        JOptionPane.showMessageDialog(null, "Session bénévole terminée.", "Info", JOptionPane.INFORMATION_MESSAGE);
                        break;

                    case "Demandeur":
                        connectionManager.authenticateOrRegisterUser(email, password, "demandeur");
                        JOptionPane.showMessageDialog(null, "Session demandeur terminée.", "Info", JOptionPane.INFORMATION_MESSAGE);
                        break;

                    case "Validateur":
                        connectionManager.authenticateOrRegisterUser(email, password, "validateur");
                        JOptionPane.showMessageDialog(null, "Session validateur terminée.", "Info", JOptionPane.INFORMATION_MESSAGE);
                        break;

                    case "Administrateur":
                        launchAdminFrame(connectionManager);
                        break;

                    default:
                        JOptionPane.showMessageDialog(null, "Rôle inconnu. Veuillez réessayer.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                // Gestion des erreurs générales
                JOptionPane.showMessageDialog(null, "Une erreur s'est produite : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    // Méthode utilitaire pour afficher un dialogue avec gestion de l'annulation
    private static String showInputDialog(String message, String title) {
        String input = JOptionPane.showInputDialog(null, message, title, JOptionPane.QUESTION_MESSAGE);
        return input; // Retourne null si l'utilisateur annule
    }

    // Gestion du rôle administrateur
    private static void launchAdminFrame(ConnectionManager connectionManager) {
        boolean keepAdminRunning = true;

        while (keepAdminRunning) {
            String[] actions = {"Gérer les utilisateurs", "Gérer les missions", "Exporter des données", "Retour"};
            String choice = (String) JOptionPane.showInputDialog(
                    null,
                    "Choisissez une action :",
                    "Menu Administrateur",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    actions,
                    actions[0]
            );

            if (choice == null || choice.equals("Retour")) {
                JOptionPane.showMessageDialog(null, "Retour au menu principal.", "Info", JOptionPane.INFORMATION_MESSAGE);
                keepAdminRunning = false;
                continue;
            }

            try {
                switch (choice) {
                    case "Gérer les utilisateurs":
                        JOptionPane.showMessageDialog(null, "Gestion des utilisateurs en cours...", "Info", JOptionPane.INFORMATION_MESSAGE);
                        break;

                    case "Gérer les missions":
                        JOptionPane.showMessageDialog(null, "Gestion des missions en cours...", "Info", JOptionPane.INFORMATION_MESSAGE);
                        break;

                    case "Exporter des données":
                        String tableName = showInputDialog("Nom de la table à exporter :", "Exportation");
                        if (tableName == null) {
                            JOptionPane.showMessageDialog(null, "Export annulé.", "Info", JOptionPane.INFORMATION_MESSAGE);
                            continue;
                        }

                        String filePath = showInputDialog("Chemin du fichier CSV :", "Exportation");
                        if (filePath == null) {
                            JOptionPane.showMessageDialog(null, "Export annulé.", "Info", JOptionPane.INFORMATION_MESSAGE);
                            continue;
                        }

                        connectionManager.exportToCSV(tableName, filePath);
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
}
