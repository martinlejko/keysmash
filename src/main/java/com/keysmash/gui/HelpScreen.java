package main.java.com.keysmash.gui;

import javax.swing.*;
import java.awt.*;

/**
 * HelpScreen class provides a user interface panel that displays game instructions and information
 * to the user, including the objective of the game and how to navigate back to the main menu.
 */
public class HelpScreen extends JPanel {

    /**
     * Constructs a HelpScreen panel.
     * Sets up the layout, background color, and initializes the help content and back button.
     */
    public HelpScreen() {
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 10, 20, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel helpLabel = new JLabel("<html><div style='text-align: center;'>"
                + "Welcome to Keysmash!<br><br>"
                + "In this game, your goal is to type as many keys as possible within a given time frame.<br>"
                + "We measure your typing speed and accuracy to determine your score.<br><br>"
                + "You can check the leaderboard to see who is the best keysmasher.<br>"
                + "Good luck and have fun!"
                + "</div></html>");
        helpLabel.setHorizontalAlignment(SwingConstants.CENTER);
        helpLabel.setForeground(Color.WHITE);
        helpLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(helpLabel, gbc);

        ButtonBuilder buttonBuilder = new ButtonBuilder();

        JButton backButton = buttonBuilder.createDefaultButton("Back", e -> {
            CardLayout cardLayout = (CardLayout) getParent().getLayout();
            cardLayout.show(getParent(), "HomeScreen");
        });

        gbc.gridy = 1;
        add(backButton, gbc);
    }
}
