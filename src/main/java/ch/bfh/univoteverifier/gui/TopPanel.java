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

import ch.bfh.univoteverifier.action.ActionManager;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author prinstin
 */
public class TopPanel extends JPanel {

    private static final Logger LOGGER = Logger.getLogger(MainGUI.class.getName());
    String eIDlist;
    JLabel vrfDescLabel;
    ResourceBundle rb;
    

    public TopPanel(JButton btnUni, JButton btnInd, JButton btnStart) {
        rb = ResourceBundle.getBundle("error", Locale.ENGLISH);

        this.setBackground(Color.WHITE);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(createTitlePanel());
        this.add(createButtonPanel(btnUni,  btnInd,  btnStart));
        this.add(createVrfDescPanel());
        
    }

    /**
     * when a button is pressed, this method with show the panel where the user enters additional information 
     */
    public void showChoicePanel(){
        this.add(createDynamicChoicePanel());
        this.revalidate();
        this.repaint();
    }
    
      /**
     * Create the button panel with the verification buttons in it
     * @return JPanel
     */ 
    public JPanel createButtonPanel(JButton btnUni, JButton btnInd, JButton btnStart){
        JPanel panel = new JPanel();
        panel.setBackground(GUIconstants.GREY);
        panel.add(btnUni);
        panel.add(btnInd);
        panel.add(btnStart);
        return panel;
    }
    
    /**
     * Create the title panel with white background and image
     * @return JPanel
     */
    public JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new GridLayout(1, 1));
        titlePanel.setBackground(Color.white);
        titlePanel.add(getTitleImage());
        titlePanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        return titlePanel;
    }

    public JPanel createVrfDescPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 1));
        panel.setBackground(GUIconstants.DARK_GREY);

        vrfDescLabel = new JLabel(rb.getString("defaultDescription"));
        vrfDescLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(vrfDescLabel);
        return panel;
    }
    /**
     * Creates a panel whose contents are changed based on selections made
     * by the user. This panel will be displayed in the verification area
     * (middle) of the GUI
     */
    public JPanel createDynamicChoicePanel() {
        JPanel choicePanel = new JPanel();
        choicePanel.setBackground(GUIconstants.DARK_GREY);
        choicePanel.setPreferredSize(new Dimension(300, 40));

        JLabel choiceLabel = new JLabel();
        choiceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        choicePanel.add(choiceLabel);
        return choicePanel;
    }
    
        
    /**
     * Draw the panel with the image
     *
     * @return a JPanel title image
     */
    private JPanel getTitleImage() {
        JPanel imgPanel = new JPanel();
        java.net.URL img = MainGUI.class.getResource("/univoteTitle.jpeg");
        if (img != null) {
            ImageIcon logo = new ImageIcon(img);
            JLabel imgLab = new JLabel(logo);
            imgPanel.setMaximumSize(new Dimension(300, 114));
            imgPanel.add(imgLab);
            imgPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        } else {
            LOGGER.log(Level.INFO, "IMAGE NOT FOUND");
        }
        return imgPanel;
    }

    public void setDescription(String str) {
        vrfDescLabel.setText(str);
    }

    public String getDescription() {
        return vrfDescLabel.getText();
    }
}
