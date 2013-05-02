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
import ch.bfh.univoteverifier.action.ChangeLocaleAction;
import ch.bfh.univoteverifier.action.FileChooserAction;
import ch.bfh.univoteverifier.action.ShowConsoleAction;
import ch.bfh.univoteverifier.action.StartAction;
import ch.bfh.univoteverifier.common.Config;
import ch.bfh.univoteverifier.common.MainController;
import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.verification.VerificationEvent;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

/**
 * Creates the main window of the GUI and generates the components needed to see
 * the GUI and operate the program
 *
 * @author prinstin
 */
public class MainGUI extends JFrame {

    JFrame frame;
    JPanel vrfDescPanel, dynamicChoicePanel, innerPanel;
    TopPanel topPanel;
    ConsolePanel consolePanel;
    ResultPanel activeVrfPanel;
    MainController mc;
    VerificationListener sl;
    JLabel vrfDescLabel, choiceDescLabel;
    JButton btnStart, btnFileSelector;
    JRadioButton btnUni, btnInd;
    boolean selectionMade = false;
    JComboBox comboBox;
    String[] eIDlist;
    String rawEIDlist;
    Preferences prefs;
    JScrollPane vrfScrollPanel;
    private final Properties prop = new Properties();
    private static final Logger LOGGER = Logger.getLogger(MainGUI.class.getName());
    ResourceBundle rb;
    File qrCodeFile;

    /**
     * @param args
     */
    public static void main(String[] args) {
        MainGUI gui = new MainGUI();
        gui.setVisible(true);
    }

    /**
     * Construct the window and frame of this GUI and initialize certain base variables.
     */
    public MainGUI() {
        initResources();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(696, 400));

        createContentPanel();

        this.setJMenuBar(new VerificationMenuBar(this));
        this.setTitle(rb.getString("windowTitle"));
        this.pack();
    }

    /**
     * Toggle the visibility of the console-like panel which contains a JTextArea.
     * @param show Boolean of true corresponds to showing the panel.
     */
    public void showConsole(boolean show) {
        if (show) {
            this.getContentPane().add(consolePanel);
        } else {
            this.getContentPane().remove(consolePanel);
        }
        this.validate();
        this.repaint();
    }

    /**
     * create the main content panel for this Frame Class.
     */
public void createContentPanel() {
        resetContentPanel();
    }

