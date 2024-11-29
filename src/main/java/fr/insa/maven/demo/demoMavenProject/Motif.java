package fr.insa.maven.demo.demoMavenProject;

public class Motif {


    private Validateur validateur;

    private String explanation ;


    public Motif(Validateur validateur, String  explanation){

        this.explanation =  explanation;
        this.validateur = validateur;
    }


    public String getExplanation() {
        return  explanation;
    }
    public void setExplanation(String  explanation) {
        this.explanation =  explanation;
    }


    public Validateur getValidateur() {
        return validateur;
    }
    public void setValidateur(Validateur validateur) {
        this.validateur = validateur;
    }

}
