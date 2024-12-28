package com.keysmash.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.sql.DriverManager.getConnection;

/**
 * DatabaseManager class handles the database connection and operations for the Keysmash application.
 * It is responsible for initializing the database, creating tables, and executing various CRUD operations
 * related to profiles, texts, scores, and leaderboards.
 */
public class DatabaseManager {
    private static final Logger logger = Logger.getLogger("DbManager");
    private static final String DB_URL = "jdbc:h2:./data/keysmash_db;MODE=MYSQL;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    private Connection connection;
    private static final String INSERT_SCORE = """
        INSERT INTO scores (profile_id, speed, error_percentage) 
        VALUES (?, ?, ?)
    """;

    /**
     * Constructs a DatabaseManager instance and initializes the database connection.
     * Lists available JDBC drivers and creates necessary tables if the connection is successful.
     */
    public DatabaseManager() {
        logger.setLevel(Level.FINE);
        logger.info("Initializing H2 database manager.");
        
        // Create data directory if it doesn't exist
        try {
            Files.createDirectories(Paths.get("./data"));
        } catch (IOException e) {
            logger.severe("Failed to create data directory: " + e.getMessage());
            throw new RuntimeException("Failed to create data directory", e);
        }

        try {
            connect();
            if (connection != null) {
                createTables();
            }
        } catch (SQLException e) {
            logger.severe("Failed to initialize the database: " + e.getMessage());
            throw new RuntimeException("Failed to initialize the database", e);
        }
    }

