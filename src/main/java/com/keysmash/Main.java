package main.java.com.keysmash;

import main.java.com.keysmash.database.DatabaseManager;
import main.java.com.keysmash.gui.MainWindow;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        DatabaseManager dbManager = null;
        try {
            dbManager = new DatabaseManager();
        } catch (Exception e) {
            System.err.println("Failed to initialize the database: " + e.getMessage());
            System.exit(1);
        }

        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setVisible(true);
        });
    }
}
