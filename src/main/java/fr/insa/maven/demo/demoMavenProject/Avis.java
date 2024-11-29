package fr.insa.maven.demo.demoMavenProject;

public class Avis {

    private String comment;

    Benevole benevole ;
    private int note; // Utilise le type Note au lieu d'un simple int

    public Avis(Benevole benevole) {
        this.benevole = benevole;

    }



    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getNote() {
        return note;
    }

    public void setValueNote(int value) {
        if (value < 0 || value > 5) {
            throw new IllegalArgumentException("La note doit être un entier entre 0 et 5.");
        }
        this.note = value;
    }

    @Override
    public String toString() {
        return "Avis{" +

                ", comment='" + comment + '\'' +
                ", note=" + note +
                '}';
    }
}
