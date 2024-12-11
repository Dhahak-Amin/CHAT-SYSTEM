package fr.insa.maven.demo.demoMavenProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Classe FrameValidateur
 * Gère l'interface graphique pour les validateurs, leur permettant
 * de valider ou invalider les missions.
 */
public class FrameValidateur extends JFrame {
    private AllMissions allMissions; // Instance de AllMissions
    private Validateur validateur; // Validateur connecté
    private DefaultListModel<Mission> missionListModel; // Modèle de liste pour les missions
    private JList<Mission> missionList; // Liste graphique pour afficher les missions
    private JButton validateButton; // Bouton pour valider une mission
    private JButton invalidateButton; // Bouton pour invalider une mission

    /**
     * Constructeur de FrameValidateur
     *
     * @param allMissions Instance des missions
     * @param validateur  Validateur connecté
     */
    public FrameValidateur(AllMissions allMissions, Validateur validateur) {
        this.allMissions = allMissions;
        this.validateur = validateur;

        // Initialisation des composants
        this.missionListModel = new DefaultListModel<>();
        this.missionList = new JList<>(missionListModel);
        this.validateButton = new JButton("Valider la mission");
        this.invalidateButton = new JButton("Invalider la mission");

        // Configuration de la fenêtre
        setTitle("Validation des Missions");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        // Configurer la liste des missions avec un renderer personnalisé
        missionList.setCellRenderer(new MissionCellRenderer());
        missionList.setFixedCellHeight(50);

        // Ajouter la liste des missions dans un JScrollPane
        JScrollPane scrollPane = new JScrollPane(missionList);
        add(scrollPane, BorderLayout.CENTER);

        // Panneau pour les boutons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(validateButton);
        buttonPanel.add(invalidateButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Configurer les actions des boutons
        configureButtonActions();

        // Charger les missions et mettre à jour l'affichage
        updateMissionList();
    }

    /**
     * Configure les actions des boutons de validation et d'invalidation.
     */
    private void configureButtonActions() {
        validateButton.addActionListener(e -> changeMissionStatus(MissionEtat.VALIDEE));
        invalidateButton.addActionListener(e -> changeMissionStatus(MissionEtat.INVALIDE));
    }

    /**
     * Met à jour la liste des missions affichées.
     * Affiche uniquement les missions en cours de validation.
     */
    private void updateMissionList() {
        missionListModel.clear(); // Réinitialiser la liste
        for (Mission mission : allMissions.getMissions()) {
            if (mission.getEtat() == MissionEtat.EN_COURS_DE_VALIDATION) {
                missionListModel.addElement(mission);
            }
        }
    }

    /**
     * Retourne les détails du bénévole associé à une mission.
     *
     * @param benevole Bénévole de la mission
     * @return Détails du bénévole ou "Aucun bénévole attribué"
     */
    private String getBenevoleDetails(Benevole benevole) {
        if (benevole != null) {
            return benevole.getFirstname() + " " + benevole.getLastname() + " (" + benevole.getMetier() + ")";
        } else {
            return "Aucun bénévole attribué";
        }
    }

    /**
     * Change le statut de la mission sélectionnée (validée ou invalidée).
     *
     * @param newStatus Nouveau statut de la mission
     */
    private void changeMissionStatus(MissionEtat newStatus) {
        Mission selectedMission = missionList.getSelectedValue();

        if (selectedMission != null) {
            Benevole benevole = selectedMission.getBenevole();

            if (benevole == null) {
                JOptionPane.showMessageDialog(this,
                        "Cette mission n'a pas encore de bénévole assigné. Impossible de valider ou d'invalider.",
                        "Erreur",
                        JOptionPane.WARNING_MESSAGE);
                return;
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
                    return;
                }

                // Ajouter le motif à la mission
                Motif invalidationMotif = new Motif(this.validateur, motif);
                selectedMission.setMotif(invalidationMotif);
            }

            // Mettre à jour le statut de la mission
            selectedMission.setEtat(newStatus);

            // Enregistrer la mission dans la base de données
            allMissions.enregistrerMission2(selectedMission);

            // Mettre à jour l'affichage
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

    /**
     * Renderer personnalisé pour afficher les détails de la mission.
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
