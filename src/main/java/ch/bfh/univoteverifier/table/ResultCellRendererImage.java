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

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * This class takes the data from the table and renders it for display in the
 * table.
 *
 * @author prinstin
 */
class ResultCellRendererImage extends DefaultTableCellRenderer {

    ResultDescriber rd;

    /**
     * Create an instance of this class.
     */
    public ResultCellRendererImage() {
        rd = new ResultDescriber();
    }

    /**
     * Get the component for the given location.
     *
     * @param table The table to get a component for.
     * @param value ?
     * @param isSelected boolean true if cell is selected.
     * @param hasFocus boolean true if cell is selected.
     * @param row int the row value.
     * @param column int the column value.
     * @return The rendered component to be displayed.
     */
    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column) {

        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        Component c = null;
        ResultTableModel rtm = (ResultTableModel) table.getModel();
        String toolTipText = rd.getDescription(rtm.getRowVerificationTypID(row));
        label.setToolTipText(toolTipText);
        if (column == 1) {
            label.setText("");
            Object o = table.getModel().getValueAt(row, column);
            ImageIcon img = (ImageIcon) o;
            label.setIcon(img);
        }
        c = label;
        return c;
    }
}
