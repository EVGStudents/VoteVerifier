/**
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

import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * A JPanel that is used to show the verification results in the middle of the
 * GUI
 *
 * @author prinstin
 */
public class ResultPanel extends JPanel {

    private String name;
    JPanel titlePanel, contentPanel;
    JLabel label;

    /**
     * Create an instance of this Panel class.
     * @param s The name to assign to this panel class.
     */
    ResultPanel(String s) {
        super();
        name = s;
        generatePanel();
    }

    /**
     * Creates and structure the panel.
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

    /**
     * Get the name of this Panel class.
     * @return A String of the name of this Panel class.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the panel responsible for showing the verification results.
     *
     * @return The content panel for this instance.
     */
    public JPanel getContentPanel() {
        return this.contentPanel;
    }

    /**
     * Add content to the results panel. This method is called if message
     * received over observer pattern.
     *
     * @param str The text for the verification result to be displayed to in the
     * GUI.
     * @param b The result of the verification.
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
     * Generate a standard panel for this class.
     *
     * @return An empty JPanel used by this class only.
     */
    public JPanel getBoxPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        return panel;
    }
}