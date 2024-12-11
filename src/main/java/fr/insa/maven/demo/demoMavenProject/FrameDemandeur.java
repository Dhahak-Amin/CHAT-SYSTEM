package fr.insa.maven.demo.demoMavenProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Classe FrameDemandeur
 * Cette classe gère l'interface graphique pour les demandeurs,
 * leur permettant de gérer leurs missions (ajouter, supprimer, noter).
 */
public class FrameDemandeur extends JFrame {
    private AllMissions allMissions; // Instance de AllMissions
    private Demandeur demandeur; // Demandeur connecté
    private JButton rateButton; // Bouton pour noter une mission
    private DefaultListModel<Mission> missionListModel; // Modèle de la liste des missions
    private JList<Mission> missionList; // Liste graphique pour afficher les missions
    private JButton addButton; // Bouton pour ajouter une mission
    private JButton deleteButton; // Bouton pour supprimer une mission

    /**
     * Constructeur de FrameDemandeur.
     *
     * @param demandeur Le demandeur connecté.
     */
    public FrameDemandeur(Demandeur demandeur) {
        this.demandeur = demandeur;
        this.allMissions = AllMissions.getInstance();
        this.missionListModel = new DefaultListModel<>();
        this.missionList = new JList<>(missionListModel);
        this.rateButton = new JButton("Noter une mission");
        this.addButton = new JButton("Ajouter une mission");
        this.deleteButton = new JButton("Supprimer une mission");

        // Configuration de la fenêtre
        setTitle("Missions de " + demandeur.getFirstname() + " " + demandeur.getLastname());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLayout(new BorderLayout());

        // Configurer le renderer personnalisé pour la liste des missions
        missionList.setCellRenderer(new MissionCellRenderer());
        missionList.setFixedCellHeight(50);

        // Ajouter la liste des missions dans un JScrollPane
        JScrollPane scrollPane = new JScrollPane(missionList);
        add(scrollPane, BorderLayout.CENTER);

        // Configuration du panneau des boutons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        buttonPanel.add(addButton);
        buttonPanel.add(rateButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Ajout des listeners aux boutons
        configureButtonActions();

        // Initialisation des missions et mise à jour de l'affichage
        allMissions.loadMissionsFromDatabase();
        updateMissionList();
    }

    /**
     * Configure les actions des boutons (ajouter, supprimer, noter).
     */
    private void configureButtonActions() {
        addButton.addActionListener(e -> addMission());
        deleteButton.addActionListener(e -> deleteMission());
        rateButton.addActionListener(e -> rateMission());
    }

    /**
     * Met à jour la liste des missions affichées.
     */
    private void updateMissionList() {
        missionListModel.clear(); // Réinitialiser la liste
        for (Mission mission : allMissions.getMissions()) {
            if (mission.getDemandeur() != null && mission.getDemandeur().getEmail().equals(this.demandeur.getEmail())) {
                missionListModel.addElement(mission);
            }
        }
    }

    /**
     * Ajoute une nouvelle mission pour le demandeur.
     */
    private void addMission() {
        String intitule = JOptionPane.showInputDialog(this, "Quel est l'intitulé de la mission ?");
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

    /**
     * Supprime une mission sélectionnée par son numéro.
     */
    private void deleteMission() {
        String input = JOptionPane.showInputDialog(this, "Entrez le numéro de la mission à supprimer :");
        if (input != null && !input.trim().isEmpty()) {
            try {
                int missionIndex = Integer.parseInt(input) - 1;
                if (missionIndex >= 0 && missionIndex < missionListModel.size()) {
                    Mission mission = missionListModel.get(missionIndex);
                    allMissions.removeMission(mission);
                    updateMissionList();
                } else {
                    JOptionPane.showMessageDialog(this, "Numéro de mission invalide !", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Veuillez entrer un numéro valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Permet de noter une mission terminée.
     */
    private void rateMission() {
        Mission selectedMission = missionList.getSelectedValue();
        if (selectedMission == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une mission à noter.", "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String noteInput = JOptionPane.showInputDialog(this, "Entrez une note pour cette mission (0 à 5) :");
        try {
            int note = Integer.parseInt(noteInput);
            if (note < 0 || note > 5) throw new NumberFormatException();

            String comment = JOptionPane.showInputDialog(this, "Entrez votre avis sur cette mission :");
            if (comment != null && !comment.trim().isEmpty()) {
                Avis avis = new Avis(selectedMission.getBenevole());
                avis.setNote(note);
                avis.setComment(comment);

                Benevole benevole = selectedMission.getBenevole();
                if (benevole != null) {
                    benevole.addAvis(avis);
                    JOptionPane.showMessageDialog(this, "Votre avis a été ajouté avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Cette mission n'est pas associée à un bénévole.", "Erreur", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vous devez entrer un avis.", "Erreur", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer une note valide entre 0 et 5.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Renderer personnalisé pour afficher les missions dans la liste.
     */
    private class MissionCellRenderer extends JPanel implements ListCellRenderer<Mission> {
        private JLabel missionLabel;

        public MissionCellRenderer() {
            setLayout(new BorderLayout());
            missionLabel = new JLabel();
            add(missionLabel, BorderLayout.CENTER);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Mission> list, Mission mission, int index, boolean isSelected, boolean cellHasFocus) {
            String missionDetails = String.format("%d. %s - %s - État : %s",
                    index + 1,
                    mission.getIntitule(),
                    mission.getPlace().name(),
                    mission.getEtat());
            missionLabel.setText(missionDetails);

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
    // Ajout dans la classe FrameBenevole
    public DefaultListModel<Mission> getMissionListModel() {
        return missionListModel;
    }
}
