package fr.insa.maven.demo.demoMavenProject;

<<<<<<< Updated upstream

    private Benevole benevole ;
    private Place place ;
    private String intitule;
    private Demandeur demandeur; // Attribut de type Demandeur

    public Mission(String etat, String intitule, Demandeur demandeur, Place place) {
        this.etat = etat;
        this.place=place ;
        this.intitule = intitule;
        this.demandeur = demandeur;
=======
public class Mission {
    private String intitule;
    private Place place;
    private MissionEtat etat;
    private Demandeur user;
    private Benevole benevole;

    // Constructeur
    public Mission(String intitule, Place place, MissionEtat etat, Demandeur user) {
        this.intitule = intitule;
        this.place = place;
        this.etat = etat;
        this.user = user;
>>>>>>> Stashed changes
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

<<<<<<< Updated upstream
    // Getter pour 'demandeur'
    public Demandeur getDemandeur() {
        return demandeur;
    }

    // Setter pour 'demandeur'
    public void setDemandeur(Demandeur demandeur) {
        this.demandeur = demandeur;
=======
    public Demandeur getUser() {
        return user;
    }

    public void setUser(Demandeur user) {
        this.user = user;
>>>>>>> Stashed changes
    }

    public Benevole getBenevole() {
        return benevole;
    }

<<<<<<< Updated upstream
    public Benevole getBenevole() {
        return benevole;
    }

    public void setBenevole(Benevole benevole) {
        this.benevole = benevole;
    }

=======
    public void setBenevole(Benevole benevole) {
        this.benevole = benevole;
    }
>>>>>>> Stashed changes
}
