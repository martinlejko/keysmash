package main.java.com.keysmash.gui;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel; // Main panel that will hold different screens
    private GameScreen gameScreen; // Store a reference to GameScreen

    public MainWindow() {
        setTitle("Typing Test");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(Color.BLACK); // Set the main panel background to black

        initializeComponents();

        getContentPane().add(mainPanel);
        getContentPane().setBackground(Color.BLACK); // Set the main window background to black

        cardLayout.show(mainPanel, "HomeScreen");
    }

    private void initializeComponents() {
        // Create the home screen panel
        JPanel homePanel = new JPanel(new GridBagLayout());
        homePanel.setBackground(Color.BLACK); // Set background to black
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 10, 20, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create the explosion label
        ExplosionLabel explosionLabel = new ExplosionLabel("Keysmash");
        explosionLabel.setForeground(Color.WHITE); // Set label text color to white
        homePanel.add(explosionLabel, gbc);

        // ButtonBuilder class to create buttons
        ButtonBuilder buttonBuilder = new ButtonBuilder();

        // Start button - switches to game screen
        JButton startButton = buttonBuilder.createDefaultButton("Start", e -> {
            cardLayout.show(mainPanel, "GameScreen"); // Switch to GameScreen
            gameScreen.requestFocusInWindow(); // Request focus when showing GameScreen
        });

        // Help button - switches to help screen
        JButton helpButton = buttonBuilder.createDefaultButton("Help", e -> {
            cardLayout.show(mainPanel, "HelpScreen"); // Switch to HelpScreen
        });

        // Leaderboard button - switches to leaderboard screen
        JButton leaderboardButton = buttonBuilder.createDefaultButton("Leaderboard", e -> {
            cardLayout.show(mainPanel, "LeaderboardScreen"); // Switch to LeaderboardScreen
        });

        // Add buttons to the home panel
        gbc.gridy = 1;
        homePanel.add(startButton, gbc);
        gbc.gridy = 2;
        homePanel.add(leaderboardButton, gbc);
        gbc.gridy = 3;
        homePanel.add(helpButton, gbc);

        // Add all the screens (panels) to the main panel using CardLayout
        mainPanel.add(homePanel, "HomeScreen");

        // Set background color to black for other screens too
        HelpScreen helpScreen = new HelpScreen();
        helpScreen.setBackground(Color.BLACK); // Set HelpScreen background to black
        mainPanel.add(helpScreen, "HelpScreen");

        LeaderboardScreen leaderboardScreen = new LeaderboardScreen();
        leaderboardScreen.setBackground(Color.BLACK); // Set LeaderboardScreen background to black
        mainPanel.add(leaderboardScreen, "LeaderboardScreen");

        // Initialize the GameScreen and set focus handling
        gameScreen = new GameScreen("Type this text");
        gameScreen.setBackground(Color.BLACK); // Set GameScreen background to black
        mainPanel.add(gameScreen, "GameScreen");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}
