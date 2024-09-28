package main.java.com.keysmash.gui;

import main.java.com.keysmash.database.DatabaseManager;

import javax.swing.*;
import java.awt.*;

/**
 * The EndScreen class represents the end screen of the typing test game.
 * It displays the player's latest score and provides options to restart the game or view the leaderboard.
 */
public class EndScreen extends JPanel {
    private String playername;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    /**
     * Constructs an EndScreen object.
     *
     * @param playername the name of the player
     * @param cardLayout the CardLayout used to switch between different screens
     * @param mainPanel the main panel containing all the screens
     */
    public EndScreen(String playername, CardLayout cardLayout, JPanel mainPanel) {
        this.playername = playername;
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        DatabaseManager dbManager = new DatabaseManager();
        int[] latestScores = dbManager.getLatestScore(playername);
        int wpm = latestScores[0];
        int accuracy = latestScores[1];

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

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        JButton restartButton = new JButton("Restart");
        JButton leaderboardButton = new JButton("Leaderboard");

        restartButton.addActionListener(e -> {
            GameScreen newGameScreen = new GameScreen("Your text to type here", playername, cardLayout, mainPanel);
            mainPanel.add(newGameScreen, "GameScreen");
            cardLayout.show(mainPanel, "GameScreen");
            newGameScreen.requestFocusInWindow();
        });

        leaderboardButton.addActionListener(e -> cardLayout.show(mainPanel, "LeaderboardScreen"));

        buttonPanel.add(restartButton);
        buttonPanel.add(leaderboardButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}