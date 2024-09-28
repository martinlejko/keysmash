package main.java.com.keysmash.gui;

import javax.swing.*;
import java.awt.*;

public class EndScreen extends JPanel {
    public EndScreen(int wpm, int accuracy, CardLayout cardLayout, JPanel mainPanel) {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

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

        // Restart the game (assuming textToType is passed again to GameScreen)
        restartButton.addActionListener(e -> {
            GameScreen newGameScreen = new GameScreen("Your text to type here");
            mainPanel.add(newGameScreen, "GameScreen");
            cardLayout.show(mainPanel, "GameScreen");
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
