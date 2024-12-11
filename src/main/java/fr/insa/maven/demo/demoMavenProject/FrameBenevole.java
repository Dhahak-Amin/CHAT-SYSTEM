package fr.insa.maven.demo.demoMavenProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Classe FrameBenevole
 * Cette classe gère l'interface graphique pour les bénévoles et leur permet de consulter
 * et accepter les missions disponibles.
 */
public class FrameBenevole extends JFrame {
    private AllMissions allMissions; // Instance de AllMissions contenant toutes les missions
    private DefaultListModel<Mission> missionListModel; // Modèle de données pour afficher les missions
    private JList<Mission> missionList; // Liste graphique des missions
    private JButton changeStatusButton; // Bouton pour accepter une mission
    private JLabel moyenneLabel; // Affiche la moyenne des avis du bénévole
    private Benevole benevole; // Référence au bénévole connecté

    /**
     * Constructeur de FrameBenevole.
     *
     * @param allMissions Instance de AllMissions contenant toutes les missions.
     * @param benevole    Bénévole connecté.
     */
    public FrameBenevole(AllMissions allMissions, Benevole benevole) {
        this.benevole = benevole;
        this.allMissions = allMissions;

        // Configuration de la fenêtre
        setTitle("Missions du Bénévole");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLayout(new BorderLayout());

        // Affichage de la moyenne des avis
        moyenneLabel = new JLabel("Moyenne des avis : " + benevole.MoyennetoString());
        add(moyenneLabel, BorderLayout.NORTH);

        // Configuration de la liste des missions
        missionListModel = new DefaultListModel<>();
        missionList = new JList<>(missionListModel);
        missionList.setCellRenderer(new MissionCellRenderer()); // Renderer personnalisé
        missionList.setFixedCellHeight(50);

        // Ajouter la liste des missions à un JScrollPane
        JScrollPane scrollPane = new JScrollPane(missionList);
        add(scrollPane, BorderLayout.CENTER);

        // Configuration du bouton pour accepter les missions
        changeStatusButton = new JButton("Accepter la mission");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(changeStatusButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // ActionListener pour le bouton
        changeStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeMissionStatus();
            }
        });

        // Initialisation de la liste des missions
        updateMissionList();
    }

    /**
     * Met à jour la liste des missions affichées.
     * Seules les missions en attente d'affectation et sans bénévole assigné sont affichées.
     */
    private void updateMissionList() {
        missionListModel.clear();
        for (Mission mission : allMissions.getMissions()) {
            if (mission.getEtat() == MissionEtat.EN_ATTENTE_AFFECTATION && mission.getBenevole() == null) {
                missionListModel.addElement(mission);
            }
        }
    }

    /**
     * Change le statut de la mission sélectionnée.
     * Les missions avec Place.HOSPITAL passent en EN_COURS_DE_VALIDATION, les autres sont VALIDÉES.
     */
    private void changeMissionStatus() {
        Mission selectedMission = missionList.getSelectedValue();
        if (selectedMission != null) {
            if (selectedMission.getPlace() == Place.HOSPITAL) {
                selectedMission.setEtat(MissionEtat.EN_COURS_DE_VALIDATION);
                benevole.acceptMission(selectedMission);
                allMissions.enregistrerMission2(selectedMission);
                JOptionPane.showMessageDialog(this, "La mission est maintenant en cours de validation par un validateur.", "Mise à jour réussie", JOptionPane.INFORMATION_MESSAGE);
            } else {
                selectedMission.setEtat(MissionEtat.VALIDEE);
                benevole.acceptMission(selectedMission);
                allMissions.enregistrerMission2(selectedMission);
                JOptionPane.showMessageDialog(this, "La mission a été validée avec succès.", "Mise à jour réussie", JOptionPane.INFORMATION_MESSAGE);
            }
            updateMissionList();
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une mission.", "Erreur", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Renderer personnalisé pour afficher chaque mission dans la liste.
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
            Demandeur demandeur = mission.getDemandeur();
            String missionDetails = String.format("%d. %s - %s - %s %s - %s",
                    index + 1,
                    mission.getIntitule(),
                    mission.getPlace().name(),
                    demandeur.getFirstname(),
                    demandeur.getLastname(),
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

    /**
     * Getter pour le modèle de données de la liste des missions.
     *
     * @return Modèle de données de la liste des missions.
     */
    public DefaultListModel<Mission> getMissionListModel() {
        return missionListModel;
    }
}
