package main.java.com.keysmash.gui;

import main.java.com.keysmash.database.DatabaseManager;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class LeaderboardScreen extends JPanel {
    private DatabaseManager databaseManager;
    private JTable leaderboardTable;

    public LeaderboardScreen() {
        databaseManager = new DatabaseManager();
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK); // Set background color to black

        // Create and style the title label
        JLabel titleLabel = new JLabel("Leaderboard");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE); // Set text color to white
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Larger font size for the title
        add(titleLabel, BorderLayout.NORTH);

        // Initialize the leaderboard table
        leaderboardTable = new JTable();
        leaderboardTable.setBackground(Color.BLACK);
        leaderboardTable.setForeground(Color.WHITE);
        leaderboardTable.setFont(new Font("Arial", Font.PLAIN, 16)); // Set font for table
        leaderboardTable.setFillsViewportHeight(true);
        leaderboardTable.setShowGrid(true);
        leaderboardTable.setGridColor(Color.WHITE);

        // Set the table's cell renderer for better visibility
        leaderboardTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                cell.setBackground(Color.BLACK);
                cell.setForeground(Color.WHITE);
                return cell;
            }
        });

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(leaderboardTable);
        scrollPane.setPreferredSize(new Dimension(400, 300)); // Set preferred size for the scroll pane

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.BLACK); // Ensure background matches
        centerPanel.add(scrollPane);

        // Add centered table panel to the main panel
        add(centerPanel, BorderLayout.CENTER);

        // ButtonBuilder to create the "Back" button with the same style
        ButtonBuilder buttonBuilder = new ButtonBuilder();
        JButton backButton = buttonBuilder.createDefaultButton("Back", e -> {
            // Go back to the home screen
            CardLayout cardLayout = (CardLayout) getParent().getLayout();
            cardLayout.show(getParent(), "HomeScreen");
        });

        // Add vertical space and back button below the table
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK); // Ensure background matches
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        refreshLeaderboardData(); // Refresh data every time the panel is shown
    }

    private void refreshLeaderboardData() {
        // Fetch leaderboard data from the database
        List<String[]> leaderboardData = databaseManager.getLeaderboardData();

        // Check if leaderboardData is empty
        if (leaderboardData.isEmpty()) {
            // Handle the case where there is no data
            leaderboardTable.setModel(new javax.swing.table.DefaultTableModel(
                    new Object[][]{{"No data available."}},
                    new String[]{"Message"}
            ));
            return; // Exit the method early if there's no data
        }

        // Limit data to top 10 entries
        int numberOfEntries = Math.min(leaderboardData.size(), 10);
        Object[][] data = new Object[numberOfEntries][4];
        int rank = 1;

        for (int i = 0; i < numberOfEntries; i++) {
            String[] row = leaderboardData.get(i);
            data[i][0] = rank++;          // Rank
            data[i][1] = row[1];         // Username (corrected)
            data[i][2] = row[0];         // WPM (corrected)
            data[i][3] = row[2];         // Accuracy
        }

        // Update the table model with new data
        leaderboardTable.setModel(new javax.swing.table.DefaultTableModel(
                data,
                new String[]{"Rank", "Name", "WPM", "Accuracy"}
        ));
    }
}
