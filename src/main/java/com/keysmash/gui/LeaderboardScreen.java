package main.java.com.keysmash.gui;

import main.java.com.keysmash.database.DatabaseManager;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class LeaderboardScreen extends JPanel {
    private final DatabaseManager databaseManager;
    private JTable leaderboardTable;

    public LeaderboardScreen() {
        databaseManager = new DatabaseManager();
        initializeComponents();

        // Add component listener to refresh data when the component is shown
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refreshLeaderboardData();
            }
        });
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JLabel titleLabel = new JLabel("Leaderboard");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        leaderboardTable = new JTable();
        leaderboardTable.setBackground(Color.BLACK);
        leaderboardTable.setForeground(Color.WHITE);
        leaderboardTable.setFont(new Font("Arial", Font.PLAIN, 16));
        leaderboardTable.setFillsViewportHeight(true);
        leaderboardTable.setShowGrid(true);
        leaderboardTable.setGridColor(Color.WHITE);

        leaderboardTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column
            ) {
                Component cell = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column
                );
                cell.setBackground(Color.BLACK);
                cell.setForeground(Color.WHITE);
                return cell;
            }
        });

        JScrollPane scrollPane = new JScrollPane(leaderboardTable);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.BLACK);
        centerPanel.add(scrollPane);

        add(centerPanel, BorderLayout.CENTER);

        ButtonBuilder buttonBuilder = new ButtonBuilder();
        JButton backButton = buttonBuilder.createDefaultButton("Back", e -> {
            CardLayout cardLayout = (CardLayout) getParent().getLayout();
            cardLayout.show(getParent(), "HomeScreen");
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void refreshLeaderboardData() {
        List<String[]> leaderboardData = databaseManager.getLeaderboardData();

        if (leaderboardData.isEmpty()) {
            leaderboardTable.setModel(new javax.swing.table.DefaultTableModel(
                    new Object[][]{{"No data available."}},
                    new String[]{"Message"}
            ));
            return;
        }

        int numberOfEntries = Math.min(leaderboardData.size(), 10);
        Object[][] data = new Object[numberOfEntries][4];
        int rank = 1;

        for (int i = 0; i < numberOfEntries; i++) {
            String[] row = leaderboardData.get(i);
            data[i][0] = rank++;      // Rank
            data[i][1] = row[1];      // Name
            data[i][2] = row[2];      // WPM
            data[i][3] = row[3];      // Accuracy
        }

        leaderboardTable.setModel(new javax.swing.table.DefaultTableModel(
                data,
                new String[]{"Rank", "Name", "WPM", "Accuracy"}
        ));
    }
}
