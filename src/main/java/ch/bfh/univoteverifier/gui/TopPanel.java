/*
 *
 *  Copyright (c) 2013 Berner Fachhochschule, Switzerland.
 *   Bern University of Applied Sciences, Engineering and Information Technology,
 *   Research Institute for Security in the Information Society, E-Voting Group,
 *   Biel, Switzerland.
 *
 *   Project independent VoteVerifier.
 *
 */
package ch.bfh.univoteverifier.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Create several panels within this class that contain images, buttons and
 * labels.
 *
 * @author Justin Springer
 */
public class TopPanel extends JPanel {

    private static final Logger LOGGER = Logger.getLogger(TopPanel.class.getName());

    /**
     * Create an instance of this panel class.
     *
     */
    public TopPanel() {
        this.setBackground(Color.WHITE);
        this.setLayout(new GridLayout(1, 1));

        this.setPreferredSize(new Dimension(696, 53));
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;
//        this.add(createTitlePanel());
        this.add(getTitleImage());
    }

    /**
     * Create the title panel with white background and image
     *
     * @return JPanel the panel which contains the title image for the window.
     */
    public JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel();
//        titlePanel.setLayout(new GridLayout(1, 1));
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.setBackground(new Color(255, 200, 200));

        titlePanel.add(getTitleImage());
        titlePanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        return titlePanel;
    }

    /**
     * Create the panel, which contains the title image.
     *
     * @return a JPanel title image
     */
    private JLabel getTitleImage() {
        JLabel imgLabel = new JLabel();
        java.net.URL img = VoteVerifier.class.getResource("/univoteTitle.jpeg");
        if (img != null) {
            ImageIcon logo = new ImageIcon(img);
            imgLabel = new JLabel(logo);
//            imgLabel.setMaximumSize(new Dimension(300, 114));
            imgLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        } else {
            LOGGER.log(Level.INFO, "IMAGE NOT FOUND");
        }
        return imgLabel;
    }
}
