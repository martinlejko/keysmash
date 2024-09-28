package main.java.com.keysmash.gui;

import javax.swing.*;
import java.awt.*;

public class HelpScreen extends JPanel {
    public HelpScreen() {
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK); // Set background color to black

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 10, 20, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create and style the help label
        JLabel helpLabel = new JLabel("<html><div style='text-align: center;'>"
                + "Welcome to Keysmash!<br><br>"
                + "In this game, your goal is to type as many keys as possible within a given time frame.<br>"
                + "We measure your typing speed and accuracy to determine your score.<br><br>"
                + "You can check the leaderboard to see who is the best keysmasher.<br>"
                + "Good luck and have fun!"
                + "</div></html>");
        helpLabel.setHorizontalAlignment(SwingConstants.CENTER);
        helpLabel.setForeground(Color.WHITE); // Set text color to white
        helpLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Optional: Set font style and size
        add(helpLabel, gbc);

        // ButtonBuilder to create the "Back" button with the same style
        ButtonBuilder buttonBuilder = new ButtonBuilder();
        JButton backButton = buttonBuilder.createDefaultButton("Back", e -> {
            // Assuming MainWindow has a reference to switch panels
            CardLayout cardLayout = (CardLayout) getParent().getLayout();
            cardLayout.show(getParent(), "HomeScreen"); // Switch back to HomeScreen
        });

        // Add back button below the help label
        gbc.gridy = 1;
        add(backButton, gbc);
    }
}