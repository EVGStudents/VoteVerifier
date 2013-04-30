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

import action.ActionManager;
import action.ChangeLocaleAction;
import action.FileChooserAction;
import action.StartAction;
import ch.bfh.univoteverifier.common.Config;
import ch.bfh.univoteverifier.common.MainController;
import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.verification.VerificationEvent;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * Creates the main window of the GUI and generates the components needed to see
 * the GUI and operate the program
 *
 * @author prinstin
 */
public class MainGUI extends JFrame {

    JFrame frame;
    JPanel  vrfDescPanel, dynamicChoicePanel, innerPanel;
    TopPanel topPanel;
    ConsolePanel consolePanel;
    VrfPanel sysSetupPanel, electSetupPanel, elecPrepPanel, elecPeriodPanel, mixerTallierPanel, activeVrfPanel;
    MainController mc;
    VerificationListener sl;
    JLabel vrfDescLabel, choiceDescLabel;
    VerificationButton btnInd, btnUni;
    VerificationButton[] btns = {btnInd, btnUni};
    JButton btnStart, btnFileSelector;
    boolean uniVrfSelected = false, selectionMade = false;
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
    
    public MainGUI(){
        initComponents();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(696, 400));
        
        
        JPanel masterPanel = createUI();
        this.setJMenuBar(new VerificationMenuBar(consolePanel));
        masterPanel.setOpaque(true); //content panes must be opaque
        this.setContentPane(masterPanel);
        this.setTitle(rb.getString("windowTitle"));
        this.pack();
    }

    /**
     * instantiates the basic building blocks of the program such as the
     * controllers and displays the wi tnndow of the GUI.
     */
    public void initComponents() {
        rb = ResourceBundle.getBundle("error", Locale.ENGLISH);
        prefs = Preferences.userNodeForPackage(MainGUI.class);
        rawEIDlist = prefs.get("eIDList", "Bern Zurich vsbfh-2013");
        Pattern pattern = Pattern.compile("\\s");
        eIDlist = pattern.split(rawEIDlist);
        mc = new MainController();
        sl = new StatusUpdate();
    }

    /**
     * creates the main components of the main window. The main window is
     * divided into three parts: northPanel, and in the middle hte verification
     * panel (vrfPanel) and at the bottom the statusPanel
     *
     * @return a JPanel which will be set as the main content panel of the frame
     */
    public JPanel createUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        createComboBox();
        createDynamicChoicePanel();
        

        innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.X_AXIS));
        innerPanel.setBackground(GUIconstants.GREY);

        consolePanel = new ConsolePanel();
        
         Action changeLocaleAction = new ChangeLocaleAction(consolePanel);
        Action fileChooserAction = new FileChooserAction(innerPanel, consolePanel, qrCodeFile);
        ActionManager.getInstance().addActions("fileChooser", fileChooserAction);
        ActionManager.getInstance().addActions("changeLocale", changeLocaleAction);
        
        topPanel = getTopPanel();

        panel.add(topPanel);
        panel.add(new MiddlePanel(innerPanel));
        panel.add(consolePanel);
        
        return panel;
    }

    /**
     * create the components necessary to display the northPanel
     *
     * @return a JPanel which contains other components to be shown in the main
     * window
     */
    public TopPanel getTopPanel() {
        createUniVrfButton();
        createIndVrfButton();
        createStartButton();
        
        TopPanel panel = new TopPanel(btnUni, btnInd, btnStart);
        
        createFileSelectButton();
        
        return panel;
    }

    /**
     * Creates a panel that whose contents are changed based on selections made
     * by the user. This panel will be displayed in the verification area
     * (middle) of the GUI
     */
    public void createDynamicChoicePanel() {
        dynamicChoicePanel = new JPanel();
        dynamicChoicePanel.setBackground(GUIconstants.DARK_GREY);
        dynamicChoicePanel.setPreferredSize(new Dimension(300, 40));

        choiceDescLabel = new JLabel();
        choiceDescLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dynamicChoicePanel.add(choiceDescLabel);
    }

    /**
     * change the language displayed in the GUI
     */
    public void changeLocale(String str) {
        Locale loc = new Locale(str);
        rb = ResourceBundle.getBundle("error", loc);

        btnStart.setText(rb.getString("start"));
        btnUni.setText(rb.getString("btnUni"));
        btnInd.setText(rb.getString("btnInd"));
        this.setTitle(rb.getString("windowTitle"));
        choiceDescLabel.setText(rb.getString("descQRCode"));
        btnFileSelector.setText(rb.getString("selectFile"));
    }

    /**
     * A JPanel that is used to show the verification results in the
     * verification panel.
     */
    public class VrfPanel extends JPanel {

        private String name;
        JPanel titlePanel, contentPanel;
        JLabel label;

        VrfPanel(String s) {
            super();
            name = s;
//            dummyEllipsePanel = createDummyEllipsePanel();
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
         * @param str the text for the verification result to be displayed to in
         * the GUI
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
            innerPanel.validate();
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

    /**
     * creates the comboBox that allows new election IDs to be inputed as well
     * as the selection of previously used election IDs
     */
    public void createComboBox() {
        comboBox = new JComboBox(eIDlist);
        comboBox.setEditable(true);
        comboBox.setSelectedIndex(2);
        comboBox.setSize(30, 50);
        comboBox.setFont(new Font("Serif", Font.PLAIN, 10));
    }

    /**
     * create the button that shows the information and buttons needed to start
     * universal verification
     *
     * @return button to choose universal verification a grey background and
     * deactivated focused effects
     */
    public JButton createUniVrfButton() {
        String descUni = rb.getString("descUni");
        String name = rb.getString("btnUni");
        btnUni = new VerificationButton(name, descUni);
        btnUni.addMouseListener(
                new MouseListener() {
                    
                    String oldText;
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                primeDescPanel();
                 oldText = rb.getString("descUni");
                choiceDescLabel.setText(oldText);
                dynamicChoicePanel.add(comboBox);
                innerPanel.repaint();
                uniVrfSelected = true;
                btnInd.depress();
                btnUni.press();
                consolePanel.setStatusText(rb.getString("btnUni"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                oldText= topPanel.getDescription();
                topPanel.setDescription(rb.getString("descUni"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                topPanel.setDescription(oldText);
                
               
            }
        });
        return btnUni;
    }

    /**
     * create the button that shows the information and buttons needed to start
     * individual verification
     *
     * @return JButton to choose individual verification a grey background and
     * deactivated focused effects
     */
    public JButton createIndVrfButton() {

        String descInd = rb.getString("descInd");
        String name = rb.getString("btnInd");
        btnInd = new VerificationButton(name, descInd);

        btnInd.addMouseListener(
                new MouseListener() {
                         String oldText;
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                primeDescPanel();
                oldText = rb.getString("descInd");
                choiceDescLabel.setText(oldText);
                dynamicChoicePanel.add(btnFileSelector);
                innerPanel.repaint();
                uniVrfSelected = false;
                btnUni.depress();
                btnInd.press();
                consolePanel.setStatusText(rb.getString("btnInd"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                oldText = topPanel.getDescription();
                topPanel.setDescription(rb.getString("descInd"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                topPanel.setDescription(oldText);
            }
        });

        return btnInd;
    }

    /**
     * create the button that shows a pop-up file selection window so that the
     * user can select a file which contains a QRCode image
     */
    public void createFileSelectButton() {
        btnFileSelector = new JButton(rb.getString("selectFile"));
        btnFileSelector.setAction(ActionManager.getInstance().getAction("fileChooser"));
    }

    /**
     * sets the content for the panel that changes when universal or individual
     * buttons are pressed
     */
    public void primeDescPanel() {
        if (!selectionMade) {
            innerPanel.add(dynamicChoicePanel);
        }
        selectionMade = true;

        dynamicChoicePanel.removeAll();
        dynamicChoicePanel.add(choiceDescLabel);
    }

    /**
     * create the button that starts the verification process
     *
     * @return JButton the start button
     */
    public JButton createStartButton() {
        btnStart = new JButton("START");
        btnStart.setBackground(GUIconstants.BLUE);
        Messenger msgr = new Messenger();
        msgr.getStatusSubject().addListener(sl);
        StartAction a = new StartAction(msgr, consolePanel, innerPanel);
        btnStart.setAction(a);
        return btnStart;
    }

    /**
     * This inner class represents the implementation of the observer pattern
     * for the status messages for the console and verification parts of the gui
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
                    activeVrfPanel = new VrfPanel(sectionName);
                    innerPanel.add(activeVrfPanel);
                } else if (!activeVrfPanel.getName().equals(sectionName)) {
                    activeVrfPanel = new VrfPanel((sectionName));
                    innerPanel.add(activeVrfPanel);
                }
                Boolean result = ve.getResult();
                int code = ve.getVerificationEnum().getID();
                String vrfType = getTextFromVrfCode(code);
                String outputText = "\n" + vrfType + " ............. " + result;
                consolePanel.appendToStatusText(outputText);
                activeVrfPanel.addResultPanel(vrfType, result);
            }
        }
    }

    /**
     * turns the vrfCode into a text string that is shown in the GUI
     *
     * @param code int value which corresponds to a verification type
     * @return the user-friendly text the describes a verification step
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
