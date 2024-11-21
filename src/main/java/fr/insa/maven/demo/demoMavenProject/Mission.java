package fr.insa.maven.demo.demoMavenProject;
import java.util.Scanner;
public class Mission {
    private String etat;



    private Benevole benevole ;
    private Place place ;
    private String intitule;
    private Demandeur demandeur; // Attribut de type Demandeur

    public Mission(String etat, String intitule, Demandeur demandeur, Place place) {
        this.etat = etat;
        this.place=place ;
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
    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Benevole getBenevole() {
        return benevole;
    }

    public void setBenevole(Benevole benevole) {
        this.benevole = benevole;
    }

}
