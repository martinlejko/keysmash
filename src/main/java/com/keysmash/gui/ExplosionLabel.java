package main.java.com.keysmash.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The ExplosionLabel class extends JLabel to create a label with an explosion animation effect.
 * When the mouse enters the label, the explosion animation starts, and when the mouse exits, the animation stops.
 */
class ExplosionLabel extends JLabel {
    private final List<ExplosionPiece> explosionPieces = new ArrayList<>();
    private final Random rand = new Random();
    private final Timer timer;

    /**
     * Constructs an ExplosionLabel with the specified text.
     *
     * @param text the text to be displayed on the label
     */
    public ExplosionLabel(String text) {
        super(text);
        setFont(new Font("Comic Sans MS", Font.BOLD, 48));
        setForeground(Color.WHITE);
        setHorizontalAlignment(SwingConstants.CENTER);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                startAnimation();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                stopAnimation();
            }
        });

        timer = new Timer(20, e -> {
            updateExplosion();
            repaint();
        });
    }

    /**
     * Starts the explosion animation by initializing explosion pieces and starting the timer.
     */
    private void startAnimation() {
        explosionPieces.clear();
        for (int i = 0; i < 20; i++) {
            explosionPieces.add(new ExplosionPiece());
        }
        timer.start();
    }

    /**
     * Stops the explosion animation by stopping the timer and clearing the explosion pieces.
     */
    private void stopAnimation() {
        timer.stop();
        explosionPieces.clear();
        repaint();
    }

    /**
     * Updates the state of each explosion piece, including its size, position, and transparency.
     * Removes pieces that have become too large or fully transparent.
     */
    private void updateExplosion() {
        for (int i = explosionPieces.size() - 1; i >= 0; i--) {
            ExplosionPiece piece = explosionPieces.get(i);
            piece.size += piece.sizeIncrement;
            piece.alpha -= 5;
            piece.x += piece.xSpeed;
            piece.y += piece.ySpeed;

            if (piece.alpha <= 0 || piece.size > 50) {
                explosionPieces.remove(i);
            }
        }
    }

    /**
     * Paints the explosion pieces on the label.
     *
     * @param g the Graphics object used for painting
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (ExplosionPiece piece : explosionPieces) {
            g.setColor(new Color(piece.color.getRed(), piece.color.getGreen(), piece.color.getBlue(), piece.alpha));
            g.fillOval(piece.x, piece.y, piece.size, piece.size);
        }
    }

    /**
     * The ExplosionPiece class represents a single piece of the explosion animation.
     * Each piece has properties such as position, size, speed, and color.
     */
    private class ExplosionPiece {
        int x, y, size, alpha;
        int sizeIncrement;
        int xSpeed, ySpeed;
        Color color;

        /**
         * Constructs an ExplosionPiece with random properties.
         */
        public ExplosionPiece() {
            x = getWidth() / 2;
            y = getHeight() / 2;
            size = 0;
            alpha = 255;
            sizeIncrement = rand.nextInt(5) + 2;
            xSpeed = rand.nextInt(5) - 2;
            ySpeed = rand.nextInt(5) - 2;
            color = getRandomColor();
        }

        /**
         * Generates a random color.
         *
         * @return a Color object with random RGB values
         */
        private Color getRandomColor() {
            return new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
        }
    }
}
