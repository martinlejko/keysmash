package main.java.com.keysmash.gui;

import main.java.com.keysmash.database.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LeaderboardScreen extends JPanel {
    private DatabaseManager databaseManager;

    public LeaderboardScreen() {
        databaseManager = new DatabaseManager();
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK); // Set background color to black

        // Create and style the title label
        JLabel titleLabel = new JLabel("Leaderboard");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE); // Set text color to white
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Optional: Set font style and size
        add(titleLabel, BorderLayout.NORTH);

        // Fetch leaderboard data from the database
        List<String[]> leaderboardData = databaseManager.getLeaderboardData();

        // Create a JTextArea to display leaderboard data
        JTextArea leaderboardTextArea = new JTextArea();
        leaderboardTextArea.setEditable(false);
        leaderboardTextArea.setBackground(Color.DARK_GRAY); // Set background color for the text area
        leaderboardTextArea.setForeground(Color.WHITE); // Set text color to white
        StringBuilder sb = new StringBuilder();
        for (String[] row : leaderboardData) {
            sb.append(String.join(", ", row)).append("\n");
        }
        leaderboardTextArea.setText(sb.toString());

        add(new JScrollPane(leaderboardTextArea), BorderLayout.CENTER);

        // ButtonBuilder to create the "Back" button with the same style
        ButtonBuilder buttonBuilder = new ButtonBuilder();
        JButton backButton = buttonBuilder.createDefaultButton("Back", e -> {
            // Go back to the home screen
            CardLayout cardLayout = (CardLayout) getParent().getLayout();
            cardLayout.show(getParent(), "HomeScreen");
        });
        add(backButton, BorderLayout.SOUTH);
    }
}