    /**
     * Logs the available JDBC drivers currently registered with the DriverManager.
     */
    private void listAvailableDrivers() {
        logger.info("Available JDBC Drivers:");
        java.util.Enumeration<java.sql.Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            logger.info(drivers.nextElement().getClass().getName());
        }
    }

    /**
     * Establishes a connection to the MySQL database using the provided URL, username, and password.
     */
    private void connect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            return;
        }

        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    /**
     * Creates the necessary tables for profiles, texts, scores, and leaderboards in the database.
     */
    private void createTables() {
        String profilesTable = """
                CREATE TABLE IF NOT EXISTS profiles (
                 id INT AUTO_INCREMENT PRIMARY KEY,
                 username VARCHAR(255) NOT NULL,
                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                );""";

        String textsTable = """
                CREATE TABLE IF NOT EXISTS texts (
                 id INT AUTO_INCREMENT PRIMARY KEY,
                 content TEXT NOT NULL,
                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                );""";

        String scoresTable = """
            CREATE TABLE IF NOT EXISTS scores (
                id INT AUTO_INCREMENT PRIMARY KEY,
                profile_id INT NOT NULL,
                speed DOUBLE NOT NULL,
                error_percentage DOUBLE NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (profile_id) REFERENCES profiles(id)
            );""";

        String leaderboardsTable = """
            CREATE TABLE IF NOT EXISTS leaderboards (
             id INT AUTO_INCREMENT PRIMARY KEY,
             profile_id INT NOT NULL,
             speed DOUBLE NOT NULL,
             error_percentage DOUBLE NOT NULL,
             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
             FOREIGN KEY (profile_id) REFERENCES profiles(id)
            );""";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(profilesTable);
            stmt.execute(textsTable);
            stmt.execute(scoresTable);
            stmt.execute(leaderboardsTable);
            logger.fine("Tables created successfully.");
        } catch (SQLException e) {
            logger.severe("Failed to create tables: " + e.getMessage());
            throw new RuntimeException("Failed to create database tables", e);
        }
    }

    /**
     * Closes the database connection if it is currently open.
     */
    public void close() {
        try {
            if (connection != null) {
                connection.close();
                logger.info("Connection to H2 database closed.");
            }
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
    }

    /**
     * Creates a new profile in the profiles table with the specified username.
     *
     * @param username the username of the new profile
     */
    public void createProfile(String username) {
        String sql = "INSERT INTO profiles(username) VALUES(?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
            logger.fine("Profile created for user: " + username);
        } catch (SQLException e) {
            logger.severe("Failed to create profile: " + e.getMessage());
            throw new RuntimeException("Failed to create profile", e);
        }
    }

    /**
     * Adds a new text entry to the texts table.
     *
     * @param content the content of the text to be added
     */
    public void addText(String content) {
        String sql = "INSERT INTO texts(content) VALUES(?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, content);
            pstmt.executeUpdate();
            logger.fine("Text added to the database.");
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
    }

    /**
     * Adds a new score entry to the scores table for a specific profile and text.
     *
     * @param profileId the ID of the profile
     * @param textId the ID of the text
     * @param speed the typing speed of the profile
     * @param errorPercentage the error percentage of the profile's typing
     */
    public void addScore(int profileId, int textId, double speed, double errorPercentage) {
        String sql = "INSERT INTO scores(profile_id, speed, error_percentage) VALUES(?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, profileId);
            pstmt.setDouble(2, speed);
            pstmt.setDouble(3, errorPercentage);
            pstmt.executeUpdate();
            logger.fine("Score added to the database.");
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
    }

    /**
     * Adds a score entry to the leaderboard for a specific profile and text.
     *
     * @param textId the ID of the text
     * @param profileId the ID of the profile
     * @param speed the typing speed of the profile
     * @param errorPercentage the error percentage of the profile's typing
     */
    public void addToLeaderboard(int textId, int profileId, double speed, double errorPercentage) {
        String sql = "INSERT INTO leaderboards(profile_id, speed, error_percentage) VALUES(?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, profileId);
            pstmt.setDouble(2, speed);
            pstmt.setDouble(3, errorPercentage);
            pstmt.executeUpdate();
            logger.fine("Score added to the leaderboard.");
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
    }

    /**
     * Retrieves the leaderboard data, including usernames and their best speed and error percentages.
     * The top 10 entries are returned, sorted by speed and error percentage.
     *
     * @return a list of string arrays containing leaderboard data
     */
    public List<String[]> getLeaderboardData() {
        String sql = """
        SELECT profiles.username, MAX(scores.speed) AS best_speed, MAX(scores.error_percentage) AS best_error_percentage
        FROM scores
        JOIN profiles ON scores.profile_id = profiles.id
        GROUP BY profiles.username
        ORDER BY best_speed, best_error_percentage DESC
        LIMIT 10;
    """;

        List<String[]> leaderboardData = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            int rank = 1;
            while (rs.next()) {
                String username = rs.getString("username");
                String speed = String.valueOf(rs.getDouble("best_speed"));
                String errors = String.valueOf(rs.getDouble("best_error_percentage"));
                leaderboardData.add(new String[]{String.valueOf(rank++), username, speed, errors});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return leaderboardData;
    }

    /**
     * Populates the database with dummy data for testing purposes, including profiles and texts.
     */
    public void populateDummyData() {
        try {
            createProfile("Player1");
            createProfile("Player2");
            createProfile("Player3");

            addText("This is the first typing test text.");
            addText("The second typing test text is a bit longer.");
            addText("Another sample text for the typing test.");

            int player1Id = getProfileIdByUsername("Player1");
            int player2Id = getProfileIdByUsername("Player2");
            int player3Id = getProfileIdByUsername("Player3");

            int text1Id = getTextIdByContent("This is the first typing test text.");
            int text2Id = getTextIdByContent("The second typing test text is a bit longer.");
            int text3Id = getTextIdByContent("Another sample text for the typing test.");

            addScore(player1Id, text1Id, 60.5, 2.0);
            addScore(player2Id, text2Id, 70.3, 1.5);
            addScore(player3Id, text3Id, 55.8, 3.2);

            addToLeaderboard(text1Id, player1Id, 60.5, 2.0);
            addToLeaderboard(text2Id, player2Id, 70.3, 1.5);
            addToLeaderboard(text3Id, player3Id, 55.8, 3.2);

            logger.info("Dummy data populated successfully.");

        } catch (SQLException e) {
            logger.severe("Failed to populate dummy data: " + e.getMessage());
        }
    }

    /**
     * Retrieves the profile ID for a given username.
     *
     * @param username the username of the profile to be retrieved
     * @return the ID of the profile, or -1 if not found
     */
    private int getProfileIdByUsername(String username) throws SQLException {
        String sql = "SELECT id FROM profiles WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        throw new SQLException("Profile not found: " + username);
    }

    /**
     * Retrieves the text ID for a given text content.
     *
     * @param content the content of the text to be retrieved
     * @return the ID of the text, or -1 if not found
     */
    private int getTextIdByContent(String content) throws SQLException {
        String sql = "SELECT id FROM texts WHERE content = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, content);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        throw new SQLException("Text not found: " + content);
    }

    /**
     * Checks if a profile with the given username exists in the database.
     *
     * @param username the username to check for existence
     * @return true if the profile exists, false otherwise
     */
    public boolean isProfileExists(String username) {
        String query = "SELECT COUNT(*) FROM profiles WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            logger.severe("Failed to check profile existence: " + e.getMessage());
            throw new RuntimeException("Failed to check profile existence", e);
        }
        return false;
    }

    /**
     * Stores a score for a given username in the database.
     *
     * @param username the username of the profile
     * @param wpm the words per minute score
     * @param accuracy the accuracy percentage
     */
    public void storeScore(String username, int wpm, int accuracy) {
        int profileId = 0;
        try {
            profileId = getProfileIdByUsername(username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try (PreparedStatement pstmt = connection.prepareStatement(INSERT_SCORE)) {
            pstmt.setInt(1, profileId);
            pstmt.setDouble(2, wpm);
            pstmt.setDouble(3, accuracy);
            pstmt.executeUpdate();
            logger.fine("Score stored successfully for user: " + username);
        } catch (SQLException e){
        logger.severe("Failed to store score: " + e.getMessage());
        throw new RuntimeException("Failed to store score", e);
    }
}

    /**
     * Retrieves the latest score for a given username.
     *
     * @param username the username of the profile
     * @return an array containing the latest speed and error percentage
     */
    public int[] getLatestScore(String username) {
        int[] scores = new int[2];
        String query = "SELECT speed, error_percentage FROM scores WHERE profile_id = ? ORDER BY created_at DESC LIMIT 1";

        try {
            int profileId = getProfileIdByUsername(username);
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, profileId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    scores[0] = rs.getInt("speed");
                    scores[1] = rs.getInt("error_percentage");
                }
            }
        } catch (SQLException e) {
            logger.severe("Failed to get latest score: " + e.getMessage());
            throw new RuntimeException("Failed to get latest score", e);
        }
        return scores;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                logger.info("Database connection closed successfully");
            } catch (SQLException e) {
                logger.severe("Failed to close database connection: " + e.getMessage());
                throw new RuntimeException("Failed to close database connection", e);
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        closeConnection();
        super.finalize();
    }
}