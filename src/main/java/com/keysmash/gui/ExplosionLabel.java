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

    private void startAnimation() {
        explosionPieces.clear();
        for (int i = 0; i < 20; i++) {
            explosionPieces.add(new ExplosionPiece());
        }
        timer.start();
    }

    private void stopAnimation() {
        timer.stop();
        explosionPieces.clear();
        repaint();
    }

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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (ExplosionPiece piece : explosionPieces) {
            g.setColor(new Color(piece.color.getRed(), piece.color.getGreen(), piece.color.getBlue(), piece.alpha));
            g.fillOval(piece.x, piece.y, piece.size, piece.size);
        }
    }

    private class ExplosionPiece {
        int x, y, size, alpha;
        int sizeIncrement;
        int xSpeed, ySpeed;
        Color color;

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

        private Color getRandomColor() {
            return new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
        }
    }
}
