package com.keysmash.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Main application window that manages navigation between different game screens.
 * Contains the home screen with game options and handles transitions between screens.
 */
public class MainWindow extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    /**
     * Creates the main window with a black background and initializes all game screens:
     * home screen, pregame screen, help screen, and leaderboard screen.
     */
    public MainWindow() {
        setTitle("Typing Test");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(Color.BLACK);

        initializeComponents();

        getContentPane().add(mainPanel);
        getContentPane().setBackground(Color.BLACK);

        cardLayout.show(mainPanel, "HomeScreen");
    }

    /**
     * Sets up the home screen with the game logo and navigation buttons:
     * Start, Leaderboard, Help, and Exit.
     */
    private void initializeComponents() {
        JPanel homePanel = new JPanel(new GridBagLayout());
        homePanel.setBackground(Color.BLACK);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 10, 20, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        ExplosionLabel explosionLabel = new ExplosionLabel("Keysmash");
        explosionLabel.setForeground(Color.WHITE);
        homePanel.add(explosionLabel, gbc);

        ButtonBuilder buttonBuilder = new ButtonBuilder();

        JButton startButton = buttonBuilder.createDefaultButton("Start", e -> cardLayout.show(mainPanel, "PregameScreen"));

        JButton helpButton = buttonBuilder.createDefaultButton("Help", e -> cardLayout.show(mainPanel, "HelpScreen"));

        JButton leaderboardButton = buttonBuilder.createDefaultButton("Leaderboard", e -> cardLayout.show(mainPanel, "LeaderboardScreen"));

        JButton exitButton = buttonBuilder.createDefaultButton("Exit", e -> System.exit(0));

        gbc.gridy = 1;
        homePanel.add(startButton, gbc);
        gbc.gridy = 2;
        homePanel.add(leaderboardButton, gbc);
        gbc.gridy = 3;
        homePanel.add(helpButton, gbc);
        gbc.gridy = 4;
        homePanel.add(exitButton, gbc);

        mainPanel.add(homePanel, "HomeScreen");

        HelpScreen helpScreen = new HelpScreen();
        helpScreen.setBackground(Color.BLACK);
        mainPanel.add(helpScreen, "HelpScreen");

        LeaderboardScreen leaderboardScreen = new LeaderboardScreen();
        leaderboardScreen.setBackground(Color.BLACK);
        mainPanel.add(leaderboardScreen, "LeaderboardScreen");

        PregameScreen pregameScreen = new PregameScreen(cardLayout, mainPanel);
        mainPanel.add(pregameScreen, "PregameScreen");
    }
}