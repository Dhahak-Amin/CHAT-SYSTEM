package fr.insa.maven.demo.demoMavenProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class FrameDemandeur extends JFrame {
    private  AllMissions allMissions; // Instance de AllMissions
    private Demandeur demandeur;
    private DefaultListModel<Mission> missionListModel; // Modèle de la liste
    private JList<Mission> missionList; // Liste pour afficher les missions
    private JButton addButton; // Bouton pour ajouter une mission
    private JButton deleteButton; // Bouton pour supprimer une mission

    public FrameDemandeur( Demandeur demandeur) {

        this.demandeur = demandeur;
        this.allMissions = AllMissions.getInstance();
        this.missionListModel = new DefaultListModel<>();
        this.missionList = new JList<>(missionListModel);
        this.addButton = new JButton("Ajouter une mission");
        this.deleteButton = new JButton("Supprimer une mission");

        // Configuration de la fenêtre
        setTitle("Missions de " + demandeur.getFirstname() + " " + demandeur.getLastname());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLayout(new BorderLayout());

        // Configurer le renderer de la liste pour afficher la mission
        missionList.setCellRenderer(new MissionCellRenderer());
        missionList.setFixedCellHeight(50);

        // Ajouter la liste des missions à un JScrollPane
        JScrollPane scrollPane = new JScrollPane(missionList);
        add(scrollPane, BorderLayout.CENTER);

        // Panel pour les boutons en bas
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Configuration du bouton d'ajout
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMission();
            }
        });

        // Configuration du bouton de suppression
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteMission();
            }
        });

        // Initialiser l'affichage des missions
        updateMissionList();
    }

    // Méthode pour mettre à jour la liste des missions affichées
    private void updateMissionList() {
        missionListModel.clear(); // Effacer les éléments existants
        for (Mission mission : allMissions.getMissions()) {
            if (mission.getDemandeur().equals(this.demandeur)){
            missionListModel.addElement(mission); // Ajouter chaque mission au modèle
        }}
    }
    // Ajout dans la classe FrameDemandeur
    public DefaultListModel<Mission> getMissionListModel() {
        return missionListModel;
    }

    // Méthode pour ajouter une nouvelle mission
    private void addMission() {
        String intitule = JOptionPane.showInputDialog(this, "Quel est l'intitulé de la mission?");
        if (intitule != null && !intitule.trim().isEmpty()) {
            Place[] places = Place.values();
            Place selectedPlace = (Place) JOptionPane.showInputDialog(
                    this,
                    "Choisissez un lieu pour la mission :",
                    "Sélection de l'emplacement",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    places,
                    places[0]
            );

            if (selectedPlace != null) {
                Mission mission = demandeur.createMission(intitule, selectedPlace);
                allMissions.addMission(mission);
                allMissions.enregistrerMission2(mission);
                updateMissionList();
            } else {
                JOptionPane.showMessageDialog(this, "Sélection de l'emplacement annulée.", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Intitulé invalide !", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Méthode pour supprimer une mission en fonction du numéro entré
    private void deleteMission() {
        String input = JOptionPane.showInputDialog(this, "Entrez le numéro de la mission à supprimer:");
        if (input != null && !input.trim().isEmpty()) {
            try {
                int missionIndex = Integer.parseInt(input) - 1; // 1 = première mission
                if (missionIndex >= 0 && missionIndex < missionListModel.size()) {
                    Mission mission = missionListModel.get(missionIndex);
                    allMissions.removeMission(mission); // Supprime la mission de AllMissions
                    updateMissionList();
                } else {
                    JOptionPane.showMessageDialog(this, "Numéro de mission invalide !", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Veuillez entrer un nombre valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Renderer personnalisé pour afficher chaque mission sans bouton de suppression
    private class MissionCellRenderer extends JPanel implements ListCellRenderer<Mission> {
        private JLabel missionLabel;

        public MissionCellRenderer() {
            setLayout(new BorderLayout());
            missionLabel = new JLabel();
            add(missionLabel, BorderLayout.CENTER);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Mission> list, Mission mission, int index, boolean isSelected, boolean cellHasFocus) {
            Demandeur demandeur = mission.getDemandeur();
            String missionDetails = (index + 1) + ". " + mission.getIntitule() + " - " + mission.getPlace().name()
                    + " - " + demandeur.getFirstname() + " " + demandeur.getLastname()
                    + " - État : " + mission.getEtat();
            missionLabel.setText(missionDetails);

            // Appliquer une couleur de fond si sélectionnée
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            return this;
        }
    }


    public static void main(String[] args) {

        AllMissions allMissions = AllMissions.getInstance();

        Demandeur demandeur = new Demandeur("Alice", "Dupont", "Besoin d'aide", "Jardin", Place.HOME, "alice@example.com", "password123");
        Demandeur demandeur2 = new Demandeur("Alieece", "Deeupont", "Besoin dee'aide", "Jareedin", Place.HOSPITAL, "alice@exeample.com", "passweeord123");

        Mission mission1 = new Mission("jardin",  demandeur, Place.HOME);
        Mission mission2 = new Mission("piscine",  demandeur2, Place.WORKPLACE);
        Mission mission3 = new Mission("canapé",  demandeur, Place.HOME);
        Mission mission4 = new Mission("bobo",  demandeur2, Place.HOSPITAL);
        Mission mission5 = new Mission("mamie",  demandeur, Place.EHPAD);

// Ajout des missions à la liste des missions
        allMissions.addMission(mission1);
        allMissions.addMission(mission2);
        allMissions.addMission(mission3);
        allMissions.addMission(mission4);
        allMissions.addMission(mission5);



        SwingUtilities.invokeLater(() -> {
            FrameDemandeur frame = new FrameDemandeur( demandeur);
            frame.setVisible(true);
        });
    }


}
