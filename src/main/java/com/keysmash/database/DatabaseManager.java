package main.java.com.keysmash.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.sql.DriverManager.getConnection;

public class DatabaseManager {
    private static final Logger logger = Logger.getLogger("DbManager");
    private static final String DB_URL = "jdbc:mysql://localhost/keysmash_db"; // MySQL connection URL
    private static final String DB_USER = "newuser";
    private static final String DB_PASSWORD = "password"; // Ensure your database name is 'keysmash'
    private Connection connection;

    public DatabaseManager() {
        logger.setLevel(Level.FINE);
        logger.info("Initializing database manager.");
        listAvailableDrivers();  // Check loaded drivers
        connect();
        if (connection != null) {
            createTables();
        } else {
            logger.severe("Failed to establish a database connection. Tables will not be created.");
        }
    }

    private void listAvailableDrivers() {
        logger.info("Available JDBC Drivers:");
        java.util.Enumeration<java.sql.Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            logger.info(drivers.nextElement().getClass().getName());
        }
    }

    private void connect() {
        try {
            connection = getConnection(DB_URL, DB_USER, DB_PASSWORD);
            logger.info("Connection to MySQL has been established.");
        } catch (SQLException e) {
            logger.severe("Connection failed: " + e.getMessage());
        }
    }

    private void createTables() {
        String profilesTable = "CREATE TABLE IF NOT EXISTS profiles (\n"
                + " id INT PRIMARY KEY AUTO_INCREMENT,\n"
                + " username VARCHAR(50) UNIQUE NOT NULL,\n" // Changed TEXT to VARCHAR with length
                + " created_at DATETIME DEFAULT CURRENT_TIMESTAMP\n"
                + ");";

        String textsTable = "CREATE TABLE IF NOT EXISTS texts (\n"
                + " id INT PRIMARY KEY AUTO_INCREMENT,\n"
                + " content TEXT NOT NULL,\n"
                + " created_at DATETIME DEFAULT CURRENT_TIMESTAMP\n"
                + ");";

        String scoresTable = "CREATE TABLE IF NOT EXISTS scores (\n"
                + " id INT PRIMARY KEY AUTO_INCREMENT,\n"
                + " profile_id INT NOT NULL,\n"
                + " text_id INT NOT NULL,\n"
                + " speed DOUBLE NOT NULL,\n"  // Using DOUBLE for more precision
                + " error_percentage DOUBLE NOT NULL,\n" // Percentage of errors
                + " created_at DATETIME DEFAULT CURRENT_TIMESTAMP,\n"
                + " FOREIGN KEY (profile_id) REFERENCES profiles(id),\n"
                + " FOREIGN KEY (text_id) REFERENCES texts(id)\n"
                + ");";

        String leaderboardsTable = "CREATE TABLE IF NOT EXISTS leaderboards (\n"
                + " id INT PRIMARY KEY AUTO_INCREMENT,\n"
                + " text_id INT NOT NULL,\n"
                + " profile_id INT NOT NULL,\n"
                + " speed DOUBLE NOT NULL,\n"  // Using DOUBLE for more precision
                + " error_percentage DOUBLE NOT NULL,\n" // Percentage of errors
                + " created_at DATETIME DEFAULT CURRENT_TIMESTAMP,\n"
                + " FOREIGN KEY (text_id) REFERENCES texts(id),\n"
                + " FOREIGN KEY (profile_id) REFERENCES profiles(id)\n"
                + ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(profilesTable);
            stmt.execute(textsTable);
            stmt.execute(scoresTable);
            stmt.execute(leaderboardsTable);
            logger.fine("Tables created successfully.");
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
                logger.info("Connection to MySQL closed.");
            }
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
    }

    // Method for creating a profile
    public void createProfile(String username) {
        String sql = "INSERT INTO profiles(username) VALUES(?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
            logger.fine("Profile created for user: " + username);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
    }

    // Method for adding text
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

    public void addScore(int profileId, int textId, double speed, double errorPercentage) {
        String sql = "INSERT INTO scores(profile_id, text_id, speed, error_percentage) VALUES(?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, profileId);
            pstmt.setInt(2, textId);
            pstmt.setDouble(3, speed);
            pstmt.setDouble(4, errorPercentage);
            pstmt.executeUpdate();
            logger.fine("Score added to the database.");
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
    }

    // Method for adding speed and error percentage to leaderboards
    public void addToLeaderboard(int textId, int profileId, double speed, double errorPercentage) {
        String sql = "INSERT INTO leaderboards(text_id, profile_id, speed, error_percentage) VALUES(?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, textId);
            pstmt.setInt(2, profileId);
            pstmt.setDouble(3, speed);
            pstmt.setDouble(4, errorPercentage);
            pstmt.executeUpdate();
            logger.fine("Score added to the leaderboard.");
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
    }

    public List<String[]> getLeaderboardData() {
        String sql = "SELECT profiles.username, MAX(leaderboards.speed) AS best_speed, MIN(leaderboards.error_percentage) AS best_error_percentage " +
                "FROM leaderboards " +
                "JOIN profiles ON leaderboards.profile_id = profiles.id " +
                "GROUP BY profiles.username " +
                "ORDER BY best_speed DESC";

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

    public void populateDummyData() {
        try {
            // Create some dummy profiles
            createProfile("Player1");
            createProfile("Player2");
            createProfile("Player3");

            // Create some dummy texts
            addText("This is the first typing test text.");
            addText("The second typing test text is a bit longer.");
            addText("Another sample text for the typing test.");

            // Fetch the IDs of the inserted profiles and texts
            int player1Id = getProfileIdByUsername("Player1");
            int player2Id = getProfileIdByUsername("Player2");
            int player3Id = getProfileIdByUsername("Player3");

            int text1Id = getTextIdByContent("This is the first typing test text.");
            int text2Id = getTextIdByContent("The second typing test text is a bit longer.");
            int text3Id = getTextIdByContent("Another sample text for the typing test.");

            // Add some scores for the players
            addScore(player1Id, text1Id, 60.5, 2.0);  // Player1's score
            addScore(player2Id, text2Id, 70.3, 1.5);  // Player2's score
            addScore(player3Id, text3Id, 80.2, 1.0);  // Player3's score

            // Add to the leaderboard as well
            addToLeaderboard(text1Id, player1Id, 60.5, 2.0);
            addToLeaderboard(text2Id, player2Id, 70.3, 1.5);
            addToLeaderboard(text3Id, player3Id, 80.2, 1.0);

            logger.info("Dummy data populated successfully.");

        } catch (SQLException e) {
            logger.severe("Failed to populate dummy data: " + e.getMessage());
        }
    }

    // Helper method to get profile ID by username
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

    public boolean isProfileExists(String username) {
        String query = "SELECT COUNT(*) FROM profiles WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void storeScore(String username, int wpm, int accuracy) {
        String query = "INSERT INTO scores (username, wpm, accuracy) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setInt(2, wpm);
            pstmt.setInt(3, accuracy);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int[] getLatestScore(String username) {
        int[] scores = new int[2]; // scores[0] = WPM, scores[1] = Accuracy
        String query = "SELECT wpm, accuracy FROM scores WHERE username = ? ORDER BY timestamp DESC LIMIT 1";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                scores[0] = rs.getInt("wpm");
                scores[1] = rs.getInt("accuracy");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle exception
        }
        return scores; // Return the latest WPM and accuracy
    }
}
