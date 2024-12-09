🛠️ Projet Gestion des Missions - Maven & Swing

Group: A1
Members:

    Dhahak Smith Mohamed Ali Amine
    Boaglio Elian

Emails:

    📧 dhahak@insa-toulouse.fr
    📧 elian.boaglio@insa-toulouse.fr

🚀 Description du Projet

Ce projet est une application de gestion des missions développée en Java avec Maven. Il intègre une base de données MySQL pour la persistance des données et des interfaces graphiques interactives avec Swing.
Fonctionnalités principales :

    🎯 Authentification et inscription des utilisateurs.
    👨‍💻 Rôles gérés : Bénévole, Demandeur, Validateur, Administrateur.
    📊 Gestion des missions (ajout, suppression, mise à jour).
    📤 Exportation des données au format CSV.

🧰 Prérequis
Logiciels :

    Java Development Kit (JDK) version 8 ou ultérieure.
    Maven pour gérer les dépendances.
    MySQL pour la base de données.

⚙️ Configuration
Base de données distante :

Le projet fonctionne principalement avec une base de données fournie par l'INSA. Voici les paramètres à configurer dans votre environnement :

    URL : jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/projet_gei_012
    User : projet_gei_012
    Password : dith1Que

Étape obligatoire :

    Importez les scripts SQL pour initialiser les tables nécessaires à l'application.

Base de données locale (tests unitaires) :

Pour tester les fonctionnalités via des tests unitaires, utilisez une base de données locale configurée avec les paramètres suivants :

    URL : jdbc:mysql://localhost:3306/test_db
    User : root
    Password : root

Étape obligatoire pour les tests :

    Créez une base de données nommée test_db.
    Assurez-vous que le fichier SQL d'initialisation est disponible à l'emplacement suivant :
    src/test/resources/Sql_Test.sql
    Le fichier SQL sera exécuté automatiquement pour préparer la base de données pour les tests.

🛠️ Comment lancer le projet
1. Lancer l'application avec la base de données distante :

   Clonez le projet :

git clone <url_du_projet>
cd <dossier_du_projet>

Compilez et exécutez avec Maven :

    mvn clean install
    mvn exec:java -Dexec.mainClass="fr.insa.maven.demo.demoMavenProject.Main"

    Interagissez avec le programme via l'interface Swing.

2. Tester avec la base de données locale :

   Assurez-vous que MySQL est installé et accessible localement.
   Configurez les paramètres de connexion dans la classe DatabaseManager :
   URL : jdbc:mysql://localhost:3306/test_db
   User : root
   Password : root
   Exécutez les tests avec Maven :

   mvn test

🧪 Instructions pour tester
1. Authentification :

   Lancer l’application et choisissez un rôle.
   Entrez votre email et mot de passe pour vous connecter.
   Si l'utilisateur n'existe pas, utilisez la fonction d'inscription.

2. Gestion des missions :

   Ajoutez une mission via l'interface du demandeur.
   Acceptez ou validez des missions en tant que bénévole ou validateur.

3. Exportation :

   Connectez-vous en tant qu’administrateur.
   Sélectionnez une table et exportez ses données vers un fichier CSV.

📞 Contact

Pour toute question ou suggestion, n'hésitez pas à nous contacter :

    Dhahak Smith Mohamed Ali Amine : 📧 dhahak@insa-toulouse.fr
    Boaglio Elian : 📧 elian.boaglio@insa-toulouse.fr

🎉 Merci !

Merci d’utiliser notre application ! 🚀 Si vous avez des retours, n'hésitez pas à nous en faire part. 😊