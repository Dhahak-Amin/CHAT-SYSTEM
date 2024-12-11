package fr.insa.maven.demo.demoMavenProject;

/**
 * Classe représentant un avis donné à un bénévole.
 * Un avis contient un commentaire, une note, et le bénévole concerné.
 */
public class Avis {

    private String comment;      // Commentaire sur l'avis
    private Benevole benevole;   // Bénévole associé à l'avis
    private int note;            // Note attribuée (entre 0 et 5)

    /**
     * Constructeur pour créer un avis associé à un bénévole.
     *
     * @param benevole Le bénévole concerné par l'avis.
     */
    public Avis(Benevole benevole) {
        this.benevole = benevole;
    }

    // Getters et Setters

    /**
     * Récupère le bénévole associé à cet avis.
     *
     * @return Le bénévole concerné.
     */
    public Benevole getBenevole() {
        return benevole;
    }

    /**
     * Définit le bénévole associé à cet avis.
     *
     * @param benevole Le bénévole concerné.
     */
    public void setBenevole(Benevole benevole) {
        this.benevole = benevole;
    }

    /**
     * Récupère le commentaire de l'avis.
     *
     * @return Le commentaire.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Définit le commentaire de l'avis.
     *
     * @param comment Le commentaire à associer.
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Récupère la note attribuée dans l'avis.
     *
     * @return La note (entre 0 et 5).
     */
    public int getNote() {
        return note;
    }

    /**
     * Définit la note de l'avis avec validation.
     *
     * @param note La note (doit être comprise entre 0 et 5).
     * @throws IllegalArgumentException Si la note est hors limites.
     */
    public void setNote(int note) {
        if (note < 0 || note > 5) {
            throw new IllegalArgumentException("La note doit être comprise entre 0 et 5.");
        }
        this.note = note;
    }

    /**
     * Représentation textuelle de l'avis pour affichage ou journalisation.
     *
     * @return Une chaîne contenant les informations principales de l'avis.
     */
    @Override
    public String toString() {
        return "Avis{" +
                "benevole=" + (benevole != null ? benevole.getFirstname() + " " + benevole.getLastname() : "Non attribué") +
                ", note=" + note +
                ", comment='" + (comment != null ? comment : "Aucun commentaire") + '\'' +
                '}';
    }
}
