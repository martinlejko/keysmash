package main.java.com.keysmash.gui;

import main.java.com.keysmash.database.DatabaseManager;

import javax.swing.*;
import java.awt.*;

public class EndScreen extends JPanel {
    private String playername; // Store player's name
    private CardLayout cardLayout; // Reference to CardLayout for navigation
    private JPanel mainPanel; // Reference to the main panel

    public EndScreen(String playername, CardLayout cardLayout, JPanel mainPanel) {
        this.playername = playername; // Set player's name
        this.cardLayout = cardLayout; // Set CardLayout
        this.mainPanel = mainPanel; // Set main panel
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // Create DatabaseManager instance to fetch the latest score
        DatabaseManager dbManager = new DatabaseManager();
        int[] latestScores = dbManager.getLatestScore(playername); // Fetch latest WPM and accuracy
        int wpm = latestScores[0];
        int accuracy = latestScores[1];

        // Display WPM and Accuracy
        JLabel wpmLabel = new JLabel("Your WPM: " + wpm);
        wpmLabel.setForeground(Color.WHITE);
        wpmLabel.setFont(new Font("Monospaced", Font.PLAIN, 24));
        wpmLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(wpmLabel, BorderLayout.NORTH);

        JLabel accuracyLabel = new JLabel("Your Accuracy: " + accuracy + "%");
        accuracyLabel.setForeground(Color.WHITE);
        accuracyLabel.setFont(new Font("Monospaced", Font.PLAIN, 24));
        accuracyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(accuracyLabel, BorderLayout.CENTER);

        // Buttons for restart and leaderboard
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        JButton restartButton = new JButton("Restart");
        JButton leaderboardButton = new JButton("Leaderboard");

        // Restart the game, passing the same username and a new text to type
        restartButton.addActionListener(e -> {
            GameScreen newGameScreen = new GameScreen("Your text to type here", playername, cardLayout, mainPanel); // Pass the username, cardLayout, and mainPanel
            mainPanel.add(newGameScreen, "GameScreen");
            cardLayout.show(mainPanel, "GameScreen");

            // Request focus for the new GameScreen
            newGameScreen.requestFocusInWindow(); // Set focus on the new game screen
        });

        // Go to the leaderboard
        leaderboardButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "LeaderboardScreen");
        });

        buttonPanel.add(restartButton);
        buttonPanel.add(leaderboardButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
