package ch.bfh.univoteverifier.gui;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.swing.JTable;
import javax.swing.table.TableColumn;

/**
 * This class extends a JTable and uses a custom TableModel called
 * ResultTableModel.
 *
 * @author prinstin
 */
public class ResultTable extends JTable {

   private String sectionName;
    private ResultTableModel rtm;
    /**
     * Create an instance of this class.
     *
     * @param rtm A ResultTableModel which manages the table's data.
     */
    public ResultTable(ResultTableModel rtm, String sectionName) {
        super(rtm);
        this.rtm=rtm;
        this.sectionName=sectionName;
        setRowHeight(30);
        TableColumn tc = getColumnModel().getColumn(0);
        tc.setPreferredWidth(340);
        tc = getColumnModel().getColumn(1);
        tc.setPreferredWidth(35);
        tc.setMaxWidth(40);
    }
    
    /**
     * Get the name of this table, which corresponds to the name of the verification type of results that it holds.
     * @return The name of the verification type
     */
    public String getSectionName(){
        return sectionName;
     }
    
  
    
        /**
     * Get table model for this table.
     * @return The tableModel for this table.
     */
    public ResultTableModel getTableModel(){
        return rtm;
     }
}
