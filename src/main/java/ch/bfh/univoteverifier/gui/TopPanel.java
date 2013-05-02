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
import ch.bfh.univoteverifier.action.SelectIndVrfAction;
import ch.bfh.univoteverifier.action.SelectUniVrfAction;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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
    JPanel dynamicChoicePanel;
    JComboBox comboBox;
    boolean selectionMade = false;

    public TopPanel(JRadioButton btnUni, JRadioButton btnInd, JButton btnStart, JComboBox comboBox, ButtonGroup btnGrp) {
        rb = ResourceBundle.getBundle("error", GUIconstants.getLocale());
        this.comboBox = comboBox;

        this.setBackground(Color.WHITE);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(createTitlePanel());
        this.add(createButtonPanel(btnUni, btnInd, btnStart, btnGrp));
        this.add(createVrfDescPanel());

    }

    /**
     * when a button is pressed, this method with show the panel where the user
     * enters additional information
     */
    public void showChoicePanel() {
        dynamicChoicePanel = createDynamicChoicePanel();
        this.add(dynamicChoicePanel);
        panelModified();
        selectionMade=true;
    }

    /**
     * change the state of the GUI to show the selection choices for Universal
     * Verification
     */
    public void uniVrfSelected() {
        if (!selectionMade) 
            showChoicePanel();          
        dynamicChoicePanel.removeAll();
        String desc = rb.getString("descEID");
        JLabel label = new JLabel(desc);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        dynamicChoicePanel.add(label);
        dynamicChoicePanel.add(comboBox);
        panelModified();
    }

    /**
     * change the state of the GUI to show the selection choices for Universal
     * Verification
     */
    public void indVrfSelected() {
        if (!selectionMade) {
            showChoicePanel();
        }
        dynamicChoicePanel.removeAll();
        String desc = rb.getString("descQRCode");
        JLabel label = new JLabel(desc);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        JButton btnFileSelector = new JButton(rb.getString("selectFile"));
        btnFileSelector.setAction(ActionManager.getInstance().getAction("fileChooser"));

        dynamicChoicePanel.add(label);
        dynamicChoicePanel.add(btnFileSelector);
        panelModified();
    }

    public void panelModified(){
            this.revalidate();
        this.repaint();
    }
    /**
     * Create the button panel with the verification buttons in it
     *
     * @return JPanel
     */
    public JPanel createButtonPanel(JRadioButton btnUni, JRadioButton btnInd, JButton btnStart, ButtonGroup btnGroup) {
         Action selectUniVrfAction = new SelectUniVrfAction(this);
        ActionManager.getInstance().addActions("selectUniVrf", selectUniVrfAction);
        btnUni.setAction(selectUniVrfAction);
        
        Action selectIndVrfAction = new SelectIndVrfAction(this);
        ActionManager.getInstance().addActions("selectIndVrf", selectIndVrfAction);
        btnInd.setAction(selectIndVrfAction);
        
        JPanel panel = new JPanel();
        panel.setBackground(GUIconstants.GREY);
        panel.add(btnUni);
        panel.add(btnInd);
        panel.add(btnStart);
        return panel;
    }
    

    /**
     * Create the title panel with white background and image
     *
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
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));//top left bottom right

        vrfDescLabel = new JLabel(rb.getString("defaultDescription"));
        vrfDescLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(vrfDescLabel);
        return panel;
    }

    /**
     * Creates a panel whose contents are changed based on selections made by
     * the user. This panel will be displayed in the verification area (middle)
     * of the GUI
     */
    public JPanel createDynamicChoicePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(0, 0, 0, 0));
        panel.setBackground(GUIconstants.DARK_GREY);
        panel.setPreferredSize(new Dimension(300, 40));
        return panel;
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
