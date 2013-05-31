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
import ch.bfh.univoteverifier.action.RemoveTabAction;
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
     * Add data to an appropriate TablePanel. Searches for a TablePanel with the
     * corresponding election ID.
     *
     * @param rs ResultSet contains the data to add.
     */
    public void addData(ResultSet rs) {
        if (hasTabPane(rs.getProcessID())) {
            LOGGER.log(Level.OFF, "TABBED PANE EXISTS, so GET INDEX OF IT: " + rs.getProcessID());
            ResultTab rtp = getTabPaneByName(rs.getProcessID());
            rtp.addData(rs);
            if (rs.getResult() == false) {
                //if a there was a problem in the verification, change tab text to red.
                LOGGER.log(Level.OFF, "NAME OF TAB TO GET: " + rs.getProcessID());
                int index = this.indexOfComponent(rtp);
                LOGGER.log(Level.OFF, "INDEX OF TAB TO GET: " + index);
                JPanel tabComponent = (JPanel) this.getTabComponentAt(index);
                tabComponent.setForeground(Color.red);
            }
        } else {
            createNewTab(rs.getProcessID(), rs.getEID());
            addData(rs);
        }
    }

    public void addElectionResults(CandidateResultSet crs) {

        LOGGER.log(Level.OFF, "ELECTION RESULTS RECEIVED BY TABBED PANE");
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
        boolean individualVrf = false;
        String vrfTypeString = processID.substring(0, 3);
        String append = "";
        if (vrfTypeString.equals("IND")) {
            append = "Ind: ";
            individualVrf = true;
        }

        String thisProcessID = processID;
        String tabTitle = append + eID;
        ResultTab rt = new ResultTab(thisProcessID, individualVrf);

        this.resultsPanels.add(rt);
        this.addTab(tabTitle, rt);

        int index = this.indexOfComponent(rt);
        TabBackground tabPanel = new TabBackground(tabTitle, removeTabAction);

        JPanel panel = ((JPanel) tabPanel);
        this.setTabComponentAt(index, panel);
        this.setSelectedIndex(index);
    }

    public void showElectionSpecError(String errorMsg, String eID) {
        if (hasTabPane(eID)) {
            ResultTab rtp = getTabPaneByName(eID);
            rtp.showElectionSpecError(errorMsg);
        }

    }

    /**
     * Get the table with a given name.
     *
     * @param eID the name of the table to find.
     * @return The table whose name is eID.
     */
    public ResultTab getTabPaneByName(String processID) {
        for (ResultTab r : resultsPanels) {
            String processIDFound = r.getProcessID();
            if (processID.compareTo(processIDFound) == 0) {
                return r;
            }
        }
        return null;
    }

    /**
     * Remove the table with a given name.
     *
     * @param eID the name of the table to find.
     * @return The table whose name is eID.
     */
    public boolean removeTabPaneByName(String eID) {
        if (!hasTabPane(eID)) {
            return false;
        }
        ResultTab rFound = null;
        for (ResultTab r : resultsPanels) {
            String iTabID = r.getProcessID();
            if (eID.compareTo(iTabID) == 0) {
                rFound = r;
            }
        }
        resultsPanels.remove(rFound);
        return true;
    }

    /**
     * Check if a tab with a given eID is in the list.
     *
     * @param eID The name of the election ID to search for.
     * @return The table with the election ID as a name.
     */
    public boolean hasTabPane(String tabID) {
        boolean found = false;
        if (resultsPanels.isEmpty()) {
            return found;
        }
        for (ResultTab r : resultsPanels) {

            String thisTabID = r.getProcessID();
            LOGGER.log(Level.OFF, "HAS TAB PANE, ResultPanel Tab ID:" + thisTabID);
            LOGGER.log(Level.OFF, "HAS TAB PANE, Tab ID to FIND" + tabID);
            if (tabID.compareTo(thisTabID) == 0) {
                found = true;
            }
        }
        return found;
    }
}
