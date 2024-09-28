package main.java.com.keysmash;

import main.java.com.keysmash.database.DatabaseManager;
import main.java.com.keysmash.gui.MainWindow;

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
     * This method attempts to initialize the database manager and
     * handles any exceptions that occur during initialization.
     * If the initialization fails, an error message is printed to
     * the standard error stream, and the application exits with a
     * status code of 1.
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
