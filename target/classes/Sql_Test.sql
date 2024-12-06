-- Table Utilisateur
CREATE TABLE IF NOT EXISTS Utilisateur (
    email VARCHAR(255) NOT NULL PRIMARY KEY,
    firstname VARCHAR(100),
    lastname VARCHAR(100),
    password VARCHAR(255)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table Demandeur
CREATE TABLE IF NOT EXISTS Demandeur (
    email VARCHAR(255) NOT NULL PRIMARY KEY,
    lastname VARCHAR(255) NOT NULL,
    firstname VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    location VARCHAR(50) NOT NULL,
    description VARCHAR(255) NOT NULL,
    needs VARCHAR(255) NOT NULL
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table Benevole
CREATE TABLE IF NOT EXISTS Benevole (
    email VARCHAR(255) NOT NULL PRIMARY KEY,
    lastname VARCHAR(255) NOT NULL,
    firstname VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table Validateur
CREATE TABLE IF NOT EXISTS Validateur (
    email VARCHAR(255) NOT NULL PRIMARY KEY,
    lastname VARCHAR(255) NOT NULL,
    firstname VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table Mission
CREATE TABLE IF NOT EXISTS Mission (
    intitule VARCHAR(255) NOT NULL PRIMARY KEY,
    place VARCHAR(50) NOT NULL,
    etat VARCHAR(50),
    demandeur_email VARCHAR(255) NOT NULL,
    benevole_email VARCHAR(255),
    motif VARCHAR(255),
    email_validateur VARCHAR(255),
    FOREIGN KEY (demandeur_email) REFERENCES Demandeur(email),
    FOREIGN KEY (benevole_email) REFERENCES Benevole(email),
    FOREIGN KEY (email_validateur) REFERENCES Validateur(email)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table Avis
CREATE TABLE IF NOT EXISTS Avis (
    id INT AUTO_INCREMENT PRIMARY KEY,
    benevole_email VARCHAR(255) NOT NULL,
    commentaire VARCHAR(255) NOT NULL,
    note INT,
    FOREIGN KEY (benevole_email) REFERENCES Benevole(email)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
