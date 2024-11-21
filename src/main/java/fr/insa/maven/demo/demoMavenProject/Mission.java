package fr.insa.maven.demo.demoMavenProject;

public class Mission {
    private Benevole benevole;
    private Place place;
    private MissionEtat etat;
    private String intitule;
    private Demandeur demandeur;

    // Constructeur
    public Mission(MissionEtat etat, String intitule, Demandeur demandeur, Place place) {
        this.etat = etat;
        this.place = place;
        this.intitule = intitule;
        this.demandeur = demandeur;
    }

    // Getters et Setters
    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public MissionEtat getEtat() {
        return etat;
    }

    public void setEtat(MissionEtat etat) {
        this.etat = etat;
    }

    public Demandeur getDemandeur() {
        return demandeur;
    }

    public void setDemandeur(Demandeur demandeur) {
        this.demandeur = demandeur;
    }

    public Benevole getBenevole() {
        return benevole;
    }

    public void setBenevole(Benevole benevole) {
        this.benevole = benevole;
    }
}
