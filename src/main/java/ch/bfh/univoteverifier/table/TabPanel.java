/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author prinstin
 */
public class TabPanel extends JPanel {

    JLabel lblTitle;

    public TabPanel(String title, RemoveTabAction removeTabAction) {
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.setOpaque(false);
        lblTitle = new JLabel(title);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

        JButton btnClose = new JButton("x");
        btnClose.setBorderPainted(true);
        btnClose.setName(title);
        btnClose.setPreferredSize(new Dimension(17, 17));
        btnClose.setToolTipText("close this tab");
        btnClose.setUI(new BasicButtonUI());
        btnClose.setContentAreaFilled(false);
        btnClose.setFocusable(false);
        btnClose.setBorder(BorderFactory.createEtchedBorder());
        btnClose.setRolloverEnabled(true);

        this.add(lblTitle);
        this.add(btnClose);

        btnClose.addActionListener(removeTabAction);

    }

    public void setFontColor(Color c) {
        lblTitle.setForeground(c);
    }
}
