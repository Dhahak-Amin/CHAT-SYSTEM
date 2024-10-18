package fr.insa.maven.demo.demoMavenProject;


public class Demandeur extends User {
    private String description;
    private String needs;
    private String location;

    public Demandeur(String firstname, String lastname, String description, String needs, String location, String email, String password) {
        super(firstname, lastname,email,password);
        this.description = description;
        this.needs = needs;
        this.location = location;
    }

    // Getters and setters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNeeds() {
        return needs;
    }

    public void setNeeds(String needs) {
        this.needs = needs;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    
    
    
    
}
