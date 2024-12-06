package fr.insa.maven.demo.demoMavenProject;


import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.regex.Pattern;
public class ConnectionManager {
    private static final String DB_URL = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/projet_gei_012";
    private static final String USER = "projet_gei_012";
    private static final String PASS = "dith1Que";

    private static ConnectionManager instance;
    private Connection conn;

    public static ConnectionManager getInstance() {
        if (instance == null) {
            synchronized (ConnectionManager.class) {
                if (instance == null) {
                    instance = new ConnectionManager();
                }
            }
        }
        return instance;
    }
    public Connection getConnection() {
        return conn;
    }

    public ConnectionManager() {
        try {
            this.conn = DriverManager.getConnection(DB_URL, USER, PASS);
            JOptionPane.showMessageDialog(null, "Connexion réussie à la base de données !", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur de connexion à la base de données. Veuillez vérifier vos paramètres.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String validateEmail(String email) {
        while (!isValidEmailFormat(email)) {
            email = JOptionPane.showInputDialog(null, "L'adresse email saisie est invalide.\nVeuillez entrer une adresse email valide (exemple : nom@domaine.com) :", "Email Invalide", JOptionPane.WARNING_MESSAGE);
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("L'email est requis !");
            }
        }
        return email;
    }

    private boolean isValidEmailFormat(String email) {
        String emailRegex = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(emailRegex, email);
    }
    public void authenticateOrRegisterUser(String email, String password, String role) {
        try {
            // Valider le format de l'email
            email = validateEmail(email);

            switch (role.toLowerCase()) {
                case "demandeur":
                    if (!isDemandeur(email)) {
                        registerDemandeur(email, password);
                    }
                    Demandeur demandeur = getDemandeurByEmail(email);
                    SwingUtilities.invokeLater(() -> {
                        FrameDemandeur frameDemandeur = new FrameDemandeur(demandeur);
                        frameDemandeur.setVisible(true);
                    });
                    break;

                case "benevole":
                    if (!isBenevole(email)) {
                        registerBenevole(email, password);
                    }
                    Benevole benevole = getBenevoleByEmail(email);
                    SwingUtilities.invokeLater(() -> {
                        FrameBenevole frameBenevole = new FrameBenevole(AllMissions.getInstance(), benevole);
                        frameBenevole.setVisible(true);
                    });
                    break;

                case "validateur":
                    if (!isValidateur(email)) {
                        registerValidateur(email, password);
                    }
                    Validateur validateur = getValidateurByEmail(email);
                    SwingUtilities.invokeLater(() -> {
                        FrameValidateur frameValidateur = new FrameValidateur(AllMissions.getInstance(), validateur);
                        frameValidateur.setVisible(true);
                    });
                    break;

                default:
                    JOptionPane.showMessageDialog(null, "Rôle inconnu ! Merci de vérifier et réessayer.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Une erreur s'est produite pendant l'authentification ou l'enregistrement. Veuillez réessayer.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isDemandeur(String email) throws SQLException {
        String sql = "SELECT * FROM Demandeur WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private boolean isBenevole(String email) throws SQLException {
        String sql = "SELECT * FROM Benevole WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private boolean isValidateur(String email) throws SQLException {
        String sql = "SELECT * FROM Validateur WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void registerDemandeur(String email, String password) throws SQLException {
        String firstname = JOptionPane.showInputDialog("Prénom :");
        String lastname = JOptionPane.showInputDialog("Nom :");
        String description = JOptionPane.showInputDialog("Description :");
        String needs = JOptionPane.showInputDialog("Besoins :");
        String locationInput = JOptionPane.showInputDialog("Lieu (HOME, WORKPLACE, HOSPITAL, etc.) :");
        Place location = Place.valueOf(locationInput.toUpperCase());

        String sql = "INSERT INTO Demandeur (firstname, lastname, description, needs, location, email, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, firstname);
            stmt.setString(2, lastname);
            stmt.setString(3, description);
            stmt.setString(4, needs);
            stmt.setString(5, location.name());
            stmt.setString(6, email);
            stmt.setString(7, password);
            stmt.executeUpdate();
        }
    }

    private void registerBenevole(String email, String password) throws SQLException {
        String firstname = JOptionPane.showInputDialog("Prénom :");
        String lastname = JOptionPane.showInputDialog("Nom :");
        String metier = JOptionPane.showInputDialog("Métier :");

        String sql = "INSERT INTO Benevole (firstname, lastname, email, password, metier) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, firstname);
            stmt.setString(2, lastname);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.setString(5, metier);
            stmt.executeUpdate();
        }
    }

    private void registerValidateur(String email, String password) throws SQLException {
        String firstname = JOptionPane.showInputDialog("Prénom :");
        String lastname = JOptionPane.showInputDialog("Nom :");

        String sql = "INSERT INTO Validateur (firstname, lastname, email, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, firstname);
            stmt.setString(2, lastname);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.executeUpdate();
        }
    }

    private Demandeur getDemandeurByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM Demandeur WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Demandeur(
                            rs.getString("firstname"),
                            rs.getString("lastname"),
                            rs.getString("description"),
                            rs.getString("needs"),
                            Place.valueOf(rs.getString("location")),
                            rs.getString("email"),
                            rs.getString("password")
                    );
                }
            }
        }
        throw new SQLException("Demandeur non trouvé !");
    }

    private Benevole getBenevoleByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM Benevole WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Benevole(
                            rs.getString("firstname"),
                            rs.getString("lastname"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("metier")
                    );
                }
            }
        }
        throw new SQLException("Bénévole non trouvé !");
    }

    private Validateur getValidateurByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM Validateur WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Validateur(
                            rs.getString("firstname"),
                            rs.getString("lastname"),
                            rs.getString("email"),
                            rs.getString("password")

                    );
                }
            }
        }
        throw new SQLException("Bénévole non trouvé !");
    }
    // Méthodes Génériques
    public ResultSet search(String tableName, String columnName, String value) throws SQLException {
        String sql = "SELECT * FROM " + tableName + " WHERE " + columnName + " = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, value);
        return stmt.executeQuery();
    }

    public void updateField(String tableName, String columnName, String newValue, String conditionColumn, String conditionValue) throws SQLException {
        String sql = "UPDATE " + tableName + " SET " + columnName + " = ? WHERE " + conditionColumn + " = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, newValue);
        stmt.setString(2, conditionValue);
        int rowsUpdated = stmt.executeUpdate();
        if (rowsUpdated > 0) {
            JOptionPane.showMessageDialog(null, "Mise à jour effectuée avec succès !");
        } else {
            JOptionPane.showMessageDialog(null, "Aucune mise à jour n'a été effectuée.");
        }
    }

    public void delete(String tableName, String conditionColumn, String conditionValue) throws SQLException {
        String sql = "DELETE FROM " + tableName + " WHERE " + conditionColumn + " = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, conditionValue);
        int rowsDeleted = stmt.executeUpdate();
        if (rowsDeleted > 0) {
            JOptionPane.showMessageDialog(null, "Suppression réussie !");
        } else {
            JOptionPane.showMessageDialog(null, "Aucune ligne trouvée à supprimer.");
        }
    }

    public void listAll(String tableName) throws SQLException {
        String sql = "SELECT * FROM " + tableName;
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        StringBuilder result = new StringBuilder("Contenu de la table " + tableName + ":\n");
        while (rs.next()) {
            ResultSetMetaData metaData = rs.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                result.append(metaData.getColumnName(i)).append(": ").append(rs.getString(i)).append("\n");
            }
            result.append("-----------------------\n");
        }
        JOptionPane.showMessageDialog(null, result.toString(), "Liste des entrées", JOptionPane.INFORMATION_MESSAGE);
    }

    public boolean isDuplicate(String tableName, String columnName, String value) throws SQLException {
        String sql = "SELECT 1 FROM " + tableName + " WHERE " + columnName + " = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, value);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }

    public int countEntries(String tableName) throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM " + tableName;
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("total");
        }
        return 0;
    }

    public void exportToCSV(String tableName, String filePath) throws SQLException, IOException {
        String sql = "SELECT * FROM " + tableName;
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Écrire les en-têtes
            for (int i = 1; i <= columnCount; i++) {
                writer.write(metaData.getColumnName(i) + (i < columnCount ? "," : ""));
            }
            writer.newLine();

            // Écrire les données
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    writer.write(rs.getString(i) + (i < columnCount ? "," : ""));
                }
                writer.newLine();
            }
        }
        JOptionPane.showMessageDialog(null, "Exportation réussie vers : " + filePath, "Succès", JOptionPane.INFORMATION_MESSAGE);
    }
}
