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
package ch.bfh.univoteverifier.table;

import ch.bfh.univote.common.Choice;
import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.action.RemoveTabAction;
import ch.bfh.univoteverifier.common.Config;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.gui.GUIconstants;
import ch.bfh.univoteverifier.gui.ThreadManager;
import java.util.logging.Logger;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;

/**
 * This class manages the various JTabbedPanes which contain verification
 * results from various elections. It holds the tabbed panes in a list, and
 * implemented methods to append verification results to a given ResultTable.
 *
 * @author prinstin
 */
public class ResultTabbedPane extends JTabbedPane {

    private ArrayList<ResultTab> resultsPanels;
    private static final Logger LOGGER = Logger.getLogger(ResultTabbedPane.class.getName());
    private RemoveTabAction removeTabAction;
    private JPanel welcomePanel = new JPanel();
    private ResourceBundle rb;

    /**
     * Create an instance of this class.
     *
     * @param tm ThreadManager to be able to end the thread if a tab is closed
     * before all verifications have been completed.
     * @param cl ChangeListener to register. This is the consolePanel that needs
     * to know when to change the text in the console to correspond to the tab
     * currently being viewed.
     */
    public ResultTabbedPane(ThreadManager tm, ChangeListener cl) {
        rb = ResourceBundle.getBundle("error", GUIconstants.getLocale());

        welcomePanel = new JPanel();
        welcomePanel.setBackground(Color.WHITE);

        String welcomeTabText = rb.getString("welcome");
        this.addTab(welcomeTabText, welcomePanel);

        this.addChangeListener(cl);

        removeTabAction = new RemoveTabAction(this, tm);
        this.setBackground(Color.WHITE);
        resultsPanels = new ArrayList<>();
    }

    /**
     * Removes the Progress Bar and displays the text that the verification
     * process has finished.
     */
    public void completeVerification(String processID) {
        if (hasTabPane(processID)) {
            ResultTab rt = getTabPaneByName(processID);
            rt.completeVerification();
        }
    }

    /**
     * Add data to an appropriate TablePanel. Searches for a TablePanel with the
     * corresponding election ID.
     *
     * @param rs ResultSet contains the data to add.
     */
    public void addData(ResultSet rs) {
        if (hasTabPane(rs.getProcessID())) {
            LOGGER.log(Level.INFO, "TABBED PANE EXISTS, so GET INDEX OF IT: " + rs.getProcessID());
            ResultTab rtp = getTabPaneByName(rs.getProcessID());
            rtp.addData(rs);
            if (rs.getResult() == false) {
                //if a there was a problem in the verification, change tab text to red.
                LOGGER.log(Level.INFO, "NAME OF TAB TO GET: " + rs.getProcessID());
                int index = this.indexOfComponent(rtp);
                LOGGER.log(Level.INFO, "INDEX OF TAB TO GET: " + index);
                JPanel tabComponent = (JPanel) this.getTabComponentAt(index);
                tabComponent.setForeground(Color.red);
            }
        } else {
            createNewTab(rs.getProcessID(), rs.getEID());
            addData(rs);
        }
    }

    /**
     * Add the election results of the candidate results panel.
     *
     * @param crs The helper class which contains the results.
     */
    public void addElectionResults(CandidateResultSet crs) {

        LOGGER.log(Level.INFO, "ELECTION RESULTS RECEIVED BY TABBED PANE");
        if (!hasTabPane(crs.getProcessID())) {
            createNewTab(crs.getProcessID(), crs.getEID());

        }
        ResultTab rtp = getTabPaneByName(crs.getProcessID());
        Map<Choice, Integer> electionResults = crs.getElectionResult();
        rtp.addElectionResults(electionResults);

    }

