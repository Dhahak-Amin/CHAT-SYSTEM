CREATE TABLE IF NOT EXISTS `User` (
                                      `email` VARCHAR(255) NOT NULL PRIMARY KEY,
    `firstname` VARCHAR(100),
    `lastname` VARCHAR(100),
    `password` VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS `Benevole` (
                                          `email` VARCHAR(255) NOT NULL PRIMARY KEY,
    `lastname` VARCHAR(255) NOT NULL,
    `firstname` VARCHAR(255) NOT NULL,
    `metier` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `moyenne` FLOAT
    );

CREATE TABLE IF NOT EXISTS `Demandeur` (
                                           `email` VARCHAR(255) NOT NULL PRIMARY KEY,
    `lastname` VARCHAR(255) NOT NULL,
    `firstname` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `location` ENUM('HOME','HOSPITAL','EHPAD','WORKPLACE','OTHER') NOT NULL,
    `description` VARCHAR(255) NOT NULL,
    `needs` VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS `Validateur` (
                                            `email` VARCHAR(255) NOT NULL PRIMARY KEY,
    `lastname` VARCHAR(255) NOT NULL,
    `firstname` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS `Avis` (
                                      `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                      `comment` VARCHAR(255),
    `note` INT,
    `email_benevole` VARCHAR(255) NOT NULL,
    `intitule_mission` VARCHAR(255),
    FOREIGN KEY (`email_benevole`) REFERENCES `Benevole`(`email`),
    FOREIGN KEY (`intitule_mission`) REFERENCES `Mission`(`intitule`)
    );

CREATE TABLE IF NOT EXISTS `Mission` (
                                         `intitule` VARCHAR(255) NOT NULL PRIMARY KEY,
    `place` ENUM('HOME','HOSPITAL','EHPAD','WORKPLACE','OTHER') NOT NULL,
    `etat` ENUM('EN_ATTENTE_AFFECTATION','VALIDEE','EN_COURS','TERMINEE','INVALIDE','EN_COURS_DE_VALIDATION'),
    `demandeur_email` VARCHAR(255) NOT NULL,
    `benevole_email` VARCHAR(255),
    `motif` TEXT,
    `email_validateur` VARCHAR(255),
    FOREIGN KEY (`demandeur_email`) REFERENCES `Demandeur`(`email`),
    FOREIGN KEY (`benevole_email`) REFERENCES `Benevole`(`email`),
    FOREIGN KEY (`email_validateur`) REFERENCES `Validateur`(`email`)
    );
