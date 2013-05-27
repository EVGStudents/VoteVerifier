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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * This class extends a JTable and uses a custom TableModel called
 * ResultTableModel.
 *
 * @author prinstin
 */
public class CandidateResultsTable extends JTable {

    private String sectionName;
    private CandidateResultsTableModel crtm;

    /**
     * Create an instance of this class.
     *
     * @param rtm A ResultTableModel which manages the table's data.
     */
    public CandidateResultsTable(CandidateResultsTableModel crtm) {
        super(crtm);
        setShowGrid(false);
        setDragEnabled(false);
        this.crtm = crtm;
        this.sectionName = "Results";
        setRowHeight(30);
        TableColumn tc = getColumnModel().getColumn(0);
        tc.setPreferredWidth(340);
        tc = getColumnModel().getColumn(1);
        tc.setPreferredWidth(30);
        tc.setMaxWidth(30);

        CandidateResultCellRendererImage rcri = new CandidateResultCellRendererImage();
        tc.setCellRenderer(rcri);
        CandidateResultCellRendererText rcrt = new CandidateResultCellRendererText();
        getColumnModel().getColumn(0).setCellRenderer(rcrt);
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
    public CandidateResultsTableModel getTableModel() {
        return crtm;
    }
}
