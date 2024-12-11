package fr.insa.maven.demo.demoMavenProject;

/**
 * Enumération représentant les différents états possibles d'une mission.
 * Chaque état correspond à une étape dans le cycle de vie d'une mission.
 */
public enum MissionEtat {
    /**
     * Mission en attente d'attribution à un bénévole.
     */
    EN_ATTENTE_AFFECTATION,

    /**
     * Mission validée et attribuée à un bénévole.
     */
    VALIDEE,

    /**
     * Mission en cours de validation par un validateur.
     */
    EN_COURS_DE_VALIDATION,

    /**
     * Mission terminée avec succès.
     */
    TERMINEE,

    /**
     * Mission considérée comme invalide.
     */
    INVALIDE;
}
