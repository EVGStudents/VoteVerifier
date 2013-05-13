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

import javax.swing.JTable;
import javax.swing.table.TableColumn;

/**
 * This class extends a JTable and uses a custom TableModel called
 * ResultTableModel.
 *
 * @author prinstin
 */
public class ResultTable extends JTable {

    private String runnerName;
    private ResultTableModel rtm;

    /**
     * Create an instance of this class.
     *
     * @param rtm A ResultTableModel which manages the table's data.
     */
    public ResultTable(ResultTableModel rtm, String runnerName) {
        super(rtm);
        setShowGrid(false);
        this.rtm = rtm;
        this.runnerName = runnerName;
        setRowHeight(30);
        TableColumn tc = getColumnModel().getColumn(0);
        tc.setPreferredWidth(340);
        tc = getColumnModel().getColumn(1);
        tc.setPreferredWidth(35);
        tc.setMaxWidth(40);
    }

    /**
     * Get the name of this table, which corresponds to the name of the
     * verification type of results that it holds.
     *
     * @return The name of the verification type.
     */
    public String getRunnerName() {
        return runnerName;
    }

    /**
     * Get table model for this table.
     *
     * @return The tableModel for this table.
     */
    public ResultTableModel getTableModel() {
        return rtm;
    }
}
