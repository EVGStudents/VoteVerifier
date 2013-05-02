/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.gui;

import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author prinstin
 * A JPanel that is used to show the verification results in the middle of the GUI
 */
public class ResultPanel extends JPanel {

    private String name;
    JPanel titlePanel, contentPanel;
    JLabel label;

    ResultPanel(String s) {
        super();
        name = s;
        generatePanel();
    }

    /**
     * creates and structures the panel
     */
    public void generatePanel() {
        this.setPreferredSize(new Dimension(600, 100));
        this.setBackground(GUIconstants.GREY);
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        label = new JLabel(name);
        label.setFont(new Font("Serif", Font.PLAIN, 16));
        titlePanel = getBoxPanel();
        titlePanel.add(label);
        contentPanel = getBoxPanel();
        this.add(titlePanel);
        this.add(contentPanel);
    }

    public String getName() {
        return name;
    }

    /**
     * returns the panel responsible for showing the verification results
     *
     * @return the content panel for this instance
     */
    public JPanel getContentPanel() {
        return this.contentPanel;
    }

    /**
     * add content to the results panel. This method is called if message
     * received over observer pattern
     *
     * @param str the text for the verification result to be displayed to in the
     * GUI
     * @param b the result of the verification
     */
    public void addResultPanel(String str, boolean b) {
        JPanel panel = new JPanel();
        panel.setBackground(GUIconstants.GREY);
        panel.setBorder(new EmptyBorder(2, 20, 2, 10));
        JLabel resultLabel = new JLabel(str + "........................................... " + b);
        resultLabel.setFont(new Font("Serif", Font.PLAIN, 12));
        panel.add(resultLabel);
        if (b) {
            ImageIcon img = new ImageIcon(MainGUI.class
                    .getResource("/check.png").getPath());
            JLabel imgLabel = new JLabel(img);
            panel.add(imgLabel);
        }
        contentPanel.add(panel);
    }

    /**
     * generate a standard panel for this class
     *
     * @return an empty JPanel used by this class only
     */
    public JPanel getBoxPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        return panel;
    }
}