🛠️ Application d’aide aux personnes vulnérables - Maven & Swing

👥 Group: A1 Members

    Dhahak Smith Mohamed Ali Amine
    📧 dhahak@insa-toulouse.fr
    Boaglio Elian
    📧 elian.boaglio@insa-toulouse.fr

🚀 Description du Projet

Cette application Java, développée avec Maven et intégrant Swing, est conçue pour simplifier la gestion des missions via une interface graphique conviviale. Elle permet une interaction efficace entre différents utilisateurs (bénévoles, demandeurs, validateurs, administrateurs) tout en s'appuyant sur une base de données MySQL pour la persistance des données.
L'objectif principal du projet est de fournir un outil intuitif pour coordonner des missions, gérer les utilisateurs, et assurer une traçabilité des données via des fonctionnalités d'exportation.

✨ Fonctionnalités principales

    🎯 Authentification et inscription des utilisateurs.
    👨‍💻 Rôles gérés : Bénévole, Demandeur, Validateur, Administrateur.
    📊 Gestion complète des missions : ajout, suppression, mise à jour.
    📤 Exportation des données au format CSV.

🧰 Prérequis Logiciels

    Java Development Kit (JDK) : version 8 ou ultérieure.
    Maven : pour la gestion des dépendances et l'exécution.
    MySQL : pour la gestion des bases de données.

⚙️ Configuration de la Base de Données
🔗 Base de données distante (production)

Le projet utilise une base de données distante fournie par l'INSA Toulouse. Cette base est configurée automatiquement via les paramètres de connexion définis dans le fichier RemoteDatabaseManager.
Paramètres de connexion :

    URL : jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/projet_gei_012
    User : projet_gei_012
    Password : dith1Que

📝 Étape obligatoire :

    Importez les scripts SQL nécessaires pour créer les tables et préparer la base de données.
        Assurez-vous que le fichier SQL initial contient toutes les tables nécessaires au bon fonctionnement du projet.
        Utilisez des outils comme MySQL Workbench ou l'interface CLI pour exécuter le fichier SQL sur la base distante.

    Vérifiez que la base de données est correctement initialisée :
        Lorsque l'application démarre, le programme utilise RemoteDatabaseManager pour établir une connexion avec la base distante.
        Les données nécessaires (missions, utilisateurs, etc.) sont automatiquement chargées depuis la base via des méthodes comme loadMissionsFromDatabase dans la classe AllMissions.

    Comment valider que tout est prêt :
        Une fois connecté, l'application charge naturellement les missions et utilisateurs dans la mémoire.
        Si la base de données n'est pas configurée correctement ou manque des tables, l'application affichera un message d'erreur descriptif.
        Vous pouvez également exécuter une commande SQL pour vérifier la présence des tables: show tables ;



🛠️ Base de données locale (tests unitaires)

Pour exécuter les tests unitaires, une base de données locale peut être utilisée.
Paramètres de connexion :

    URL : jdbc:mysql://localhost:3306/test_db
    User : root
    Password : root

Étape obligatoire pour les tests :

    Créez une base de données nommée test_db.
    Placez le fichier SQL d'initialisation à l'emplacement suivant :
    src/test/resources/Sql_Test.sql.
    Ce fichier sera automatiquement exécuté pour préparer la base.


🌟 Astuce : Détection Automatique des Tables

    L'application inclut une vérification automatique pour s'assurer que toutes les tables nécessaires sont présentes. Cette vérification est effectuée à l'aide de la méthode checkTableExists de la classe RemoteDatabaseManager.
    En cas d'absence d'une table, un message vous invite à vérifier votre configuration.
🛠️ Comment Lancer le Projet
🌐 Lancer avec la base de données distante :

    Clonez le projet :

git clone <url_du_projet>
cd <dossier_du_projet>

Compilez et exécutez avec Maven :

    mvn clean install
    mvn compile 
    mvn exec:java -Dexec.mainClass="fr.insa.maven.demo.demoMavenProject.Main"
    Remarque : il faut executer cette commande si vous utiliser Winows:  mvn exec:java `-Dexec.mainClass=fr.insa.maven.demo.demoMavenProject.Main

    Interagissez avec le programme via l'interface graphique Swing.

🧪 Tester avec la base de données locale :

    Assurez-vous que MySQL est correctement installé et accessible localement.
    Configurez les paramètres dans la classe DatabaseManager :

URL : jdbc:mysql://localhost:3306/test_db
User : root
Password : root

Exécutez les tests avec Maven :

mvn test

📞 Contact

    Dhahak Smith Mohamed Ali Amine
    📧 dhahak@insa-toulouse.fr
    Boaglio Elian
    📧 elian.boaglio@insa-toulouse.fr

🎉 Merci !

Nous espérons que notre projet répondra à vos attentes !
🚀 Bon test et exploration ! 😊