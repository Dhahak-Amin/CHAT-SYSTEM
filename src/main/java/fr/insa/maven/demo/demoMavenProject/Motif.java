package fr.insa.maven.demo.demoMavenProject;

/**
 * Classe représentant un motif associé à une mission.
 * Le motif est défini par une explication et un validateur qui le justifie.
 */
public class Motif {

    private Validateur validateur;  // Validateur associé au motif
    private String explanation;    // Explication du motif

    /**
     * Constructeur de la classe Motif.
     *
     * @param validateur  Le validateur associé au motif.
     * @param explanation L'explication du motif.
     */
    public Motif(Validateur validateur, String explanation) {
        this.validateur = validateur;
        this.explanation = explanation;
    }

    // Getters et Setters

    /**
     * Retourne l'explication du motif.
     *
     * @return L'explication du motif.
     */
    public String getExplanation() {
        return explanation;
    }

    /**
     * Définit l'explication du motif.
     *
     * @param explanation La nouvelle explication du motif.
     */
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    /**
     * Retourne le validateur associé au motif.
     *
     * @return Le validateur.
     */
    public Validateur getValidateur() {
        return validateur;
    }

    /**
     * Définit le validateur associé au motif.
     *
     * @param validateur Le nouveau validateur.
     */
    public void setValidateur(Validateur validateur) {
        this.validateur = validateur;
    }
}
