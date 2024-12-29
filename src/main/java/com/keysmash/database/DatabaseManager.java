package com.keysmash.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Manages the game's H2 database operations including player profiles, scores, and leaderboards.
 * Uses an H2 database in MySQL compatibility mode with auto-server enabled for data persistence.
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
     * Initializes the database connection and creates required tables.
     * Creates the data directory if it doesn't exist.
     * 
     * @throws RuntimeException if database initialization fails
     */
    public DatabaseManager() {
        logger.setLevel(Level.FINE);
        logger.info("Initializing H2 database manager.");
        
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
                populateDummyData();
            }
        } catch (SQLException e) {
            logger.severe("Failed to initialize the database: " + e.getMessage());
            throw new RuntimeException("Failed to initialize the database", e);
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
     * Creates a new player profile if the username is not already taken.
     *
     * @param username the player's chosen username
     * @throws RuntimeException if profile creation fails
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
     * Adds a new score entry to the scores table for a specific profile.
     *
     * @param profileId the ID of the profile
     * @param speed the typing speed of the profile
     * @param errorPercentage the error percentage of the profile's typing
     */
    public void addScore(int profileId, double speed, double errorPercentage) {
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
     * Adds a score entry to the leaderboard for a specific profile.
     *
     * @param profileId the ID of the profile
     * @param speed the typing speed of the profile
     * @param errorPercentage the error percentage of the profile's typing
     */
    public void addToLeaderboard(int profileId, double speed, double errorPercentage) {
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
     * Retrieves the top 10 players sorted by typing speed and accuracy.
     * Each entry contains: rank, username, speed, and error percentage.
     *
     * @return List of String arrays containing leaderboard entries
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
     * Populates the database with dummy data for testing purposes, including profiles.
     */
    public void populateDummyData() {
        try {
            createProfile("Player1");
            createProfile("Player2");
            createProfile("Player3");

            int player1Id = getProfileIdByUsername("Player1");
            int player2Id = getProfileIdByUsername("Player2");
            int player3Id = getProfileIdByUsername("Player3");

            addScore(player1Id,60.5, 2.0);
            addScore(player2Id,70.3, 1.5);
            addScore(player3Id,55.8, 3.2);

            addToLeaderboard(player1Id, 60.5, 2.0);
            addToLeaderboard(player2Id, 70.3, 1.5);
            addToLeaderboard(player3Id, 55.8, 3.2);

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
     * Checks if a username is already registered in the database.
     *
     * @param username the username to check
     * @return true if the username exists, false otherwise
     * @throws RuntimeException if the database query fails
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
     * Stores a player's typing test results.
     *
     * @param username the player's username
     * @param wpm words per minute achieved
     * @param accuracy typing accuracy percentage
     * @throws RuntimeException if storing the score fails
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
     * Gets the most recent typing test results for a player.
     *
     * @param username the player's username
     * @return int array containing [wpm, accuracy]
     * @throws RuntimeException if retrieving the score fails
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

    /**
     * Closes the database connection.
     * Should be called when the application is shutting down.
     *
     * @throws RuntimeException if closing the connection fails
     */
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