/**
 * Create or recreate the main content panel for this Frame Class.
 * This method is called when the program starts as well as if a change of locale is needed.
 * The entire GUI will is recreated.
 */
    public void resetContentPanel() {
        JPanel masterPanel = createUI();
        masterPanel.setOpaque(true); //content panes must be opaque
        this.setContentPane(masterPanel);
        initResources();
        this.setJMenuBar(new VerificationMenuBar(this));
        this.validate();
        this.repaint();
    }

    /**
     * Instantiates some basic resources needed in this class.
     */
    public void initResources() {
        rb = ResourceBundle.getBundle("error", GUIconstants.getLocale());
        prefs = Preferences.userNodeForPackage(MainGUI.class);
        rawEIDlist = prefs.get("eIDList", "Bern Zurich vsbfh-2013");
        Pattern pattern = Pattern.compile("\\s");
        eIDlist = pattern.split(rawEIDlist);
        mc = new MainController();
        sl = new StatusUpdate();
    }

    /**
     * Creates the main components of the main window. The main window is
     * divided into three parts: topPanel, middlePanel, and optionally a
     * consolePanel can be added at the bottom.
     *
     * @return a JPanel which will be set as the main content panel of the frame
     */
    public JPanel createUI() {
        JPanel panel = new JPanel();
        
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        createComboBox();
        ButtonGroup btnGroup = new ButtonGroup();
        Messenger msgr = new Messenger();
        msgr.getStatusSubject().addListener(sl);

        innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.X_AXIS));
        innerPanel.setBackground(GUIconstants.GREY);

        createActions(msgr, btnGroup);
        
        topPanel = getTopPanel(btnGroup);
        consolePanel = new ConsolePanel();
        panel.add(topPanel);
        panel.add(new MiddlePanel(innerPanel));
        
        return panel;
    }


    /**
     * Create the actions that are used in this GUI.
     * @param msgr 
     */
    private void createActions(Messenger msgr, ButtonGroup btnGroup){
        ActionManager am = ActionManager.getInstance();
        Action changeLocaleAction = new ChangeLocaleAction(this);
        Action fileChooserAction = new FileChooserAction(this, qrCodeFile);
        Action startAction = new StartAction(msgr, innerPanel, comboBox, btnGroup, qrCodeFile);
        Action showConsoleAction = new ShowConsoleAction(this);
        
        am.addActions("fileChooser", fileChooserAction);
        am.addActions("changeLocale", changeLocaleAction);
        am.addActions("start", startAction);
        am.addActions("showConsole", showConsoleAction);
    }
    
    
    /**
     * Create the components necessary to display the topPanel.
     *
     * @return a JPanel which contains other components to be shown in the main
     * window
     */
    private TopPanel getTopPanel(ButtonGroup btnGrp) {
        createBtnGrpUniInd(btnGrp);
        createStartButton();
        TopPanel panel = new TopPanel(btnUni, btnInd, btnStart, comboBox, btnGrp);
        return panel;
    }

    /**
     * Creates the comboBox that allows new election IDs to be inputed as well
     * as the selection of previously used election IDs.
     */
    private void createComboBox() {
        comboBox = new JComboBox(eIDlist);
        comboBox.setEditable(true);
        comboBox.setSelectedIndex(2);
        comboBox.setSize(30, 50);
        comboBox.setFont(new Font("Serif", Font.PLAIN, 10));
    }

    /**
     * Create the button that shows the information and buttons needed to start
     * universal verification.
     */
    private void createBtnGrpUniInd(ButtonGroup btnGrp) {
        btnUni = new JRadioButton();
        btnUni.setText("btnUni");
        ActionManager am = ActionManager.getInstance();
        String name = rb.getString("btnUni");
        btnUni = new JRadioButton(name);
        btnUni.setBackground(GUIconstants.GREY);

        name = rb.getString("btnInd");
        btnInd = new JRadioButton(name);
        btnInd.setBackground(GUIconstants.GREY);
        btnInd.setText("btnInd");

        btnGrp.add(btnUni);
        btnGrp.add(btnInd);
        btnUni.setSelected(true);
        
        for (Enumeration<AbstractButton> buttons = btnGrp.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            LOGGER.log(Level.INFO, "createBtnGroup", button.getName());
            if (button.isSelected()) {
                LOGGER.log(Level.INFO, "createBtnGroup is selected", button.getName());
            }
        }

      
    }


    /**
     * Create the button that starts the verification process.
     *
     * @return JButton the start button
     */
    private JButton createStartButton() {
        btnStart = new JButton("START");
        btnStart.setBackground(GUIconstants.BLUE);
        btnStart.setAction(ActionManager.getInstance().getAction("start"));
        return btnStart;
    }

    /**
     * This inner class represents the implementation of the observer pattern
     * for the status messages for the console and verification parts of the GUI.
     *
     * @author prinstin
     */
    class StatusUpdate implements VerificationListener {

        @Override
        public void updateStatus(VerificationEvent ve) {

            if (ve.getVerificationEnum() == VerificationType.ERROR) {
                consolePanel.appendToStatusText("\n" + ve.getMessage());
            } else {
                String sectionName = ve.getSection().toString();
                if (activeVrfPanel == null) {
                    activeVrfPanel = new ResultPanel(sectionName);
                    innerPanel.add(activeVrfPanel);
                } else if (!activeVrfPanel.getName().equals(sectionName)) {
                    activeVrfPanel = new ResultPanel((sectionName));
                    innerPanel.add(activeVrfPanel);
                }
                Boolean result = ve.getResult();
                int code = ve.getVerificationEnum().getID();
                String vrfType = getTextFromVrfCode(code);
                String outputText = "\n" + vrfType + " ............. " + result;
                consolePanel.appendToStatusText(outputText);
                activeVrfPanel.addResultPanel(vrfType, result);
                innerPanel.validate();
            }
        }
    }
    
    /**
     * Append some text to the console-like JTextArea.
     * @param str The String of the text to append.
     */
    public void appendToConsole(String str){
        consolePanel.appendToStatusText(str);
    }

    /**
     * Turns the vrfCode into a text string that is shown in the GUI.
     *
     * @param code The int value which corresponds to a verification type.
     * @return The user-friendly text that describes a verification step.
     */
    public String getTextFromVrfCode(int code) {
        try {
            prop.load(new FileInputStream("src/main/java/ch/bfh/univoteverifier/resources/messages.properties"));
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }

        return (String) prop.getProperty(String.valueOf(code));
    }
}
