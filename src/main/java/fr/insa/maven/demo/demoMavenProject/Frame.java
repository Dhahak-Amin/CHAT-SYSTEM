package fr.insa.maven.demo.demoMavenProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Frame extends JFrame {
    private AllMissions allMissions; // Instance de AllMissions
    private DefaultListModel<Mission> missionListModel; // Modèle de la liste
    private JList<Mission> missionList; // Liste pour afficher les missions
    private JButton addButton; // Bouton pour ajouter une mission

    public Frame(AllMissions allMissions) {
        this.allMissions = allMissions;
        this.missionListModel = new DefaultListModel<>();
        this.missionList = new JList<>(missionListModel);
        this.addButton = new JButton("Ajouter une mission");

        // Configuration de la fenêtre
        setTitle("Missions");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        // Ajouter la liste des missions à un JScrollPane
        JScrollPane scrollPane = new JScrollPane(missionList);
        add(scrollPane, BorderLayout.CENTER);

        // Configuration du bouton d'ajout
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMission();
            }
        });
        add(addButton, BorderLayout.SOUTH);

        // Initialiser l'affichage des missions
        updateMissionList();
    }

    // Méthode pour mettre à jour la liste des missions affichées
    private void updateMissionList() {
        missionListModel.clear(); // Effacer les éléments existants
        for (Mission mission : allMissions.getMissions()) {
            missionListModel.addElement(mission); // Ajouter chaque mission au modèle
        }
    }

    // Méthode pour ajouter une nouvelle mission
    private void addMission() {
        // Obtenir des informations pour créer une nouvelle mission
        String intitule = JOptionPane.showInputDialog(this, "Quel est l'intitulé de la mission?");

        // Pour la démonstration, nous allons utiliser une place fixe
        Place place = Place.HOME; // Ou appeler une méthode pour choisir une place

        // Créer un demandeur pour la mission (vous pouvez demander des détails ici aussi)
        Demandeur demandeur = new Demandeur("Alice", "Dupont", "Besoin d'aide", "Jardin", "Paris", "alice@example.com", "password123");

        if (intitule != null && !intitule.trim().isEmpty()) {
            // Créer la mission
            Mission mission = new Mission("En attente", intitule, demandeur, place);
            allMissions.addMission(mission); // Ajouter la mission à AllMissions
            updateMissionList(); // Mettre à jour l'affichage
        } else {
            JOptionPane.showMessageDialog(this, "Intitulé invalide !", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Création de l'objet AllMissions
        AllMissions allMissions = new AllMissions();

        // Lancer l'application GUI
        SwingUtilities.invokeLater(() -> {
            Frame frame = new Frame(allMissions);
            frame.setVisible(true);
        });
    }
}
