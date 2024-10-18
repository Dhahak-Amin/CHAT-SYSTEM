package fr.insa.maven.demo.demoMavenProject;

public class Mission {
    private String etat;
    private String intitule;
    private Demandeur demandeur; // Attribut de type Demandeur

    public Mission(String etat, String intitule, Demandeur demandeur) {
        this.etat = etat;
        this.intitule = intitule;
        this.demandeur = demandeur;
    }

    // Getter pour 'etat'
    public String getEtat() {
        return etat;
    }

    // Setter pour 'etat'
    public void setEtat(String etat) {
        this.etat = etat;
    }

    // Getter pour 'intitule'
    public String getIntitule() {
        return intitule;
    }

    // Setter pour 'intitule'
    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    // Getter pour 'demandeur'
    public Demandeur getDemandeur() {
        return demandeur;
    }

    // Setter pour 'demandeur'
    public void setDemandeur(Demandeur demandeur) {
        this.demandeur = demandeur;
    }
}
