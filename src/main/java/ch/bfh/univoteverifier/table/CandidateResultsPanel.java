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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * This panel contains the election results for the number of votes each
 * candidate received.
 *
 * @author prinstin
 */
public class CandidateResultsPanel extends JPanel {

    private ResultTableModel tableModel;
    private ArrayList<SectionResultsTable> tables;
    private static final Logger LOGGER = Logger.getLogger(ResultTablesContainer.class.getName());
    private CandidateResultsTable activeTable;

    /**
     * Create an instance of this class.
     */
    public CandidateResultsPanel() {
        this.setBackground(Color.PINK);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        ArrayList<ResultSet> data = new ArrayList<>();
        createNewTable();
    }

    /**
     * Adds data to a given table.
     *
     * @param rt The table to which to add data.
     * @param r The data to add.
     */
    public void addData(ResultSet rs) {
//            public void addData(Entry e) {

//         for (Entry<Choice, Integer> e : v.getElectionResults()) {
//            Choice c = e.getKey();
//            Integer count = e.getValue();
//
//            if (c instanceof PoliticalList) {
//                //create new table
//        createNewTable();
//                PoliticalList pl = (PoliticalList) c;
//                System.out.print(pl.getNumber() + " ");
//                System.out.print("Political list: " + pl.getPartyName().get(0).getText());
//            } else if (c instanceof Candidate) {
//                //add to existing table
//                Candidate can = (Candidate) c;
//                System.out.print("\t");
//                System.out.print(can.getNumber() + " ");
//                System.out.print(can.getFirstName() + " ");
//                System.out.print(can.getLastName());
//            }
//
//            System.out.println("...................." + count);
//        }

//        createNewTable();


        activeTable.getTableModel().addResultSet(rs);
        activeTable.revalidate();
        this.revalidate();
        this.repaint();
    }

    public void createNewTable() {
        CandidateResultsTableModel crtm = new CandidateResultsTableModel();
        activeTable = new CandidateResultsTable(crtm);

        JPanel tablePanel = new JPanel();
        tablePanel.setBackground(Color.ORANGE);
        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(activeTable, BorderLayout.CENTER);
        tablePanel.add(activeTable.getTableHeader(), BorderLayout.NORTH);
        activeTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        this.add(tablePanel);
    }

    /**
     * Get the table with a given name.
     *
     * @param eID the name of the table to find.
     * @return The table whose name is eID.
     */
    public SectionResultsTable getTableByName(String tableSectionName) {
        for (SectionResultsTable tbl : tables) {
            String tableSectionNameFound = tbl.getSectionName();
            if (0 == tableSectionNameFound.compareTo(tableSectionName)) {
                return tbl;
            }
        }
        return null;
    }

    /**
     * Check if a table with a given section name exists
     *
     * @param tableSectionName the section name for which to find a table.
     * @param eID The election ID for which to find a table.
     * @return boolean true if the list contains a table that matches that
     * parameters.
     */
    public boolean hasTable(String tableSectionName) {
        boolean found = false;
        if (tables.isEmpty()) {
            LOGGER.log(Level.INFO, "hasTable: No, list empty");
            return found;
        }
        for (SectionResultsTable tbl : tables) {
            String tableSectionNameFound = tbl.getSectionName();
            if (0 == tableSectionNameFound.compareTo(tableSectionName)) {
                found = true;
                LOGGER.log(Level.INFO, "hasTable: yes, match found");
            }
        }
        if (!found) {
            LOGGER.log(Level.INFO, "hasTable: No, not found");
        }
        return found;
    }
}
