package main.java.com.keysmash.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ButtonBuilder {
    private String text;
    private Color background;
    private Color foreground;
    private Font font;
    private ActionListener actionListener;

    public ButtonBuilder() {
        this.background = new Color(34, 139, 34);
        this.foreground = Color.WHITE;
        this.font = new Font("Arial", Font.BOLD, 24);
    }

    public ButtonBuilder setText(String text) {
        this.text = text;
        return this;
    }

    public ButtonBuilder setBackground(Color background) {
        this.background = background;
        return this;
    }

    public ButtonBuilder setForeground(Color foreground) {
        this.foreground = foreground;
        return this;
    }

    public ButtonBuilder setFont(Font font) {
        this.font = font;
        return this;
    }

    public ButtonBuilder setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
        return this;
    }

    public JButton build() {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(background.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(background);
            }
        });

        if (actionListener != null) {
            button.addActionListener(actionListener);
        }

        return button;
    }

    // New method for creating a default button
    public JButton createDefaultButton(String text, ActionListener actionListener) {
        return new ButtonBuilder()
                .setText(text)
                .setActionListener(actionListener)
                .build();
    }
}