    /**
     * Add data to a new tab.
     *
     * @param rs ResultSet contains the data to add.
     */
    public void createNewTab(String processID, String eID) {
        int numberOfVrfs = 0;
        boolean individualVrf = false;
        String vrfTypeString = processID.substring(0, 3);
        String append = "";
        if (vrfTypeString.equals("IND")) {
            append = "Ind: ";
            individualVrf = true;
            numberOfVrfs = getIndVrfCount();
        } else {
            numberOfVrfs = getUniVrfCount(processID, eID);
        }


        String thisProcessID = processID;
        String tabTitle = append + eID;
        ResultTab rt = new ResultTab(thisProcessID, individualVrf, numberOfVrfs);

        this.resultsPanels.add(rt);
        this.addTab(tabTitle, rt);

        int index = this.indexOfComponent(rt);
        TabBackground tabBackgroundComponent = new TabBackground(tabTitle, removeTabAction, processID);

        JPanel panel = ((JPanel) tabBackgroundComponent);
        this.setTabComponentAt(index, panel);
        this.setSelectedIndex(index);
    }

    /**
     * Gets the tab with a given
     */
    /**
     * Display and error that is specific to this election ID.
     *
     * @param errorMsg The error message to display.
     * @param eID The election ID for which to display this message.
     */
    public void showElectionSpecError(String errorMsg, String eID) {
        if (hasTabPane(eID)) {
            ResultTab rtp = getTabPaneByName(eID);
            rtp.showElectionSpecError(errorMsg);
        }
    }

    /**
     * Get the result tab panel with a given name.
     *
     * @param eID the name of the table to find.
     * @return The table whose name is eID.
     */
    public ResultTab getTabPaneByName(String processID) {
        for (ResultTab r : resultsPanels) {
            String processIDFound = r.getProcessID();
            if (processID.equals(processIDFound)) {
                return r;
            }
        }
        return null;
    }

    /**
     * Remove the table with a given name.
     *
     * @param processID the name of the table to find.
     * @return The table whose name is eID.
     */
    public boolean removeTabPaneByName(String processID) {
        if (!hasTabPane(processID)) {
            return false;
        }
        ResultTab rtByID = null;
        for (ResultTab rt : resultsPanels) {
            String tabProcessID = rt.getProcessID();
            if (processID.equals(tabProcessID)) {
                rtByID = rt;
            }
        }
        resultsPanels.remove(rtByID);
        return true;
    }

    /**
     * Check if a tab with a given eID is in the list.
     *
     * @param eID The name of the election ID to search for.
     * @return The table with the election ID as a name.
     */
    public boolean hasTabPane(String tabProcessID) {
        boolean found = false;
        if (resultsPanels.isEmpty()) {
            return found;
        }
        for (ResultTab r : resultsPanels) {

            String thisTabID = r.getProcessID();
            LOGGER.log(Level.INFO, "HAS TAB PANE, ResultPanel Tab ID:" + thisTabID);
            LOGGER.log(Level.INFO, "HAS TAB PANE, Tab ID to FIND" + tabProcessID);
            if (tabProcessID.equals(thisTabID)) {
                found = true;
            }
        }
        return found;
    }

    public int getUniVrfCount(String processID, String eID) {
        ElectionBoardProxy ebp = new ElectionBoardProxy(eID);
        int vrfs = 0;
        int tCount = 0;
        int mCount = 0;

        try {
            tCount = ebp.getElectionDefinition().getMixerId().size();
            mCount = ebp.getElectionDefinition().getTallierId().size();
        } catch (ElectionBoardServiceFault ex) {
            showElectionSpecError("ElectionBoardServiceFault", processID);
        }
        int mixerVrfs = Config.VERIFICATIONS_FOR_MIXER * mCount;
        int tallierVrfs = Config.VERIFICATIONS_FOR_TALLIER * tCount;
        vrfs = Config.UNIVERSAL_VERIFICATION_COUNT + mixerVrfs + tallierVrfs;

        return vrfs;

    }

    public int getIndVrfCount() {
        return Config.INDIVIDUAL_VERIFICATION_COUNT;
    }
}
