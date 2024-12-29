package com.keysmash;

import com.keysmash.database.DatabaseManager;
import com.keysmash.gui.MainWindow;

import javax.swing.*;

/**
 * The entry point of the Keysmash application.
 * <p>
 * This class is responsible for initializing the database manager
 * and launching the main application window.
 * </p>
 */
public class Main {

    /**
     * The main method that starts the application.
     * <p>
     * Initializes the database manager and handles exceptions. If initialization
     * fails, an error message is printed, and the application exits with status 1.
     * If successful, the main application window is launched.
     * </p>
     *
     * @param args command-line arguments passed to the application
     */
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
