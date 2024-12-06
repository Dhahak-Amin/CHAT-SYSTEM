package fr.insa.maven.demo.demoMavenProject;

public class Avis {

    private String comment;      // Commentaire sur l'avis
    private Benevole benevole;   // Bénévole associé à l'avis
    private int note;            // Note attribuée (0 à 5)

    // Constructeur
    public Avis(Benevole benevole) {
        this.benevole = benevole;
    }

    // Getter pour le bénévole
    public Benevole getBenevole() {
        return benevole;
    }

    // Setter pour le bénévole
    public void setBenevole(Benevole benevole) {
        this.benevole = benevole;
    }

    // Getter pour le commentaire
    public String getComment() {
        return comment;
    }

    // Setter pour le commentaire
    public void setComment(String comment) {
        this.comment = comment;
    }

    // Getter pour la note
    public int getNote() {
        return note;
    }

    // Setter pour la note avec validation
    public void setNote(int note) {
        if (note < 0 || note > 5) {
            throw new IllegalArgumentException("La note doit être comprise entre 0 et 5.");
        }
        this.note = note;
    }

    // Méthode toString pour afficher un résumé de l'avis
    @Override
    public String toString() {
        return "Avis{" +
                "benevole=" + (benevole != null ? benevole.getFirstname() + " " + benevole.getLastname() : "Non attribué") +
                ", note=" + note +
                ", comment='" + (comment != null ? comment : "Aucun commentaire") + '\'' +
                '}';
    }
}
