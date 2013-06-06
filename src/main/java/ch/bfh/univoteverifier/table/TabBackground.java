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
package ch.bfh.univoteverifier.table;

import ch.bfh.univoteverifier.action.RemoveTabAction;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * This is the information that is displayed in a tab of the TabbedPane.
 *
 * @author prinstin
 */
public class TabBackground extends JPanel {

    private JLabel titleLabel;

    public TabBackground(String title, RemoveTabAction removeTabAction, String processID) {
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.setOpaque(false);
        titleLabel = new JLabel(title);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

        JButton btnClose = new JButton("x");
        btnClose.setBorderPainted(true);
        btnClose.setName(processID);
        btnClose.setPreferredSize(new Dimension(17, 17));
        btnClose.setToolTipText("close this tab");
        btnClose.setUI(new BasicButtonUI());
        btnClose.setContentAreaFilled(false);
        btnClose.setFocusable(false);
        btnClose.setBorder(BorderFactory.createEtchedBorder());
        btnClose.setRolloverEnabled(true);

        this.add(titleLabel);
        this.add(btnClose);

        btnClose.addActionListener(removeTabAction);

    }

    /**
     * Set the font color of this component.
     *
     * @param c
     */
    public void setFontColor(Color c) {
        titleLabel.setForeground(c);
    }
}
