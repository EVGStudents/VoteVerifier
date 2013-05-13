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

import ch.bfh.univoteverifier.table.ResultTabbedPane;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JPanel;
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

    JPanel backgroundPanel;

    /**
     * Create an instance of this class.
     *
     * @param resultPanelManager the content panel for verification results is
     * added into a scroll pane.
     */
    public MiddlePanel(ResultTabbedPane resultPanelManager) {
        this.setLayout(new GridLayout(1, 1));
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(696, 500));
        this.setBorder(new EmptyBorder(10, 30, 10, 30)); //top left bottom right
        resultPanelManager.setBackground(Color.WHITE);
//        backgroundPanel = new JPanel();
//        backgroundPanel.setBackground(Color.WHITE);
//        backgroundPanel.setLayout(new GridLayout(1, 1));
//        backgroundPanel.add(resultPanelManager);
        this.add(resultPanelManager);
    }

    public void setBackgroundGrey() {
        backgroundPanel.setBackground(GUIconstants.GREY);
    }
}
