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

    private JLabel moyenneLabel;
    private Benevole benevole;
    public FrameBenevole(AllMissions allMissions, Benevole benevole) {
        this.benevole=benevole;
        this.allMissions = allMissions;
        // Ajout de l'étiquette de la moyenne
        moyenneLabel = new JLabel("Moyenne des avis : " + benevole.MoyennetoString());
        add(moyenneLabel, BorderLayout.NORTH);
        this.missionListModel = new DefaultListModel<>();
        this.missionList = new JList<>(missionListModel);
        this.changeStatusButton = new JButton("Accepter la mission");

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
        updateMissionList();    }

    // Méthode pour mettre à jour la liste des missions affichées
    private void updateMissionList() {

        for (Mission mission : allMissions.getMissions()) {
            // Affiche les missions en attente d'affectation
            if (mission.getEtat() == MissionEtat.EN_ATTENTE_AFFECTATION && mission.getBenevole() == null) {
                missionListModel.addElement(mission);
            }
        }

    }

    private void changeMissionStatus() {
        // Vérifie si une mission est sélectionnée
        Mission selectedMission = missionList.getSelectedValue();
        if (selectedMission != null) {
            if (selectedMission.getPlace() == Place.HOSPITAL) {
                // Missions avec PLACE == HOSPITAL passent directement en EN_COURS_DE_VALIDATION
                selectedMission.setEtat(MissionEtat.EN_COURS_DE_VALIDATION);

                // Assigner la mission au bénévole
                benevole.acceptMission(selectedMission);

                // Enregistrer la mise à jour
                allMissions.enregistrerMission2(selectedMission);

                JOptionPane.showMessageDialog(this,
                        "La mission est maintenant en cours de validation par un validateur.",
                        "Mise à jour réussie",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Missions avec une autre PLACE sont directement validées
                selectedMission.setEtat(MissionEtat.VALIDEE);

                // Assigner la mission au bénévole
                benevole.acceptMission(selectedMission);

                // Enregistrer la mise à jour
                allMissions.enregistrerMission2(selectedMission);

                JOptionPane.showMessageDialog(this,
                        "La mission a été validée avec succès.",
                        "Mise à jour réussie",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            // Mettre à jour l'affichage
            updateMissionList();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner une mission.",
                    "Erreur",
                    JOptionPane.WARNING_MESSAGE);
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
    // Ajout dans la classe FrameBenevole
    public DefaultListModel<Mission> getMissionListModel() {
        return missionListModel;
    }

/*
    public static void main(String[] args) {
        // Création de l'objet AllMissions
        AllMissions allMissions = AllMissions.getInstance();

        // Exemple de missions
        Benevole benevole = new Benevole ("Elian", "Boaglio", "lalalla", "mdp","agriculteur");
        Demandeur demandeur = new Demandeur("Alice", "Dupont", "Besoin d'aide", "Jardin", Place.HOME, "alice@example.com", "password123");
        Mission mission1 = new Mission(MissionEtat.EN_COURS_DE_VALIDATION,"En cours", demandeur, Place.HOME);
        Mission mission2 = new Mission(MissionEtat.INVALIDE,"En attente",  demandeur, Place.WORKPLACE);
        Mission mission3 = new Mission("voiture maison hehe",  demandeur, Place.HOME);
        Mission mission4 = new Mission("besoin de soin d'urgence",  demandeur, Place.HOSPITAL);
        Mission mission5 = new Mission("un peu de compagnie",  demandeur, Place.EHPAD);

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

*/

}
