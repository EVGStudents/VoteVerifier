/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author prinstin
 */
public class ResultPanel extends JPanel {

    private ResultTableModel masterTableModel;
    private ArrayList<ResultTable> tables;
    private static final Logger LOGGER = Logger.getLogger(ResultPanel.class.getName());

    public ResultPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        tables = new ArrayList<>();
    }

    /**
     * Find the appropriate table and add data to it.
     *
     * @param r data to insert into a table.
     */
    public void addData(ResultSet r) {
        String tableSectionName = r.getSectionName();
        if (hasTable(tableSectionName)) {
            ResultTable rt = getTableByName(tableSectionName);
            addDataToTable(rt, r);
        } else {
            addTable(r);
        }
    }

    /**
     * Adds data to a given table.
     *
     * @param rt The table to which to add data.
     * @param r The data to add.
     */
    public void addDataToTable(ResultTable rt, ResultSet r) {
        rt.getTableModel().addResultSet(r);
        rt.revalidate();
        this.revalidate();
        this.repaint();
    }

    /**
     * Get the table with a given name.
     *
     * @param eID the name of the table to find.
     * @return The table whose name is eID.
     */
    public ResultTable getTableByName(String tableSectionName) {
        for (ResultTable tbl : tables) {
            String tableSectionNameFound = tbl.getSectionName();
            if (0 == tableSectionNameFound.compareTo(tableSectionName)) {
                return tbl;
            }
        }
        return null;
    }

    /**
     * Add a table to this result panel
     *
     * @param data Data with which to initialize this table. It must contain at
     * least one entry of a ResultPair.
     */
    public void addTable(ResultSet r) {
        JPanel tablePanel = createTablePanel();
        ArrayList<ResultSet> data = new ArrayList<>();
        data.add(r);

        masterTableModel = new ResultTableModel(data);
        ResultTable rt = new ResultTable(masterTableModel, data.get(0).getSectionName());
        tables.add(rt);

        tablePanel.add(rt, BorderLayout.CENTER);
        tablePanel.add(rt.getTableHeader(), BorderLayout.NORTH);
        rt.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        this.add(tablePanel);
        this.revalidate();
        this.repaint();
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
        for (ResultTable tbl : tables) {
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

    /**
     * Create a new panel in which to place the individual tables. Border layout
     * is used and then header of the tables are placed in the Border.NORTH area
     * and the tables themselves are placed into Border.CENTER.
     *
     * @return JPanel which contains one table.
     */
    public JPanel createTablePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout());
        return panel;
    }
}
