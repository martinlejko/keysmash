package main.java.com.keysmash.gui;

import javax.swing.*;
import java.awt.*;

public class HelpScreen extends JPanel {
    public HelpScreen() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK); // Set background color to black

        // Create and style the help label
        JLabel helpLabel = new JLabel("This is the Help Screen.");
        helpLabel.setHorizontalAlignment(SwingConstants.CENTER);
        helpLabel.setForeground(Color.WHITE); // Set text color to white
        helpLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Optional: Set font style and size
        add(helpLabel, BorderLayout.CENTER);

        // ButtonBuilder to create the "Back" button with the same style
        ButtonBuilder buttonBuilder = new ButtonBuilder();
        JButton backButton = buttonBuilder.createDefaultButton("Back", e -> {
            // Assuming MainWindow has a reference to switch panels
            CardLayout cardLayout = (CardLayout) getParent().getLayout();
            cardLayout.show(getParent(), "HomeScreen"); // Switch back to HomeScreen
        });

        // Add back button to the bottom (south) of the panel
        add(backButton, BorderLayout.SOUTH);
    }
}
