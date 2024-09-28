package main.java.com.keysmash;

//import main.java.com.keysmash.database.DatabaseManager;
import main.java.com.keysmash.database.DatabaseManager;
import main.java.com.keysmash.gui.MainWindow;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        DatabaseManager dbManager = new DatabaseManager();

        javax.swing.SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}