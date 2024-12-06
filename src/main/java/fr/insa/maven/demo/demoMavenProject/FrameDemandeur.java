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

    private JButton rateButton; // Bouton pour noter une mission

    private DefaultListModel<Mission> missionListModel; // Modèle de la liste
    private JList<Mission> missionList; // Liste pour afficher les missions
    private JButton addButton; // Bouton pour ajouter une mission
    private JButton deleteButton; // Bouton pour supprimer une mission

    public FrameDemandeur( Demandeur demandeur) {

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

        // Configurer le renderer de la liste pour afficher la mission
        missionList.setCellRenderer(new MissionCellRenderer());
        missionList.setFixedCellHeight(50);

        // Ajouter la liste des missions à un JScrollPane
        JScrollPane scrollPane = new JScrollPane(missionList);
        add(scrollPane, BorderLayout.CENTER);

        // Panel pour les boutons en bas
        // Utilisation d'un GridLayout pour une organisation flexible des boutons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0)); // 1 ligne, 3 colonnes, avec un espace horizontal de 10px

        buttonPanel.add(addButton);
        buttonPanel.add(rateButton);
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
        rateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rateMission();
            }
        });


        // Initialiser l'affichage des missions
        allMissions.loadMissionsFromDatabase();
        updateMissionList();
    }

    // Méthode pour mettre à jour la liste des missions affichées
    private void updateMissionList() {
        missionListModel.clear(); // On vide le modèle avant de le remplir pour éviter les doublons

        for (Mission mission : allMissions.getMissions()) {
            // Vérifie si la mission appartient au demandeur connecté
            if (mission.getDemandeur() != null && mission.getDemandeur().getEmail().equals(this.demandeur.getEmail())) {
                missionListModel.addElement(mission);
            }
        }
       // System.out.println("Missions affichées pour le demandeur " + demandeur.getEmail() + ": " + missionListModel.size());
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

    // Méthode pour noter une mission
    private void rateMission() {
        Mission selectedMission = missionList.getSelectedValue();
        if (selectedMission == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une mission à noter.", "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Saisie de la note (0 à 5)
        String noteInput = JOptionPane.showInputDialog(this, "Entrez une note pour cette mission (0 à 5) :");
        if (noteInput == null || noteInput.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vous devez entrer une note.", "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int note;
        try {
            note = Integer.parseInt(noteInput);
            if (note < 0 || note > 5) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un entier entre 0 et 5.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Saisie du commentaire
        String comment = JOptionPane.showInputDialog(this, "Entrez votre avis sur cette mission :");
        if (comment == null || comment.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vous devez entrer un avis.", "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Création de l'avis
        Avis avis = new Avis(selectedMission.getBenevole());
        avis.setNote(note);
        avis.setComment(comment);


        // Ajouter l'avis au bénévole ou à une autre entité
        Benevole benevole = selectedMission.getBenevole(); // Récupère le bénévole de la mission
        if (benevole != null) {
            benevole.addAvis(avis);
            allMissions.removeMission(selectedMission);
            updateMissionList();
            JOptionPane.showMessageDialog(this, "Votre avis a été ajouté avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Cette mission n'est pas associée à un bénévole.", "Erreur", JOptionPane.WARNING_MESSAGE);
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
        Benevole benevole = new Benevole ("Elian", "Boaglio", "lalalla", "mdp","agriculteur");
        Mission mission1 = new Mission(MissionEtat.INVALIDE,"jardin",  demandeur, Place.HOME);
        Mission mission2 = new Mission(MissionEtat.VALIDEE,"piscine",  demandeur, Place.WORKPLACE, benevole);
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
