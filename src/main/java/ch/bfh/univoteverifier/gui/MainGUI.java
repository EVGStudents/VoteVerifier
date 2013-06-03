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

import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.listener.VerificationMessage;
import ch.bfh.univoteverifier.listener.VerificationListener;
import ch.bfh.univoteverifier.listener.VerificationEvent;
import ch.bfh.univoteverifier.table.ResultTabbedPane;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.common.MessengerManager;
import java.awt.Dimension;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Creates the main window of the GUI and generates the components needed to see
 * the GUI and operate the program
 *
 * @author prinstin
 */
public class MainGUI extends JFrame {

    private JPanel masterPanel;
    private TopPanel topPanel;
    private MiddlePanel middlePanel;
    private ConsolePanel consolePanel;
    private ResultTabbedPane resultTabbedPane;
    private VerificationListener sl;
    private Preferences prefs;
    private static final Logger LOGGER = Logger.getLogger(MainGUI.class.getName());
    private ResourceBundle rb;
    private ResultProcessor resultProccessor;
    private ThreadManager tm;
    private MessengerManager mm;

    /**
     * Construct the window and frame of this GUI and initialize certain base
     * variables.
     */
    public MainGUI() {
        tm = new ThreadManager();
        initResources();
        setLookAndFeel();

        java.net.URL imgURL = VoteVerifier.class.getResource("/iconVoteVerifier.jpg");
        ImageIcon img = new ImageIcon(imgURL);
        this.setIconImage(img.getImage());

        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(696, 400));

        createContentPanel();

        this.setJMenuBar(new VerificationMenuBar(this));
        this.setTitle(rb.getString("windowTitle"));
        this.pack();
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
     * Get the list of possible election IDs.
     */
    public String[] getElectionIDList() {
        String[] eidList = null;
        try {
            ElectionBoardProxy ebp = new ElectionBoardProxy("");
            List<String> eids = ebp.getElectionsID().getElectionId();
            eidList = new String[eids.size()];
            eids.toArray(eidList);
        } catch (ElectionBoardServiceFault ex) {
            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
        }

        return eidList;
    }

    /**
     * Create or recreate the main content panel for this Frame Class. This
     * method is called when the program starts as well as if a change of locale
     * is needed. The entire GUI will is recreated.
     */
    public void resetContentPanel() {
        tm.killAllThreads();
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
        sl = new StatusUpdate();
        mm = new MessengerManager(sl);
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

        consolePanel = new ConsolePanel();

        resultTabbedPane = new ResultTabbedPane(tm, consolePanel);
        resultProccessor = new ResultProcessor(consolePanel, resultTabbedPane);

        topPanel = new TopPanel();

        String[] eidList = getElectionIDList();

        middlePanel = new MiddlePanel(resultTabbedPane, eidList, mm, tm);
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.X_AXIS));
        middlePanel.setBackground(GUIconstants.GREY);

        panel.add(topPanel);
        panel.add(middlePanel);

        return panel;
    }

    /**
     * This inner class represents the implementation of the observer pattern
     * for the status messages for the console and verification parts of the
     * GUI.
     *
     * @author prinstin
     */
    class StatusUpdate implements VerificationListener {

        @Override
        public void updateStatus(VerificationEvent ve) {
            if (ve.getVm() == VerificationMessage.ELECTION_SPECIFIC_ERROR) {
                resultTabbedPane.showElectionSpecError(ve.getMsg(), ve.getProcessID());
                consolePanel.appendToStatusText("\n" + ve.getMsg(), ve.getProcessID());
            } else if (ve.getVm() == VerificationMessage.SETUP_ERROR) {
                String text = "\n" + ve.getMsg();
                middlePanel.setupErrorMsg(text);
            } else if (ve.getVm() == VerificationMessage.FILE_SELECTED) {
                middlePanel.showFileSelected(ve.getMsg());
            } else if (ve.getVm() == VerificationMessage.SHOW_CONSOLE) {
                showConsole(ve.getConsoleSelected());
            } else if (ve.getVm() == VerificationMessage.VRF_FINISHED) {
                String processID = ve.getProcessID();
                tm.killThread(processID);
            } else {
                resultProccessor.showResultInGUI(ve);
            }
        }
    }

    /**
     * Set the look and feel of the GUI to the default of the system the program
     * is running on.
     */
    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainGUI.class
                    .getName()).log(Level.SEVERE, ex.getMessage());
        } catch (InstantiationException ex) {
            Logger.getLogger(MainGUI.class
                    .getName()).log(Level.SEVERE, ex.getMessage());
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MainGUI.class
                    .getName()).log(Level.SEVERE, ex.getMessage());
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MainGUI.class
                    .getName()).log(Level.SEVERE, ex.getMessage());
        }
    }
}
