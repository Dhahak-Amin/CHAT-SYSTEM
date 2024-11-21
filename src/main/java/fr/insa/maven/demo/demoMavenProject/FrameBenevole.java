package fr.insa.maven.demo.demoMavenProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrameBenevole extends JFrame {
    private AllMissions allMissions; // Instance de AllMissions
    private DefaultListModel<Mission> missionListModel; // Modèle de la liste des missions
    private JList<Mission> missionList; // Liste pour afficher les missions
    private JButton changeStatusButton; // Bouton pour changer le statut d'une mission

    private Benevole benevole;
    public FrameBenevole(AllMissions allMissions, Benevole benevole) {
        this.benevole=benevole;
        this.allMissions = allMissions;
        this.missionListModel = new DefaultListModel<>();
        this.missionList = new JList<>(missionListModel);
        this.changeStatusButton = new JButton("Changer le statut de la mission");

        // Configuration de la fenêtre
        setTitle("Missions du Bénévole");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLayout(new BorderLayout());

        // Configurer le renderer de la liste pour afficher la mission
        missionList.setCellRenderer(new MissionCellRenderer());
        missionList.setFixedCellHeight(50);

        // Ajouter la liste des missions à un JScrollPane
        JScrollPane scrollPane = new JScrollPane(missionList);
        add(scrollPane, BorderLayout.CENTER);

        // Configuration du bouton pour changer le statut
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(changeStatusButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // ActionListener pour le bouton "Changer le statut"
        changeStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeMissionStatus();
            }
        });

        // Initialiser l'affichage des missions
        updateMissionList();
    }

    // Méthode pour mettre à jour la liste des missions affichées
    private void updateMissionList() {
        missionListModel.clear(); // Effacer les éléments existants
        for (Mission mission : allMissions.getMissions()) {
            if (mission.getEtat().equals(MissionEtat.EN_ATTENTE_AFFECTATION)) {
                missionListModel.addElement(mission); // Ajouter chaque mission au modèle
            }
            }
    }

    // Méthode pour changer le statut d'une mission
    private void changeMissionStatus() {
        // Vérifier si une mission est sélectionnée
        Mission selectedMission = missionList.getSelectedValue();
        if (selectedMission != null) {
            // Demander à l'utilisateur de choisir un nouveau statut
            MissionEtat[] statuses = {
                    MissionEtat.EN_ATTENTE_AFFECTATION,
                    MissionEtat.EN_COURS
            };

            MissionEtat newStatus = (MissionEtat) JOptionPane.showInputDialog(
                    this,
                    "Choisissez un nouveau statut pour la mission :",
                    "Sélectionner le statut",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    statuses,
                    statuses[0]
            );

            if (newStatus != null) {
                // Modifier le statut de la mission
                selectedMission.setEtat(newStatus);

                // Si le nouveau statut est "EN_COURS", ajouter la mission à la liste du bénévole
                if (newStatus == MissionEtat.EN_COURS) {
                    benevole.acceptMission(selectedMission);
                }

                updateMissionList(); // Mettre à jour la liste affichée
            } else {
                JOptionPane.showMessageDialog(this, "Sélection annulée.", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une mission.", "Erreur", JOptionPane.WARNING_MESSAGE);
        }
    }


    // Renderer personnalisé pour afficher chaque mission
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
            String missionDetails = (index + 1) + ". " + mission.getIntitule() + " - " + mission.getPlace().name() + " - " + " - " + demandeur.getFirstname() + " " + demandeur.getLastname() + "   "+ mission.getEtat() ;
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
        // Création de l'objet AllMissions
        AllMissions allMissions = new AllMissions();

        // Exemple de missions
        Benevole benevole = new Benevole ("Elian", "Boaglio", "lalalla", "mdp");
        Demandeur demandeur = new Demandeur("Alice", "Dupont", "Besoin d'aide", "Jardin", "Paris", "alice@example.com", "password123");
        Mission mission1 = new Mission(MissionEtat.EN_COURS,"En cours", demandeur, Place.HOME);
        Mission mission2 = new Mission(MissionEtat.INVALIDE,"En attente",  demandeur, Place.WORKPLACE);
        Mission mission3 = new Mission("Complétée",  demandeur, Place.HOME);
        Mission mission4 = new Mission("Annulée",  demandeur, Place.HOSPITAL);
        Mission mission5 = new Mission("En cours",  demandeur, Place.EHPAD);

        // Ajout des missions à la liste des missions
        allMissions.addMission(mission1);
        allMissions.addMission(mission2);
        allMissions.addMission(mission3);
        allMissions.addMission(mission4);
        allMissions.addMission(mission5);

        // Lancer l'application GUI
        SwingUtilities.invokeLater(() -> {
            FrameBenevole frame = new FrameBenevole(allMissions,benevole);
            frame.setVisible(true);
        });
    }
}
