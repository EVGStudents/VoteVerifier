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
    JButton btnUni, btnInd, btnStart;
    String eIDlist;
JLabel vrfDescLabel;
ResourceBundle rb;

    public TopPanel(JButton btnUni, JButton btnInd, JButton btnStart) {
        rb= ResourceBundle.getBundle("error", Locale.ENGLISH);
        this.btnInd = btnInd;
        this.btnUni = btnUni;
        this.btnStart = btnStart;
        
        this.setBackground(Color.WHITE);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(getTitlePanel());

        
        //description panel.  button in above panel changes text in this panel
        //contains button to start verification
        JPanel vrfDescPanel = new JPanel();
        vrfDescPanel.setLayout(new GridLayout(1, 1));
        vrfDescPanel.setBackground(GUIconstants.DARK_GREY);


        vrfDescLabel = new JLabel(rb.getString("defaultDescription"));
        vrfDescLabel.setHorizontalAlignment(SwingConstants.CENTER);
        vrfDescPanel.add(vrfDescLabel);
        
        
        //button panel with two buttons and grey background
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(GUIconstants.GREY);

        JButton btnUniVrf = btnUni;
        JButton btnIndVrf = btnInd;

       
        buttonPanel.add(new I18nButtonPanel());
        buttonPanel.add(btnUniVrf);
        buttonPanel.add(btnIndVrf);
        buttonPanel.add(btnStart);
 
        this.add(buttonPanel);
        this.add(vrfDescPanel);
    }

    public JPanel getTitlePanel() {
        //title panel with white background and image
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new GridLayout(1, 1));
        titlePanel.setBackground(Color.white);
        titlePanel.add(getTitleImage());
        titlePanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        return titlePanel;
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
    
    public void setDescription(String str){
        vrfDescLabel.setText(str);
    }
    public String getDescription(){
    return vrfDescLabel.getText();
    }
}
