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

import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.table.AbstractTableModel;

/**
 * This class defines a model for the information that is stored in the tables
 * for the results of verification groups.
 *
 * @author prinstin
 */
public class ResultTableModel extends AbstractTableModel {

    private ArrayList<ResultSet> data;
    private String[] columnNames = {"", ""};

    /**
     * Create an instance of this ResultTableModel.
     *
     * @param data the data with which to initialize this table. At least one
     * ResultSet must be present in the list.
     * @param section The description of the section from which this
     * verification result was produced.
     */
    public ResultTableModel(ArrayList<ResultSet> data) {
        this.data = data;
        String sectionName = data.get(0).getSectionName();
        columnNames[0] = sectionName;

    }

    /**
     * Get the name of the column to be shown in the header of the table. The
     * second column where the image is shown has been left empty.
     *
     * @param col int value of the column for which to find a header title.
     * @return String value which is the name of the column.
     */
    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    /**
     * Get the number of rows that exist in this table.
     *
     * @return int which is the number of rows in this table.
     */
    @Override
    public int getRowCount() {
        return data.size();
    }

    /**
     * Get the number of columns that exist in this table.
     *
     * @return int which is the number of columns in this table.
     */
    @Override
    public int getColumnCount() {
        return 2;
    }

    /**
     * Get the value of a certain cell in the table.
     *
     * @param row The row in which to search for data.
     * @param col The column in which to search for data.
     * @return The value of the cell at the given row and column.
     */
    @Override
    public Object getValueAt(int row, int col) {
        Object ret;
        if (col == 0) {
            ret = data.get(row).getTxt();
        } else if (col == 1) {
            ret = data.get(row).getImage();
        } else {
            ret = null;
        }
        return ret;
    }

    /**
     * Checks whether this cell is editable or not.
     *
     * @param row int value for the row in which to check for editable status.
     * @param col int value for the column in which to check for editable
     * status.
     * @return Boolean value. True if the cell is editable.
     */
    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    /**
     * Set the value of a cell at a given row and column.
     *
     * @param value The value to which to set the cell.
     * @param row The row in which to find the cell.
     * @param col The column in which to find the cell.
     */
    @Override
    public void setValueAt(Object value, int row, int col) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Add a row of results to the table.
     *
     * @param r The ResultPair object containing a name and image.
     */
    public void addResultSet(ResultSet r) {
        data.add(r);
        int row = data.size();
        fireTableDataChanged();
    }

    /**
     * Query for the class type of the objects which are entered into a given
     * column.
     *
     * @param column The column for which to check the Class type.
     * @return Class of the object in a given column.
     */
    @Override
    public Class getColumnClass(int column) {
        switch (column) {
            case 0:
                return String.class;
            case 1:
                return Icon.class;
            default:
                return String.class;
        }
    }
}
