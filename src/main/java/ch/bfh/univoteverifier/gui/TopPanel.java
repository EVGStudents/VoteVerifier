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
import com.sun.java.swing.plaf.motif.MotifBorders;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

/**
 * Create several panels within this class that contain images, buttons and
 * labels.
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
    JButton btnFileSelector;

    /**
     * Create an instance of this panel class.
     *
     * @param btnUni a button to add to the button panel.
     * @param btnInd a button to add to the button panel.
     * @param btnStart a button to add to the button panel.
     * @param comboBox The comboBox that contains election ID choices.
     * @param btnGrp The ButtonGroup containing the RadioButtons btnUni and
     * btnInd.
     */
    public TopPanel(JRadioButton btnUni, JRadioButton btnInd, JButton btnStart, JComboBox comboBox, ButtonGroup btnGrp) {
        rb = ResourceBundle.getBundle("error", GUIconstants.getLocale());
        this.comboBox = comboBox;
        btnFileSelector = new JButton(rb.getString("selectFile"));
        btnFileSelector.setAction(ActionManager.getInstance().getAction("fileChooser"));

        this.setBackground(Color.WHITE);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(createTitlePanel());
        this.add(createButtonPanel(btnUni, btnInd, btnStart, btnGrp));
        this.add(createVrfDescPanel());

    }

    /**
     * When a button is pressed, this method with show the panel where the user
     * enters additional information.
     */
    public void showChoicePanel() {
        dynamicChoicePanel = createDynamicChoicePanel();
        this.add(dynamicChoicePanel);
        panelModified();
        selectionMade = true;
    }

    public void setupErrorMsg(String text) {
        vrfDescLabel.setText(text);
        vrfDescLabel.setForeground(new Color(240, 70, 20));
    }

    /**
     * Change the state of the GUI to show the selection choices for Universal
     * Verification.
     */
    public void uniVrfSelected() {
        changeContent("descEID", "descUni", comboBox);
    }

    /**
     * Change the state of the GUI to show the selection choices for Individual
     * Verification.
     */
    public void indVrfSelected() {
        changeContent("descQRCode", "descInd", btnFileSelector);
    }

    public void changeContent(String descLabel, String descName, JComponent c) {
        vrfDescLabel.setForeground(Color.BLACK);
        vrfDescLabel.setText(rb.getString(descName));
        if (!selectionMade) {
            showChoicePanel();
        }
        dynamicChoicePanel.removeAll();

        String desc = rb.getString(descLabel);
        JLabel label = new JLabel(desc);
        label.setHorizontalAlignment(SwingConstants.CENTER);


        dynamicChoicePanel.add(label);
        dynamicChoicePanel.add(c);

        panelModified();
    }

    /**
     * Repaints and revalidates the GUI when changes have been made that must be
     * shown.
     */
    public void panelModified() {
        this.revalidate();
        this.repaint();
    }

    /**
     * Create the button panel with the verification buttons in it.
     *
     * @return JPanel the panel, which contains the buttons in the topPanel.
     */
    public JPanel createButtonPanel(JRadioButton btnUni, JRadioButton btnInd, JButton btnStart, ButtonGroup btnGroup) {
        Action selectUniVrfAction = new SelectUniVrfAction(this);
        ActionManager.getInstance().addActions("selectUniVrf", selectUniVrfAction);
        btnUni.setAction(selectUniVrfAction);

        Action selectIndVrfAction = new SelectIndVrfAction(this);
        ActionManager.getInstance().addActions("selectIndVrf", selectIndVrfAction);
        btnInd.setAction(selectIndVrfAction);

        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        panel.setBackground(GUIconstants.GREY);
        panel.add(btnUni);
        panel.add(btnInd);
        panel.add(btnStart);
        return panel;
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
     * Create a panel where description of the verification options is shown.
     *
     * @return A JPanel which shows verification options.
     */
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
     * of the GUI.
     */
    public JPanel createDynamicChoicePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(0, 0, 0, 0));
        panel.setBackground(GUIconstants.DARK_GREY);
        panel.setPreferredSize(new Dimension(300, 40));
        return panel;
    }

    /**
     * Create the panel, which contains the title image.
     *
     * @return a JPanel title image
     */
    private JLabel getTitleImage() {
        JLabel imgLabel = new JLabel();
        java.net.URL img = UniVoteVerifierRunner.class.getResource("/univoteTitle.jpeg");
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

    /**
     * Change the text of the description label.
     *
     * @param str The next text to show in the description label.
     */
    public void setDescription(String str) {
        vrfDescLabel.setText(str);
    }

    /**
     * Get the text of the description label.
     *
     * @return A String of the text in the description label.
     */
    public String getDescription() {
        return vrfDescLabel.getText();
    }
}
