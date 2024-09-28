package main.java.com.keysmash.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PregameScreen extends JPanel {
    private JTextField nameField;

    public PregameScreen(CardLayout cardLayout, JPanel mainPanel) {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK); // Set background color

        // Label for name input
        JLabel promptLabel = new JLabel("Enter your name to start:");
        promptLabel.setForeground(Color.WHITE); // Set text color
        promptLabel.setFont(new Font("Monospaced", Font.PLAIN, 24));
        promptLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(promptLabel, BorderLayout.NORTH);

        // Text field for name input
        nameField = new JTextField();
        nameField.setFont(new Font("Monospaced", Font.PLAIN, 24));
        add(nameField, BorderLayout.CENTER);

        // Start button
        JButton startButton = new JButton("Start Game");
        startButton.addActionListener((ActionEvent e) -> {
            String username = nameField.getText().trim();
            if (!username.isEmpty()) {
                // Store the username in the database
                // DatabaseManager dbManager = new DatabaseManager();
                // dbManager.createProfile(username);

                // Now switch to the GameScreen
                GameScreen gameScreen = new GameScreen("Your text to type here"); // Change the text as needed
                mainPanel.add(gameScreen, "GameScreen");
                cardLayout.show(mainPanel, "GameScreen");

                // Request focus for GameScreen to ensure it receives keyboard input
                gameScreen.requestFocusInWindow();
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid name.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(startButton, BorderLayout.SOUTH);
    }
}
