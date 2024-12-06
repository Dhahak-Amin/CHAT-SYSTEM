package fr.insa.maven.demo.demoMavenProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrameValidateur extends JFrame {
    private AllMissions allMissions;

    private Validateur validateur;
    private DefaultListModel<Mission> missionListModel;
    private JList<Mission> missionList;
    private JButton validateButton;
    private JButton invalidateButton;

    public FrameValidateur(AllMissions allMissions, Validateur validateur) {
        this.allMissions = allMissions;
        this.missionListModel = new DefaultListModel<>();
        this.missionList = new JList<>(missionListModel);
        this.validateButton = new JButton("Valider la mission");
        this.invalidateButton = new JButton("Invalider la mission");
        this.validateur = validateur;

        // Configuration de la fenêtre
        setTitle("Validation des Missions");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        // Configurer le renderer pour afficher les missions
        missionList.setCellRenderer(new MissionCellRenderer());
        missionList.setFixedCellHeight(50);

        // Ajouter la liste des missions à un JScrollPane
        JScrollPane scrollPane = new JScrollPane(missionList);
        add(scrollPane, BorderLayout.CENTER);

        // Panneau des boutons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(validateButton);
        buttonPanel.add(invalidateButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Actions des boutons
        validateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeMissionStatus(MissionEtat.VALIDEE);
            }
        });

        invalidateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeMissionStatus(MissionEtat.INVALIDE);
            }
        });

        // Charger la liste des missions
        updateMissionList();
    }

    private void updateMissionList() {
        for (Mission mission : allMissions.getMissions()) {
            // Filtrer les missions qui nécessitent une validation
            if (mission.getEtat() == MissionEtat.EN_COURS_DE_VALIDATION) {
                missionListModel.addElement(mission);
            }
        }
       // System.out.println("Missions en attente de validation : " + missionListModel.size());
    }

    public String getBenevoleDetails(Benevole benevole) {
        if (benevole != null) {
            return benevole.getFirstname() + " " + benevole.getLastname() + " (" + benevole.getMetier() + ")";
        } else {
            return "Aucun bénévole attribué";
        }
    }

    private void changeMissionStatus(MissionEtat newStatus) {
        Mission selectedMission = missionList.getSelectedValue();

        if (selectedMission != null) {
            Benevole benevole = selectedMission.getBenevole();

            if (benevole == null) {
                // Avertir si aucun bénévole n'est assigné
                JOptionPane.showMessageDialog(this,
                        "Cette mission n'a pas encore de bénévole assigné. Impossible de valider ou d'invalider.",
                        "Erreur",
                        JOptionPane.WARNING_MESSAGE);
                return; // Sortir de la méthode
            }

            if (newStatus == MissionEtat.INVALIDE) {
                // Demander un motif via une boîte de dialogue
                String motif = JOptionPane.showInputDialog(
                        this,
                        "Veuillez fournir un motif pour invalider la mission :",
                        "Motif d'invalidation",
                        JOptionPane.PLAIN_MESSAGE
                );

                if (motif == null || motif.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "L'invalidation nécessite un motif.",
                            "Erreur",
                            JOptionPane.WARNING_MESSAGE);
                    return; // Annuler l'invalidation si aucun motif n'est fourni
                }

                // Ajouter le motif d'invalidation
                Motif mess = new Motif(this.validateur, motif);
                selectedMission.setMotif(mess);
            }

            // Modifier le statut de la mission
            selectedMission.setEtat(newStatus);

            // Enregistrer les modifications dans la base de données
            allMissions.enregistrerMission2(selectedMission);

            // Mettre à jour la liste des missions
            updateMissionList();

            // Message de confirmation
            JOptionPane.showMessageDialog(this,
                    "La mission a été mise à jour en : " + newStatus,
                    "Mise à jour réussie",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner une mission.",
                    "Erreur",
                    JOptionPane.WARNING_MESSAGE);
        }
    }



    // Renderer pour afficher les détails de la mission
    // Renderer pour afficher les détails de la mission
    private class MissionCellRenderer extends JPanel implements ListCellRenderer<Mission> {
        private JLabel missionLabel;

        public MissionCellRenderer() {
            setLayout(new BorderLayout());
            missionLabel = new JLabel();
            add(missionLabel, BorderLayout.CENTER);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Mission> list, Mission mission, int index, boolean isSelected, boolean cellHasFocus) {
            String benevoleDetails;
            Benevole benevole = mission.getBenevole();

            if (benevole != null) {
                benevoleDetails = benevole.getFirstname() + " " + benevole.getLastname() + " (" + benevole.getMetier() + ")";
                benevoleDetails += " - Moyenne: " + benevole.MoyennetoString();
            } else {
                benevoleDetails = "Aucun bénévole attribué";
            }

            String missionDetails = (index + 1) + ". " + mission.getIntitule() + " - " +
                    mission.getPlace().name() + " - " + mission.getEtat() + " - " + benevoleDetails;

            missionLabel.setText(missionDetails);

            // Gestion de la sélection
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
}



/*
        // Lancer l'interface
    public static void main(String[] args) {
        AllMissions allMissions = AllMissions.getInstance();

        // Exemple de missions
        Validateur validateur = new Validateur ("Elieean", "Boagaalio", "lalalla", "mdp");
        Benevole benevole = new Benevole ("Elian", "Boaglio", "lalalla", "mdp","agriculteur");
        Demandeur demandeur = new Demandeur("Alice", "Dupont", "Besoin d'aide", "Jardin", Place.HOME, "alice@example.com", "password123");
        Mission mission1 = new Mission(MissionEtat.EN_COURS_DE_VALIDATION, "Mission 1", demandeur, Place.HOME,benevole);
        Mission mission2 = new Mission(MissionEtat.EN_COURS_DE_VALIDATION, "Mission 2", demandeur, Place.HOSPITAL,benevole);
        Mission mission3 = new Mission(MissionEtat.INVALIDE, "Mission 3", demandeur, Place.WORKPLACE,benevole);

        allMissions.addMission(mission1);
        allMissions.addMission(mission2);
        allMissions.addMission(mission3);

        // Lancer l'application
        SwingUtilities.invokeLater(() -> {
            FrameValidateur frame = new FrameValidateur(allMissions, validateur);
            frame.setVisible(true);
        });
    }

 */
