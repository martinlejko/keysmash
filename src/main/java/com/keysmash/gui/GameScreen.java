package main.java.com.keysmash.gui;

import main.java.com.keysmash.database.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

public class GameScreen extends JPanel {
    private String textToType;
    private String userInput = "";
    private long startTime;
    private Timer timer;
    private JLabel displayLabel;
    private JLabel wpmLabel;
    private JLabel accuracyLabel;
    private int correctCharacters = 0;
    private String username; // Store username for score tracking
    private CardLayout cardLayout; // CardLayout for navigating between screens
    private JPanel mainPanel; // Main panel to hold different screens

    public GameScreen(String text, String username, CardLayout cardLayout, JPanel mainPanel) {
        this.textToType = text;
        this.username = username; // Set username
        this.cardLayout = cardLayout; // Set card layout
        this.mainPanel = mainPanel; // Set main panel
        initializeComponents();
        startGame();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // Label to display the text to type
        displayLabel = new JLabel();
        displayLabel.setFont(new Font("Monospaced", Font.PLAIN, 24));
        displayLabel.setForeground(Color.WHITE);
        displayLabel.setHorizontalAlignment(SwingConstants.CENTER);
        displayLabel.setText(getFormattedText()); // Initialize with formatted text
        add(displayLabel, BorderLayout.CENTER);

        // WPM and Accuracy labels
        JPanel statsPanel = new JPanel();
        statsPanel.setBackground(Color.BLACK);
        wpmLabel = new JLabel("WPM: 0");
        wpmLabel.setForeground(Color.WHITE);
        accuracyLabel = new JLabel("Accuracy: 100%");
        accuracyLabel.setForeground(Color.WHITE);
        statsPanel.add(wpmLabel);
        statsPanel.add(accuracyLabel);
        add(statsPanel, BorderLayout.SOUTH);

        // Key listener for typing input
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                handleKeyTyped(e.getKeyChar());
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    handleBackspace();
                }
            }
        });
        setFocusable(true);
        requestFocusInWindow(); // Ensure this panel is focused
    }

    private void startGame() {
        startTime = System.currentTimeMillis();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateWPM();
            }
        }, 60000, 60000); // Update WPM every minute
    }

    private void handleKeyTyped(char keyChar) {
        if (userInput.length() < textToType.length()) {
            userInput += keyChar;
            checkInput();
            updateDisplay();

            // Stop the game when the last character is typed
            if (userInput.length() == textToType.length()) {
                endGame();
            }
        }
    }

    private void handleBackspace() {
        if (!userInput.isEmpty()) {
            userInput = userInput.substring(0, userInput.length() - 1);
            checkInput(); // Recheck input after deletion
            updateDisplay();
        }
    }

    private void checkInput() {
        correctCharacters = 0; // Reset correct characters for recalculation

        // Iterate through userInput and textToType to count correct characters
        for (int i = 0; i < userInput.length(); i++) {
            if (i < textToType.length() && userInput.charAt(i) == textToType.charAt(i)) {
                correctCharacters++;
            }
        }

        updateAccuracy();
    }

    private void updateDisplay() {
        StringBuilder displayText = new StringBuilder("<html>");
        for (int i = 0; i < textToType.length(); i++) {
            char expectedChar = textToType.charAt(i);
            if (i < userInput.length()) {
                char typedChar = userInput.charAt(i);
                if (typedChar == expectedChar) {
                    displayText.append("<font color='green'>").append(expectedChar).append("</font>");
                } else {
                    displayText.append("<font color='red'>").append(expectedChar).append("</font>");
                }
            } else {
                // Highlight the next character with an underscore
                if (i == userInput.length()) {
                    displayText.append("<u><font color='white'>").append(expectedChar).append("</font></u>");
                } else {
                    displayText.append(expectedChar);
                }
            }
        }
        displayText.append("</html>");
        displayLabel.setText(displayText.toString()); // Update label with formatted text
    }

    private void updateWPM() {
        long elapsedTime = System.currentTimeMillis() - startTime; // Time in milliseconds
        int minutes = (int) (elapsedTime / 60000);
        int wpm = (userInput.length() / 5) / (minutes + 1);
        wpmLabel.setText("WPM: " + wpm);
    }

    private void updateAccuracy() {
        int totalChars = Math.max(userInput.length(), textToType.length());
        int accuracy = (int) ((correctCharacters * 100.0) / totalChars);
        accuracyLabel.setText("Accuracy: " + accuracy + "%");
    }

    private String getFormattedText() {
        return "<html>" + textToType.replace("\n", "<br>") + "</html>";
    }

    // Ends the game, calculates WPM, accuracy, and stops the timer
    private void endGame() {
        timer.cancel(); // Stop the timer
        updateWPM(); // Final WPM update
        updateAccuracy(); // Final accuracy update

        // Store the score in the database
        DatabaseManager dbManager = new DatabaseManager();
        dbManager.storeScore(username, Integer.parseInt(wpmLabel.getText().split(": ")[1]), Integer.parseInt(accuracyLabel.getText().split(": ")[1].replace("%", ""))); // Store current score

        // Show the EndScreen with latest score
        EndScreen endScreen = new EndScreen(username, cardLayout, mainPanel);
        mainPanel.add(endScreen, "EndScreen");
        cardLayout.show(mainPanel, "EndScreen");
    }
}
