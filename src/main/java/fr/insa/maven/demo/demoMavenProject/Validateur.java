package fr.insa.maven.demo.demoMavenProject;

import java.sql.Connection;
import java.util.List;
import java.util.Arrays;

/**
 * Classe représentant un validateur, un utilisateur qui peut valider ou invalider des missions.
 * Un validateur est également capable d'attribuer des missions à des bénévoles compatibles.
 */
public class Validateur extends User {

    private Connection conn; // Connexion à la base de données

    /**
     * Constructeur avec connexion à la base de données.
     *
     * @param firstname Prénom du validateur.
     * @param lastname  Nom de famille du validateur.
     * @param email     Email du validateur.
     * @param password  Mot de passe du validateur.
     * @param conn      Connexion à la base de données.
     */
    public Validateur(String firstname, String lastname, String email, String password, Connection conn) {
        super(firstname, lastname, email, password);
        this.conn = conn; // Initialisation de la connexion
        this.setAllMissions(AllMissions.getInstance()); // Initialisation de AllMissions
    }

    /**
     * Constructeur sans connexion à la base de données.
     *
     * @param firstname Prénom du validateur.
     * @param lastname  Nom de famille du validateur.
     * @param email     Email du validateur.
     * @param password  Mot de passe du validateur.
     */
    public Validateur(String firstname, String lastname, String email, String password) {
        super(firstname, lastname, email, password);
    }

    /**
     * Valide une mission donnée et attribue un bénévole si compatible.
     *
     * @param mission   La mission à valider.
     * @param benevoles Liste des bénévoles disponibles.
     */
    public void validerMission(Mission mission, List<Benevole> benevoles) {
        Place missionPlace = mission.getPlace();
        String metierRequis = mission.getIntitule(); // L'intitulé représente le métier requis

        // Validation automatique si l'emplacement est "OTHER"
        if (missionPlace == Place.OTHER) {
            mission.setEtat(MissionEtat.VALIDEE);
            mission.setBenevole(null); // Aucun bénévole requis
            this.getAllMissions().addMission(mission);
            return;
        }

        // Vérification de la validité de l'emplacement de la mission
        if (missionPlace != null && Arrays.asList(Place.HOME, Place.HOSPITAL, Place.EHPAD, Place.WORKPLACE).contains(missionPlace)) {
            Benevole benevoleAttribue = trouverBenevoleCompatibilite(benevoles, metierRequis);

            if (benevoleAttribue != null) {
                benevoleAttribue.acceptMission(mission); // Le bénévole accepte la mission
                mission.setEtat(MissionEtat.VALIDEE);
                mission.setBenevole(benevoleAttribue); // Attribution du bénévole
                this.getAllMissions().addMission(mission);
            } else {
                mission.setEtat(MissionEtat.EN_ATTENTE_AFFECTATION); // Aucun bénévole compatible trouvé
            }
        } else {
            mission.setEtat(MissionEtat.INVALIDE); // L'emplacement est invalide
        }
    }

    /**
     * Trouve un bénévole compatible avec les exigences de la mission.
     *
     * @param benevoles    Liste des bénévoles disponibles.
     * @param metierRequis Métier requis pour la mission.
     * @return Un bénévole compatible ou null si aucun n'est trouvé.
     */
    private Benevole trouverBenevoleCompatibilite(List<Benevole> benevoles, String metierRequis) {
        for (Benevole benevole : benevoles) {
            if (benevole.getMetier().equalsIgnoreCase(metierRequis)) {
                return benevole; // Retourne le premier bénévole compatible trouvé
            }
        }
        return null; // Aucun bénévole compatible
    }
}
