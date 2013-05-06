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
import ch.bfh.univoteverifier.common.ConsoleRunner;
import ch.bfh.univoteverifier.common.IFileManager;
import ch.bfh.univoteverifier.common.MainController;
import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.common.QRCode;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.verification.VerificationResult;
import ch.bfh.univoteverifier.verification.VerificationThread;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Creates the main window of the GUI and generates the components needed to see
 * the GUI and operate the program
 *
 * @author prinstin
 */
public class MainGUI extends JFrame {

    JFrame frame;
    JPanel vrfDescPanel, dynamicChoicePanel, masterPanel;
    TopPanel topPanel;
    MiddlePanel middlePanel;
    ConsolePanel consolePanel;
    ResultPanelManager resultPanelManager;
    MainController mc;
    VerificationListener sl;
    JLabel vrfDescLabel, choiceDescLabel;
    JButton btnStart, btnFileSelector;
    JRadioButton btnUni, btnInd;
    boolean selectionMade = false, GUIRunning=true;
    JComboBox comboBox;
    String[] eIDlist;
    String rawEIDlist;
    Preferences prefs;
    private static final Logger LOGGER = Logger.getLogger(MainGUI.class.getName());
    ResourceBundle rb;
    
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        MainGUI gui = new MainGUI(args);
        gui.setVisible(true);
    }

    /**
     * Construct the window and frame of this GUI and initialize certain base
     * variables.
     */
    public MainGUI(String[] args) {
        initResources();
        if (args.length > 0) {
              GUIRunning=false;
             Messenger msgr = createMessengerAddListener();
            ConsoleRunner cr = new ConsoleRunner(msgr, args);
        } else {
            setLookAndFeel();
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setMinimumSize(new Dimension(696, 400));

            createContentPanel();

            this.setJMenuBar(new VerificationMenuBar(this));
            this.setTitle(rb.getString("windowTitle"));
            this.pack();
        }
       
    }
    
   
    
	/**
	 * Toggle the visibility of the console-like panel which contains a
	 * JTextArea.
	 *
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
private void createContentPanel() {
        resetContentPanel();
    }

/**
 * Create or recreate the main content panel for this Frame Class.
 * This method is called when the program starts as well as if a change of locale is needed.
 * The entire GUI will is recreated.
 */
    public void resetContentPanel() {
        masterPanel = createUI();
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
    private void initResources() {
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
	 * @return a JPanel which will be set as the main content panel of the
	 * frame
	 */
	public JPanel createUI() {
		JPanel panel = new JPanel();

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        createComboBox();
        ButtonGroup btnGroup = new ButtonGroup();
        Messenger msgr = createMessengerAddListener();

       
        resultPanelManager = new ResultPanelManager();
        middlePanel = new MiddlePanel(resultPanelManager);
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.X_AXIS));
        middlePanel.setBackground(GUIconstants.GREY);

        createActions(msgr, btnGroup);
        
        topPanel = getTopPanel(btnGroup);
        consolePanel = new ConsolePanel();
        panel.add(topPanel);
        panel.add(middlePanel);
        
        return panel;
    }

    /**
     * Create a Messenger instance and register the listener in this class.
     *
     * @return Messenger
     */
    public Messenger createMessengerAddListener() {
        Messenger msgr = new Messenger();
        msgr.getStatusSubject().addListener(sl);
        return msgr;
    }

    /**
     * Create the actions that are used in this GUI.
     * @param msgr 
     */
    private void createActions(Messenger msgr, ButtonGroup btnGroup){
        ActionManager am = ActionManager.getInstance();
        IFileManager fm= new FileManager();
        Action changeLocaleAction = new ChangeLocaleAction(this);
        Action fileChooserAction = new FileChooserAction(msgr, fm);
        Action startAction = new StartAction(msgr, middlePanel, comboBox, btnGroup, fm);
        Action showConsoleAction = new ShowConsoleAction(this);
        
        am.addActions("fileChooser", fileChooserAction);
        am.addActions("changeLocale", changeLocaleAction);
        am.addActions("start", startAction);
        am.addActions("showConsole", showConsoleAction);
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
     * Create the components necessary to display the topPanel.
     *
     * @return a JPanel which contains other components to be shown in the main
     * window
     */
    private TopPanel getTopPanel(ButtonGroup btnGrp) {
        createBtnGrp(btnGrp);
        createStartButton();
        TopPanel panel = new TopPanel(btnUni, btnInd, btnStart, comboBox, btnGrp);
        return panel;
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
     * Create the button that select the verification type.
     */
    private void createBtnGrp(ButtonGroup btnGrp) {
        ActionManager am = ActionManager.getInstance();

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
     * This inner class represents the implementation of the observer pattern
     * for the status messages for the console and verification parts of the GUI.
     *
     * @author prinstin
     */
    class StatusUpdate implements VerificationListener {

        @Override
        public void updateStatus(VerificationResult ve) {
            if (GUIRunning) {
                if (ve.getVerificationEnum() == VerificationType.ERROR) {
                    consolePanel.appendToStatusText("\n" + ve.getMessage());
                } else if (ve.getVerificationEnum() == VerificationType.FATAL_ERROR) {
                    String text = "\n" + ve.getMessage();
//                    JOptionPane.showMessageDialog(masterPanel, text);
                    topPanel.setupErrorMsg(text);
                } else {
                    showResultInGUI(ve);
                }
            } else {
                if (ve.getVerificationEnum() == VerificationType.ERROR) {
                   LOGGER.log(Level.SEVERE, ve.getMessage());
                } else if (ve.getVerificationEnum() == VerificationType.FATAL_ERROR) {
                    String text = "\n" + ve.getMessage();
                    LOGGER.log(Level.SEVERE, text);
                } else {
                    showResultInTerminal(ve);
                }
            }
        }
    }
    
    /**
     * Display the incoming verification result information in the GUI
     * @param ve VerificationResult helper class containing verification information.
     */
    public void showResultInGUI(VerificationResult ve){
     String sectionName = ve.getSection().toString();
                Boolean result = ve.getResult();
                int code = ve.getVerificationEnum().getID();
                String vrfType = GUIconstants.getTextFromVrfCode(code);
                String eID="vsbfh-2013";
                ResultSet rs = new ResultSet(vrfType, result, sectionName, eID);
                resultPanelManager.addData(rs);
                String outputText = "\n" + vrfType + " ............. " + result;
                consolePanel.appendToStatusText(outputText);
    }
    
    /**
     * Show the verification information in the terminal.  This method is called if the program is being run from the terminal.
     * @param ve VerificationResult helper class containing verification information.
     */
    public void showResultInTerminal(VerificationResult ve) {
        Boolean result = ve.getResult();
        int code = ve.getVerificationEnum().getID();
        String vrfType = GUIconstants.getTextFromVrfCode(code);
        String eID = "vsbfh-2013";
        String outputText = "\n" +eID + " : "+ vrfType + " ............. " + result;
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, outputText);
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

    /**
     * Append some text to the console-like JTextArea.
     *
     * @param str The String of the text to append.
     */
    public void appendToConsole(String str){
        consolePanel.appendToStatusText(str);
    }

    /**
     * Set the look and feel of the GUI to the default of the system the program is running on.
     */
    public void setLookAndFeel(){
             try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
       }
 
}
