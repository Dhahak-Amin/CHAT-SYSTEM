package fr.insa.maven.demo.demoMavenProject;

/**
 * Classe représentant un utilisateur générique.
 * Les utilisateurs ont un prénom, un nom, une adresse email, un mot de passe,
 * et une référence vers les missions associées.
 */
public class User {

    // Attributs de l'utilisateur
    private String firstname;       // Prénom de l'utilisateur
    private String lastname;        // Nom de l'utilisateur
    private String email;           // Adresse email de l'utilisateur
    private String password;        // Mot de passe de l'utilisateur
    private AllMissions allMissions; // Liste des missions associées à l'utilisateur

    /**
     * Constructeur de la classe User.
     *
     * @param firstname Prénom de l'utilisateur
     * @param lastname  Nom de l'utilisateur
     * @param email     Adresse email de l'utilisateur
     * @param password  Mot de passe de l'utilisateur
     */
    public User(String firstname, String lastname, String email, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    // Getters et Setters

    /**
     * Retourne le prénom de l'utilisateur.
     *
     * @return Le prénom de l'utilisateur.
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Définit le prénom de l'utilisateur.
     *
     * @param firstname Le prénom à définir.
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * Retourne le nom de l'utilisateur.
     *
     * @return Le nom de l'utilisateur.
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Définit le nom de l'utilisateur.
     *
     * @param lastname Le nom à définir.
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * Retourne l'adresse email de l'utilisateur.
     *
     * @return L'adresse email de l'utilisateur.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Définit l'adresse email de l'utilisateur.
     *
     * @param email L'adresse email à définir.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retourne l'objet AllMissions associé à l'utilisateur.
     *
     * @return L'objet AllMissions.
     */
    public AllMissions getAllMissions() {
        return allMissions;
    }

    /**
     * Définit l'objet AllMissions associé à l'utilisateur.
     *
     * @param allMissions L'objet AllMissions à définir.
     */
    public void setAllMissions(AllMissions allMissions) {
        this.allMissions = allMissions;
    }

    /**
     * Retourne le mot de passe de l'utilisateur.
     *
     * @return Le mot de passe de l'utilisateur.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Définit le mot de passe de l'utilisateur.
     *
     * @param password Le mot de passe à définir.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
