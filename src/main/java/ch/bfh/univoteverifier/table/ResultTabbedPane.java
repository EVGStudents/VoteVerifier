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
import ch.bfh.univoteverifier.gui.ThreadManager;
import java.util.logging.Logger;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Map;
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
        welcomePanel = new JPanel();
        welcomePanel.setBackground(Color.WHITE);
        this.addTab("Welcome", welcomePanel);

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
        //Find is pane with eID exists
        if (hasTabPane(rs.getEID())) {
            ResultTab rtp = getTabPaneByName(rs.getEID());
            rtp.addData(rs);
            if (rs.getResult() == false) {
                //if a there was a problem in the verification, change tab text to red.
                int index = this.indexOfTab(rs.getEID());
                JPanel tabComponent = (JPanel) this.getTabComponentAt(index);
                tabComponent.setForeground(Color.red);
            }
        } else {
            createNewTab(rs);
            addData(rs);
        }
    }

    public void addElectionResults(ResultSet rs) {
        if (hasTabPane(rs.getEID())) {
            ResultTab rtp = getTabPaneByName(rs.getEID());
            rtp.addElectionResults(rs.getElectionResult());
        } else {
            createNewTab(rs);
        }
    }

    /**
     * Add data to a new tab.
     *
     * @param rs ResultSet contains the data to add.
     */
    public void createNewTab(ResultSet rs) {
        String title = rs.getEID();
        ResultTab newRTP = new ResultTab(title);

        this.resultsPanels.add(newRTP);
        this.addTab(title, newRTP);

        int index = this.indexOfTab(title);
        TabBackground tabPanel = new TabBackground(title, removeTabAction);

        JPanel panel = ((JPanel) tabPanel);
        this.setTabComponentAt(index, panel);
        this.setSelectedIndex(index);
    }

    /**
     * Get the table with a given name.
     *
     * @param eID the name of the table to find.
     * @return The table whose name is eID.
     */
    public ResultTab getTabPaneByName(String eID) {
        for (ResultTab r : resultsPanels) {
            String thisEID = r.getEID();
            if (eID.compareTo(thisEID) == 0) {
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
            String thisEID = r.getEID();
            if (eID.compareTo(thisEID) == 0) {
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
    public boolean hasTabPane(String eID) {
        boolean found = false;
        if (resultsPanels.isEmpty()) {
            return found;
        }
        for (ResultTab r : resultsPanels) {
            String thisEID = r.getEID();
            if (eID.compareTo(thisEID) == 0) {
                found = true;
            }
        }
        return found;
    }
}
