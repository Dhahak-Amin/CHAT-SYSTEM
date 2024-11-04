package fr.insa.maven.demo.demoMavenProject;
import java.util.Scanner;
public class Mission {
    private String etat;


    private Place place ;
    private String intitule;
    private User user; // Attribut de type Demandeur

    public Mission(String etat, String intitule, User user, Place place) {
        this.etat = etat;
        this.place=place ;
        this.intitule = intitule;
        this.user = user;
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
    public User getUser() {
        return user;
    }

    // Setter pour 'demandeur'
    public void setUser(User user) {
        this.user = user;
    }
    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

}
