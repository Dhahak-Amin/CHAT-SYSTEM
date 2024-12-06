-- Table User
CREATE TABLE IF NOT EXISTS User (
                                    email VARCHAR(255) NOT NULL PRIMARY KEY,
    firstname VARCHAR(100),
    lastname VARCHAR(100),
    password VARCHAR(255)
    );

-- Table Demandeur
CREATE TABLE IF NOT EXISTS Demandeur (
                                         email VARCHAR(255) NOT NULL PRIMARY KEY,
    firstname VARCHAR(255),
    lastname VARCHAR(255),
    password VARCHAR(255),
    location VARCHAR(50),
    description VARCHAR(255),
    needs VARCHAR(255)
    );

-- Table Benevole
CREATE TABLE IF NOT EXISTS Benevole (
                                        email VARCHAR(255) NOT NULL PRIMARY KEY,
    firstname VARCHAR(255),
    lastname VARCHAR(255),
    password VARCHAR(255)
    );

-- Table Validateur
CREATE TABLE IF NOT EXISTS Validateur (
                                          email VARCHAR(255) NOT NULL PRIMARY KEY,
    firstname VARCHAR(255),
    lastname VARCHAR(255),
    password VARCHAR(255)
    );

-- Table Mission
CREATE TABLE IF NOT EXISTS Mission (
                                       intitule VARCHAR(255) NOT NULL PRIMARY KEY,
    place VARCHAR(50),
    etat VARCHAR(50),
    demandeur_email VARCHAR(255),
    benevole_email VARCHAR(255),
    motif VARCHAR(255),
    email_validateur VARCHAR(255),
    FOREIGN KEY (demandeur_email) REFERENCES Demandeur(email) ON DELETE SET NULL,
    FOREIGN KEY (benevole_email) REFERENCES Benevole(email) ON DELETE SET NULL,
    FOREIGN KEY (email_validateur) REFERENCES Validateur(email) ON DELETE SET NULL
    );

-- Table Avis
CREATE TABLE IF NOT EXISTS Avis (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    benevole_email VARCHAR(255),
    commentaire VARCHAR(255),
    note INT CHECK (note >= 0 AND note <= 5),
    FOREIGN KEY (benevole_email) REFERENCES Benevole(email) ON DELETE CASCADE
    );
