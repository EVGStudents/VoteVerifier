/*
 *
 *  Copyright (c) 2013 Berner Fachhochschule, Switzerland.
 *   Bern University of Applied Sciences, Engineering and Information Technology,
 *   Research Institute for Security in the Information Society, E-Voting Group,
 *   Biel, Switzerland.
 *
 *   Project independent UniVoteVerifier.
 *
 */
package ch.bfh.univoteverifier.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author prinstin
 *
 * creates the JPanel which is displayed in the middle of the GUI and contains
 * either input options for the user or displays the results of the verification
 * process.
 *
 * @return a JPanel which is one of the three main container/structure panels
 *
 */
public class MiddlePanel extends JPanel {

    /**
     * Create an instance of this class.
     * @param innerPanel the content panel for verification results is added into a scroll pane.
     */
    public MiddlePanel(JPanel innerPanel) {
        this.setLayout(new GridLayout(1, 1));
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(696, 450));
        this.setBorder(new EmptyBorder(10, 30, 10, 30)); //top left bottom right

        JScrollPane vrfScrollPanel = new JScrollPane(innerPanel);
        vrfScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        this.add(vrfScrollPanel);

    }
}
