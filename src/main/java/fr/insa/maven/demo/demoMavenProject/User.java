package fr.insa.maven.demo.demoMavenProject;

public class User {
    private String firstname;
    private String lastname;
    private String email;
    private String password;



    private AllMissions allMissions ;


    public User(String firstname, String lastname, String email, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;

    }

    // Getters and setters
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AllMissions getAllMissions() {
        return allMissions;
    }

    public void setAllMissions(AllMissions allMissions) {
        this.allMissions = allMissions;
    }

    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }
}
