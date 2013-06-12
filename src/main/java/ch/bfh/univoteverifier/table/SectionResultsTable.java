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
import javax.swing.ToolTipManager;
import javax.swing.table.TableColumn;

/**
 * This class extends a JTable and uses a custom TableModel called
 * ResultTableModel.
 *
 * @author prinstin
 */
public class SectionResultsTable extends JTable {

    private String sectionName;
    private ResultTableModel rtm;

    /**
     * Create an instance of this class.
     *
     * @param rtm A ResultTableModel which manages the table's data.
     */
    public SectionResultsTable(ResultTableModel rtm, String sectionName) {
        super(rtm);
        setShowGrid(false);
        setDragEnabled(true);
        this.rtm = rtm;
        this.sectionName = sectionName;
        setRowHeight(30);
        TableColumn tc = getColumnModel().getColumn(0);
        tc.setPreferredWidth(340);
        tc = getColumnModel().getColumn(1);
        tc.setPreferredWidth(30);
        tc.setMaxWidth(50);

        ResultCellRendererImage rcri = new ResultCellRendererImage();
        tc.setCellRenderer(rcri);
        ResultCellRendererText rcrt = new ResultCellRendererText();
        getColumnModel().getColumn(0).setCellRenderer(rcrt);

        ToolTipManager.sharedInstance().setDismissDelay(30000);
        ToolTipManager.sharedInstance().registerComponent(this);
    }

    /**
     * Get the name of this table, which corresponds to the name of the
     * verification type of results that it holds.
     *
     * @return The name of the verification type.
     */
    public String getSectionName() {
        return sectionName;
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
