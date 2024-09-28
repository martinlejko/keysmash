package main.java.com.keysmash.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * The `ButtonBuilder` class is a utility for creating customized `JButton` instances.
 * It allows setting various properties such as text, background color, foreground color,
 * font, and action listeners in a fluent API style.
 */
public class ButtonBuilder {
    private String text;
    private Color background;
    private Color foreground;
    private Font font;
    private ActionListener actionListener;

    /**
     * Constructs a `ButtonBuilder` with default button properties.
     */
    public ButtonBuilder() {
        this.background = new Color(34, 139, 34);
        this.foreground = Color.WHITE;
        this.font = new Font("Arial", Font.BOLD, 24);
    }

    /**
     * Sets the text of the button.
     *
     * @param text the text to set
     * @return the current instance of `ButtonBuilder`
     */
    public ButtonBuilder setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * Sets the background color of the button.
     *
     * @param background the background color to set
     * @return the current instance of `ButtonBuilder`
     */
    public ButtonBuilder setBackground(Color background) {
        this.background = background;
        return this;
    }

    /**
     * Sets the foreground color of the button.
     *
     * @param foreground the foreground color to set
     * @return the current instance of `ButtonBuilder`
     */
    public ButtonBuilder setForeground(Color foreground) {
        this.foreground = foreground;
        return this;
    }

    /**
     * Sets the font of the button.
     *
     * @param font the font to set
     * @return the current instance of `ButtonBuilder`
     */
    public ButtonBuilder setFont(Font font) {
        this.font = font;
        return this;
    }

    /**
     * Sets the action listener for the button.
     *
     * @param actionListener the action listener to set
     * @return the current instance of `ButtonBuilder`
     */
    public ButtonBuilder setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
        return this;
    }

    /**
     * Builds and returns a `JButton` with the specified properties.
     *
     * @return the constructed `JButton`
     */
    public JButton build() {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

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

    /**
     * Creates a default `JButton` with the specified text and action listener.
     *
     * @param text the text to set on the button
     * @param actionListener the action listener to set on the button
     * @return the constructed `JButton`
     */
    public JButton createDefaultButton(String text, ActionListener actionListener) {
        return new ButtonBuilder()
                .setText(text)
                .setActionListener(actionListener)
                .build();
    }
}