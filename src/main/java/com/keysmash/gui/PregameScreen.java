package com.keysmash.gui;

import com.keysmash.database.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Represents the initial screen where players enter their username before starting the game.
 * This screen validates the username against the database to ensure uniqueness and
 * transitions to the game screen once a valid username is provided.
 */
public class PregameScreen extends JPanel {
    private final JTextField nameField;

    /**
     * Creates a new pregame screen with username input functionality.
     *
     * @param cardLayout The card layout manager used for screen transitions
     * @param mainPanel The main panel containing all game screens
     */
    public PregameScreen(CardLayout cardLayout, JPanel mainPanel) {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setOpaque(false);

        JLabel promptLabel = new JLabel("Enter your name to start:");
        promptLabel.setForeground(Color.WHITE);
        promptLabel.setFont(new Font("Monospaced", Font.PLAIN, 24));
        promptLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputPanel.add(promptLabel);

        inputPanel.add(Box.createVerticalStrut(20));

        nameField = new JTextField();
        nameField.setFont(new Font("Monospaced", Font.PLAIN, 24));
        nameField.setMaximumSize(new Dimension(200, 40));
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputPanel.add(nameField);

        inputPanel.add(Box.createVerticalStrut(20));

        JButton startButton = new JButton("Start Game");
        startButton.setPreferredSize(new Dimension(150, 40));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener((ActionEvent e) -> {
            String username = nameField.getText().trim();
            if (!username.isEmpty()) {
                DatabaseManager dbManager = new DatabaseManager();
                if (dbManager.isProfileExists(username)) {
                    JOptionPane.showMessageDialog(this, "This name is already taken. Please choose another.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    dbManager.createProfile(username);
                    String textToType = "Socrates (c. 470 – 399 BC) was a Greek philosopher from Athens who is credited as the founder of Western philosophy and as among the first moral philosophers of the ethical tradition of thought.";
                    GameScreen gameScreen = new GameScreen(textToType, username, cardLayout, mainPanel);
                    mainPanel.add(gameScreen, "GameScreen");
                    cardLayout.show(mainPanel, "GameScreen");
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
