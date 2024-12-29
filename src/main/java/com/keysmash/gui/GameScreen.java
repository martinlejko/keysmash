package com.keysmash.gui;

import com.keysmash.database.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A JPanel that implements the typing game interface. Displays text for the user to type,
 * tracks typing progress, and calculates real-time statistics like WPM and accuracy.
 */
public class GameScreen extends JPanel {
    private final String textToType;
    private String userInput = "";
    private long startTime;
    private Timer timer;
    private JLabel displayLabel;
    private JLabel wpmLabel;
    private JLabel accuracyLabel;
    private int correctCharacters = 0;
    private final String username;
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private DatabaseManager dbManager;

    /**
     * Creates a new game screen with the specified parameters.
     *
     * @param text The text that the user needs to type
     * @param username The current player's username for score tracking
     * @param cardLayout The layout manager used for screen transitions
     * @param mainPanel The container panel that holds all game screens
     */
    public GameScreen(String text, String username, CardLayout cardLayout, JPanel mainPanel) {
        this.textToType = text;
        this.username = username;
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        initializeComponents();
        startGame();
    }

    /**
     * Sets up the UI components including the text display, WPM counter,
     * and accuracy display. Also initializes the key listeners for typing input.
     */
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        displayLabel = new JLabel();
        displayLabel.setFont(new Font("Monospaced", Font.PLAIN, 24));
        displayLabel.setForeground(Color.WHITE);
        displayLabel.setHorizontalAlignment(SwingConstants.CENTER);
        displayLabel.setText(getFormattedText());
        add(displayLabel, BorderLayout.CENTER);

        JPanel statsPanel = new JPanel();
        statsPanel.setBackground(Color.BLACK);
        wpmLabel = new JLabel("WPM: 0");
        wpmLabel.setForeground(Color.WHITE);
        accuracyLabel = new JLabel("Accuracy: 100%");
        accuracyLabel.setForeground(Color.WHITE);
        statsPanel.add(wpmLabel);
        statsPanel.add(accuracyLabel);
        add(statsPanel, BorderLayout.SOUTH);

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
        requestFocusInWindow();
    }

    /**
     * Initializes the game timer and start time for WPM calculation.
     */
    private void startGame() {
        startTime = System.currentTimeMillis();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateWPM();
            }
        }, 60000, 60000);
    }

    /**
     * Processes typed characters and updates the game state.
     * Ignores control characters and handles game completion.
     *
     * @param keyChar The character that was typed
     */
    private void handleKeyTyped(char keyChar) {
        if (Character.isISOControl(keyChar)) {
            return;
        }
        if (userInput.length() < textToType.length()) {
            userInput += keyChar;
            checkInput();
            updateDisplay();

            if (userInput.length() == textToType.length()) {
                endGame();
            }
        }
    }

    /**
     * Removes the last character from the user's input when backspace is pressed.
     */
    private void handleBackspace() {
        if (!userInput.isEmpty()) {
            userInput = userInput.substring(0, userInput.length() - 1);
            checkInput();
            updateDisplay();
        }
    }

    /**
     * Compares the user's input against the target text and counts correct characters.
     * Updates the accuracy display after checking.
     */
    private void checkInput() {
        correctCharacters = 0;

        for (int i = 0; i < userInput.length(); i++) {
            if (i < textToType.length() && userInput.charAt(i) == textToType.charAt(i)) {
                correctCharacters++;
            }
        }

        updateAccuracy();
    }

    /**
     * Updates the text display, highlighting correct characters in green,
     * incorrect characters in red, and remaining characters in white.
     * The current character position is underlined.
     */
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
                if (i == userInput.length()) {
                    displayText.append("<u><font color='white'>").append(expectedChar).append("</font></u>");
                } else {
                    displayText.append("<font color='white'>").append(expectedChar).append("</font>");
                }
            }
        }
        displayText.append("</html>");
        displayLabel.setText(displayText.toString());
    }

    /**
     * Calculates and updates the Words Per Minute (WPM) display.
     * WPM is calculated as (character count / 5) / minutes elapsed.
     */
    private void updateWPM() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        int minutes = (int) (elapsedTime / 60000);
        int wpm = (userInput.length() / 5) / (minutes + 1);
        wpmLabel.setText("WPM: " + wpm);
    }

    /**
     * Calculates and updates the typing accuracy percentage display.
     * Accuracy is the ratio of correct characters to total characters typed.
     */
    private void updateAccuracy() {
        int totalChars = userInput.length();
        int accuracy = totalChars > 0 ? (int) ((correctCharacters * 100.0) / totalChars) : 100;
        accuracyLabel.setText("Accuracy: " + accuracy + "%");
    }

    /**
     * Wraps the target text in HTML tags for proper display in the JLabel.
     *
     * @return HTML-formatted string of the text to type
     */
    private String getFormattedText() {
        return "<html>" + textToType.replace("\n", "<br>") + "</html>";
    }

    /**
     * Handles game completion by saving the final score to the database
     * and transitioning to the end screen. Cancels the WPM update timer.
     */
    private void endGame() {
        timer.cancel();
        updateWPM();
        updateAccuracy();

        int finalWPM = Integer.parseInt(wpmLabel.getText().split(": ")[1]);
        int finalAccuracy = Integer.parseInt(accuracyLabel.getText().split(": ")[1].replace("%", ""));

        dbManager = new DatabaseManager();
        dbManager.storeScore(username, finalWPM, finalAccuracy);

        EndScreen endScreen = new EndScreen(username, cardLayout, mainPanel);
        mainPanel.add(endScreen, "EndScreen");
        cardLayout.show(mainPanel, "EndScreen");
    }


}