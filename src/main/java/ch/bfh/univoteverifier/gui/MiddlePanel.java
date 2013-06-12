/*
 *
 *  Copyright (c) 2013 Berner Fachhochschule, Switzerland.
 *   Bern University of Applied Sciences, Engineering and Information Technology,
 *   Research Institute for Security in the Information Society, E-Voting Group,
 *   Biel, Switzerland.
 *
 *   Project independent VoteVerifier.
 *
 */
package ch.bfh.univoteverifier.gui;

import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.common.MessengerManager;
import ch.bfh.univoteverifier.table.ResultTabbedPane;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.File;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

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

    private JPanel tabPanel, backgroundPanel, welcomePanel, inputPanel, inputIndPanel, inputUniPanel;
    private static final Logger LOGGER = Logger.getLogger(MiddlePanel.class.getName());
    private JLabel vrfDescLabel;
    private ResourceBundle rb;
    private JLabel fileSelectedLabel, errorLabel;
    private ButtonCreator buttonCreator;

    /**
     * Create an instance of this class.
     *
     * @param resultTabbedPane the content panel for verification results is
     * added into a scroll pane.
     */
    public MiddlePanel(ResultTabbedPane resultTabbedPane, String[] eIDlist, MessengerManager mm, ThreadManager tm, boolean networkUp) {
        rb = ResourceBundle.getBundle("error", GUIconstants.getLocale());
        buttonCreator = new ButtonCreator(this, mm, tm, eIDlist);

        String welcomeTabText = rb.getString("welcome");
        int index = resultTabbedPane.indexOfTab(welcomeTabText);
        tabPanel = (JPanel) resultTabbedPane.getComponentAt(index);
        tabPanel.setBackground(Color.WHITE);
        tabPanel.setLayout(new GridLayout(1, 1));
        int border = 45;
        tabPanel.setBorder(new EmptyBorder(border, border, border, border)); //top left bottom right

        welcomePanel = new JPanel();
        welcomePanel.setBackground(Color.LIGHT_GRAY);
        welcomePanel.setLayout(new GridBagLayout());
        welcomePanel.setBorder(new EtchedBorder());

        GridBagConstraints c = new GridBagConstraints();
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = .4;
        welcomePanel.add(Box.createHorizontalGlue(), c);

        c.gridx = 0;
        c.gridy = 1;
        JPanel buttonPanel = buttonCreator.getButtonPanel();
        welcomePanel.add(buttonPanel, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.weighty = .2;
        inputPanel = createInputPanel();
        welcomePanel.add(inputPanel, c);

        errorLabel = new JLabel();
        errorLabel.setPreferredSize(new Dimension(300, 100));
        errorLabel.setAlignmentX(SwingConstants.CENTER);
        errorLabel.setForeground(Color.red);
        if (!networkUp) {
            String networkError = rb.getString("networkProblem");
            errorLabel.setText(networkError);

        }

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        c.weighty = .2;
        welcomePanel.add(errorLabel, c);


        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 4;
        c.weighty = .7;
        welcomePanel.add(Box.createHorizontalGlue(), c);

        tabPanel.add(welcomePanel);

        this.setLayout(new GridLayout(1, 1));
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(696, 500));
        this.setBorder(new EmptyBorder(10, 30, 10, 30)); //top left bottom right
        resultTabbedPane.setBackground(Color.WHITE);
        this.add(resultTabbedPane);
    }

    public JPanel createInputPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new EtchedBorder());
//        panel.setBorder(new EmptyBorder(20, 20, 20, 20)); //top left bottom right
        inputUniPanel = createInputUniPanel();
        inputIndPanel = createInputIndPanel();
        panel.add(inputUniPanel);
        return panel;
    }

    /**
     * Create the panel that contains the input controls and information for
     * universal verification.
     *
     * @return The panel with options for individual verification.
     */
    public JPanel createInputUniPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;

        vrfDescLabel = new JLabel(rb.getString("descUni"));
        vrfDescLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(vrfDescLabel, c);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;
        panel.add(buttonCreator.getComboBox(), c);
        return panel;
    }

    /**
     *
     * Create the panel that contains the input controls and information for
     * universal verification.
     *
     * @return The panel with options for individual verification.
     */
    public JPanel createInputIndPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;

        String html1 = "<html><body style='width: ";
        String html2 = "px'>";
        vrfDescLabel = new JLabel();
        String text = rb.getString("descInd") + "<br>" + rb.getString("pleaseSelectFile");
        vrfDescLabel.setText(html1 + "300" + html2 + text);
        vrfDescLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(vrfDescLabel, c);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;
        panel.add(buttonCreator.getBtnFileSelector(), c);

        fileSelectedLabel = new JLabel();
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.insets = new Insets(20, 0, 0, 0);
        c.anchor = GridBagConstraints.CENTER;
        panel.add(fileSelectedLabel, c);
        return panel;
    }

    /**
     * Display the file that was selected in the GUI
     */
    public void showFileSelected(String filePath) {
        fileSelectedLabel.setText(rb.getString("descIndFileSelected") + " " + filePath);
        fileSelectedLabel.setForeground(Color.BLACK);
    }

    /**
     * Remove the file information when a verification has begun.
     */
    public void resetFileText() {
        fileSelectedLabel.setText("");
    }

    /**
     * Set the error message in the error panel of the welcome screen.
     *
     * @param text The error message to display.
     */
    public void setupErrorMsg(String text) {
        String html1 = "<html><body style='width: ";
        String html2 = "px'>";
        errorLabel.setText(html1 + "300" + html2 + text);
        errorLabel.setForeground(new Color(240, 70, 20));
    }

    /**
     * Change the state of the GUI to show the selection choices for Universal
     * Verification.
     */
    public void uniVrfSelected() {
        inputPanel.removeAll();
        inputPanel.add(inputUniPanel);
        setupErrorMsg("");
        panelModified();
    }

    /**
     * Change the state of the GUI to show the selection choices for Individual
     * Verification.
     */
    public void indVrfSelected() {
        inputPanel.removeAll();
        inputPanel.add(inputIndPanel);
        setupErrorMsg("");
        panelModified();
    }

    /**
     * Repaints and revalidates the GUI when changes have been made that must be
     * shown.
     */
    public void panelModified() {
        welcomePanel.revalidate();
        welcomePanel.repaint();
    }

    /**
     * Change the text of the description label.
     *
     * @param str The next text to show in the description label.
     */
    public void setDescription(String str) {
        String html1 = "<html><body style='width: ";
        String html2 = "px'>";
        vrfDescLabel.setText(html1 + "300" + html2 + str);
//        vrfDescLabel.setText(str);
    }

    /**
     * Get the text of the description label.
     *
     * @return A String of the text in the description label.
     */
    public String getDescription() {
        return vrfDescLabel.getText();
    }

    public String getSelectedVrfType() {
        if (buttonCreator.getUniBtn().isSelected()) {
            return "btnUni";
        } else {
            return "btnInd";
        }
    }
}
