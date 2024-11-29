package fr.insa.maven.demo.demoMavenProject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Arrays;

public class Validateur extends User {
    private Connection conn; // Connexion à la base de données

    public Validateur(String firstname, String lastname, String email, String password, Connection conn ) {
        super(firstname, lastname, email, password);
        this.conn = conn; // Initialiser la connexion
    }
    public Validateur(String firstname, String lastname, String email, String password ) {
        super(firstname, lastname, email, password);

    }


    public void validerMission(Mission mission, List<Benevole> benevoles) {
        Place missionPlace = mission.getPlace();
        String metierRequis = mission.getIntitule(); // Utilise l'intitulé comme le métier requis

        // Si l'emplacement est OTHER, la mission est automatiquement validée
        if (missionPlace == Place.OTHER) {
            mission.setEtat(MissionEtat.VALIDEE);
            mission.setBenevole(null);
            this.getAllMissions().enregistrerMission(mission);
            // Aucun bénévole attribué
             // Enregistrer la mission validée
            System.out.println("Mission validée automatiquement pour emplacement : " + missionPlace);
            return;
        }

        // Vérifie si l'emplacement de la mission est valide
        if (missionPlace != null && Arrays.asList(Place.HOME, Place.HOSPITAL, Place.EHPAD, Place.WORKPLACE).contains(missionPlace)) {
            Benevole benevoleAttribue = trouverBenevoleCompatibilite(benevoles, metierRequis);

            if (benevoleAttribue != null) {
                benevoleAttribue.acceptMission(mission); // Le bénévole accepte la mission
                mission.setEtat(MissionEtat.VALIDEE);
                mission.setBenevole(benevoleAttribue); // Assignation du bénévole à la mission
                this.getAllMissions().enregistrerMission(mission); // Enregistrer la mission validée
                System.out.println("Mission validée et attribuée à " + benevoleAttribue.getFirstname() + " " + benevoleAttribue.getLastname());
            } else {
                System.out.println("Aucun bénévole disponible pour l'emplacement choisi ou avec le métier requis.");
                mission.setEtat(MissionEtat.EN_ATTENTE_AFFECTATION);
            }
        } else {
            System.out.println("L'emplacement de la mission n'est pas valide. Mission invalidée.");
            mission.setEtat(MissionEtat.INVALIDE);
        }
    }



    private Benevole trouverBenevoleCompatibilite(List<Benevole> benevoles, String metierRequis) {
        // Rechercher un bénévole avec le métier requis et qui est libre
        for (Benevole benevole : benevoles) {
            if ( benevole.getMetier().equalsIgnoreCase(metierRequis)) {
                return benevole; // Retourne le premier bénévole libre avec le métier correspondant
            }
        }
        return null; // Aucun bénévole avec le métier requis n'est disponible
    }
}
