package main.java.com.keysmash.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class ExplosionLabel extends JLabel {
    private List<ExplosionPiece> explosionPieces = new ArrayList<>();
    private Random rand = new Random();
    private Timer timer;

    public ExplosionLabel(String text) {
        super(text);
        setFont(new Font("Comic Sans MS", Font.BOLD, 48));
        setForeground(Color.WHITE); // Set title text color
        setHorizontalAlignment(SwingConstants.CENTER); // Center the text

        // Mouse listener for explosion effect
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                startAnimation(); // Start animation on mouse enter
            }

            @Override
            public void mouseExited(MouseEvent e) {
                stopAnimation(); // Stop animation on mouse exit
            }
        });

        // Timer for updating the explosion animation
        timer = new Timer(20, e -> {
            updateExplosion();
            repaint(); // Repaint to show updated explosion
        });
    }

    private void startAnimation() {
        explosionPieces.clear(); // Clear existing pieces
        for (int i = 0; i < 20; i++) { // Generate 20 explosion pieces
            explosionPieces.add(new ExplosionPiece());
        }
        timer.start(); // Start the timer
    }

    private void stopAnimation() {
        timer.stop(); // Stop the timer
        explosionPieces.clear(); // Clear explosion pieces when stopped
        repaint(); // Repaint to clear any lingering pieces
    }

    private void updateExplosion() {
        // Update positions and sizes of the explosion pieces
        for (int i = explosionPieces.size() - 1; i >= 0; i--) {
            ExplosionPiece piece = explosionPieces.get(i);
            piece.size += piece.sizeIncrement; // Increase size
            piece.alpha -= 5; // Decrease alpha for fading effect
            piece.x += piece.xSpeed; // Move in X direction
            piece.y += piece.ySpeed; // Move in Y direction

            // Remove if out of view or completely faded
            if (piece.alpha <= 0 || piece.size > 50) {
                explosionPieces.remove(i);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw each explosion piece
        for (ExplosionPiece piece : explosionPieces) {
            g.setColor(new Color(piece.color.getRed(), piece.color.getGreen(), piece.color.getBlue(), piece.alpha));
            g.fillOval(piece.x, piece.y, piece.size, piece.size);
        }
    }

    // Class to represent individual explosion pieces
    private class ExplosionPiece {
        int x, y, size, alpha;
        int sizeIncrement;
        int xSpeed, ySpeed;
        Color color;

        public ExplosionPiece() {
            // Start from the center of the label
            x = getWidth() / 2;
            y = getHeight() / 2;
            size = 0; // Start size for explosion
            alpha = 255; // Start fully opaque
            sizeIncrement = rand.nextInt(5) + 2; // Random size increment
            xSpeed = rand.nextInt(5) - 2; // Random X speed (-2 to +2)
            ySpeed = rand.nextInt(5) - 2; // Random Y speed (-2 to +2)
            color = getRandomColor(); // Random color
        }

        private Color getRandomColor() {
            return new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
        }
    }
}
