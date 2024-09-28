package main.java.com.keysmash.gui;

import main.java.com.keysmash.database.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PregameScreen extends JPanel {
    private JTextField nameField;

    public PregameScreen(CardLayout cardLayout, JPanel mainPanel) {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK); // Set background color

        // Create a panel to hold the input components
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setOpaque(false); // Make the panel transparent

        // Label for name input
        JLabel promptLabel = new JLabel("Enter your name to start:");
        promptLabel.setForeground(Color.WHITE); // Set text color
        promptLabel.setFont(new Font("Monospaced", Font.PLAIN, 24));
        promptLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputPanel.add(promptLabel);

        // Add some vertical space between components
        inputPanel.add(Box.createVerticalStrut(20));

        // Text field for name input with a preferred size
        nameField = new JTextField();
        nameField.setFont(new Font("Monospaced", Font.PLAIN, 24));
        nameField.setMaximumSize(new Dimension(200, 40)); // Set maximum size
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputPanel.add(nameField);

        // Add some vertical space between components
        inputPanel.add(Box.createVerticalStrut(20));

        // Start button with customized size
        JButton startButton = new JButton("Start Game");
        startButton.setPreferredSize(new Dimension(150, 40)); // Set preferred size for the button
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener((ActionEvent e) -> {
            String username = nameField.getText().trim();
            if (!username.isEmpty()) {
                // Check for duplicates in the database
                DatabaseManager dbManager = new DatabaseManager();
                if (dbManager.isProfileExists(username)) {
                    JOptionPane.showMessageDialog(this, "This name is already taken. Please choose another.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Store the username in the database
                    dbManager.createProfile(username);

                    String textToType = "Socrates (c. 470 – 399 BC) was a Greek philosopher from Athens who is credited as the founder of Western philosophy and as among the first moral philosophers of the ethical tradition of thought."; // Text to type in the game
                    GameScreen gameScreen = new GameScreen(textToType, username, cardLayout, mainPanel); // Pass username to GameScreen// Pass username to GameScreen
                    mainPanel.add(gameScreen, "GameScreen");
                    cardLayout.show(mainPanel, "GameScreen");

                    // Request focus for GameScreen to ensure it receives keyboard input
                    gameScreen.requestFocusInWindow();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid name.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        inputPanel.add(startButton);

        add(inputPanel, BorderLayout.CENTER);
    }
}
