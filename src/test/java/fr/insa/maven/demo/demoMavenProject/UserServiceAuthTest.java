package fr.insa.maven.demo.demoMavenProject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceAuthTest {

    private Connection conn;
    private UserService userService;

    private static final String DB_URL = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/projet_gei_012";
    private static final String USER = "projet_gei_012";
    private static final String PASS = "dith1Que";

    @BeforeEach
    public void setUp() throws SQLException {
        // Debug message to ensure this is executed
        System.out.println("Setting up test environment...");

        // Establish database connection
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        if (conn == null) {
            throw new SQLException("Connection failed: conn is null");
        }
        System.out.println("Database connection established: " + conn);

        // Clean the User table
        try (PreparedStatement clearUserTable = conn.prepareStatement("DELETE FROM User")) {
            clearUserTable.executeUpdate();
        }

        // Initialize user service
        userService = new UserService(conn);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (conn != null) {
            conn.close();
            System.out.println("Database connection closed.");
        }
    }

    @Test
    public void testAuthenticateUserSuccess() throws SQLException {
        try (PreparedStatement insertUserStmt = conn.prepareStatement(
                "INSERT INTO User (firstname, lastname, email, password) VALUES (?, ?, ?, ?)"
        )) {
            insertUserStmt.setString(1, "John");
            insertUserStmt.setString(2, "Doe");
            insertUserStmt.setString(3, "john.doe@example.com");
            insertUserStmt.setString(4, "password123");
            insertUserStmt.executeUpdate();
        }

        User user = userService.authenticateUser("john.doe@example.com", "password123");

        assertNotNull(user, "User should not be null on successful authentication");
        assertEquals("John", user.getFirstname());
        assertEquals("Doe", user.getLastname());
    }

    @Test
    public void testAuthenticateUserFailure() throws SQLException {
        try (PreparedStatement insertUserStmt = conn.prepareStatement(
                "INSERT INTO User (firstname, lastname, email, password) VALUES (?, ?, ?, ?)"
        )) {
            insertUserStmt.setString(1, "Jane");
            insertUserStmt.setString(2, "Doe");
            insertUserStmt.setString(3, "jane.doe@example.com");
            insertUserStmt.setString(4, "password123");
            insertUserStmt.executeUpdate();
        }

        User user = userService.authenticateUser("jane.doe@example.com", "wrongpassword");

        assertNull(user, "User should be null on failed authentication");
    }

    @Test
    public void testConnection() throws SQLException {
        assertNotNull(conn, "Database connection should not be null");
        System.out.println("Test connection succeeded: " + conn);
    }
}
