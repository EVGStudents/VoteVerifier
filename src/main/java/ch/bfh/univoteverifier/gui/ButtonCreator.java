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
import ch.bfh.univoteverifier.action.FileChooserAction;
import ch.bfh.univoteverifier.action.SelectIndVrfAction;
import ch.bfh.univoteverifier.action.SelectUniVrfAction;
import ch.bfh.univoteverifier.action.ShowConsoleAction;
import ch.bfh.univoteverifier.action.StartAction;
import ch.bfh.univoteverifier.common.IFileManager;
import ch.bfh.univoteverifier.common.Messenger;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.util.ResourceBundle;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

/**
 *
 * @author prinstin
 */
public class ButtonCreator {

    private JComboBox comboBox;
    private JPanel buttonPanel;
    private ResourceBundle rb;
    private JButton btnStart, btnFileSelector;
    private JRadioButton btnUni, btnInd;

    public ButtonCreator(MiddlePanel middlePanel, Messenger msgr, ThreadManager tm, String[] eIDlist) {
        rb = ResourceBundle.getBundle("error", GUIconstants.getLocale());

        createComboBox(eIDlist);
        createActions(msgr, tm, middlePanel);
        createButtons();
        this.buttonPanel = createButtonPanel(middlePanel);
    }

    /**
     * Create the actions that are used in this GUI.
     *
     * @param msgr
     */
    private void createActions(Messenger msgr, ThreadManager tm, MiddlePanel middlePanel) {
        ActionManager am = ActionManager.getInstance();
        IFileManager fm = new FileManager();
        Action fileChooserAction = new FileChooserAction(middlePanel, msgr, fm);
        Action startAction = new StartAction(msgr, middlePanel, comboBox, fm, tm);
        Action showConsoleAction = new ShowConsoleAction(msgr);

        am.addActions("fileChooser", fileChooserAction);
        am.addActions("start", startAction);
        am.addActions("showConsole", showConsoleAction);
    }

    /**
     * Create the buttons that select the verification type.
     */
    private void createButtons() {
        btnFileSelector = new JButton(rb.getString("selectFile"));
        btnFileSelector.setAction(ActionManager.getInstance().getAction("fileChooser"));

        btnStart = new JButton("START");
        btnStart.setBackground(GUIconstants.BLUE);
        btnStart.setAction(ActionManager.getInstance().getAction("start"));

        ButtonGroup btnGrp = new ButtonGroup();

        btnUni = new JRadioButton();
        btnUni.setBackground(GUIconstants.GREY);
        btnUni.setName("btnUni");

        btnInd = new JRadioButton();
        btnInd.setBackground(GUIconstants.GREY);
        btnInd.setName("btnInd");

        btnGrp.add(btnUni);
        btnGrp.add(btnInd);
        btnUni.setSelected(true);
    }

    /**
     * Creates the comboBox that allows new election IDs to be inputed as well
     * as the selection of previously used election IDs.
     */
    public void createComboBox(String[] eIDlist) {
        comboBox = new JComboBox(eIDlist);
        comboBox.setEditable(true);
        comboBox.setSelectedIndex(0);
        comboBox.setSize(30, 50);
        comboBox.setFont(new Font("Serif", Font.PLAIN, 10));
    }

    /**
     * Create the button panel with the verification buttons in it.
     *
     * @return JPanel the panel, which contains the buttons in the topPanel.
     */
    public JPanel createButtonPanel(MiddlePanel middlePanel) {
        Action selectUniVrfAction = new SelectUniVrfAction(middlePanel);
        ActionManager.getInstance().addActions("selectUniVrf", selectUniVrfAction);
        btnUni.setAction(selectUniVrfAction);

        Action selectIndVrfAction = new SelectIndVrfAction(middlePanel);
        ActionManager.getInstance().addActions("selectIndVrf", selectIndVrfAction);
        btnInd.setAction(selectIndVrfAction);


        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.LIGHT_GRAY);
        int insets = 10;
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 6;
        c.insets = new Insets(insets, insets, insets, insets);
        c.anchor = GridBagConstraints.CENTER;

        JLabel startLabel = new JLabel(rb.getString("welcomeText2"));
        startLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(startLabel, c);


        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = .35;
        c.anchor = GridBagConstraints.CENTER;
        panel.add(Box.createHorizontalGlue(), c);


        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(insets, insets, insets, insets);
        c.anchor = GridBagConstraints.CENTER;

        panel.add(btnUni, c);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 1;
        c.insets = new Insets(insets, insets, insets, insets);
        c.anchor = GridBagConstraints.CENTER;
        panel.add(btnInd, c);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 1;
        c.weightx = .3;
        c.anchor = GridBagConstraints.CENTER;
        panel.add(Box.createHorizontalGlue(), c);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 4;
        c.gridy = 1;
        c.insets = new Insets(insets, insets, insets, insets);
        c.anchor = GridBagConstraints.CENTER;
        panel.add(btnStart, c);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 5;
        c.gridy = 1;
        c.weightx = .35;
        c.anchor = GridBagConstraints.CENTER;
        panel.add(Box.createHorizontalGlue(), c);

        return panel;
    }

    public JComboBox getComboBox() {
        return comboBox;
    }

    public JPanel getButtonPanel() {
        return buttonPanel;
    }

    public JButton getBtnFileSelector() {
        return btnFileSelector;
    }

    public JRadioButton getUniBtn() {
        return btnUni;
    }

    /**
     * This inner class holds a reference to a file expected to be QRCode and
     * which is sent to the verification thread when individual verification is
     * selected.
     *
     * @author prinstin
     */
    class FileManager implements IFileManager {

        File file;

        @Override
        public File getFile() {
            return file;
        }

        @Override
        public void setFile(File file) {
            this.file = file;
        }
    }
}
