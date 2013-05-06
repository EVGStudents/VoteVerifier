package ch.bfh.univoteverifier.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


/**
 * This class is a panel which contains all the results from the verifications.
 * It has one main panel and can contain multiple tables. A Vertical scroll bar
 * is added.
 *
 * @author prinstin
 */
public class ResultTablePanel extends JPanel {

    JScrollPane scroll;
    ResultTableModel rtm;
    ResultTable rt;
    JPanel masterTablePanel;
    ArrayList<ResultTable> tables;
    String eID;
    
    /**
     * Create an instance of this panel.
     */
    public ResultTablePanel(String eID) {
        tables = new ArrayList<>();
        createContentPanel();
    }

    /**
     * Create the masterTablePanel which contains all other tablePanels
     *
     * @return a JPanel
     */
    public void createContentPanel() {
        this.setLayout(new BorderLayout());

        masterTablePanel = new JPanel();
        masterTablePanel.setBackground(Color.WHITE);
        masterTablePanel.setLayout(new BoxLayout(masterTablePanel, BoxLayout.Y_AXIS));

        scroll = new JScrollPane();
        scroll.getViewport().add(masterTablePanel);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        this.add(scroll, BorderLayout.CENTER);
       
    }

    /**
     * Add data to a table to see how it looks.  The data is meaningless.
     */
    public void addDummyData() {
        addTable(new ResultSet("first entry", true,"Some section", "Some Election"));
    }

    /**
     * Add data to the active table.
     * @param r data to insert into the table.
     */
    public void addData(ResultSet r) {
        boolean tableExists=false;
        ResultTable rt=null;
        for (ResultTable rti: tables){
            String tableSectionName=rti.getSectionName();
            String dataSectionName = r.getSectionName();
            if (0==tableSectionName.compareTo(dataSectionName))
                tableExists=true;
                rt=rti;
        }
        if (tableExists){
            rt.getTableModel().addResultPair(r);
            rt.revalidate();
        } else{
             addTable(r);
        }
       
        
    }

    /**
     * Create a new panel in which to place the individual tables.  Border layout is used and then header of the tables are placed in the Border.NORTH area and the tables themselves are placed into Border.CENTER.
     * @return JPanel which contains one table.
     */
    public JPanel createTablePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout());
        return panel;
    }

    /**
     * Add a table to a TablePanel from createTablePanel.
     * @param data Data with which to initialize this table.  It must contain at least one entry of a ResultPair.
     */
    public void addTable(ResultSet r) {
        JPanel tablePanel = createTablePanel();
        ArrayList<ResultSet> data = new ArrayList<>();
        data.add(r);
        
        rtm = new ResultTableModel(data);
        rt = new ResultTable(rtm, data.get(0).getSectionName());
        rt.setShowGrid(false);

        tablePanel.add(rt, BorderLayout.CENTER);
        tablePanel.add(rt.getTableHeader(), BorderLayout.NORTH);
        rt.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        masterTablePanel.add(tablePanel);
        masterTablePanel.revalidate();
        masterTablePanel.repaint();
    }
    
        /**
     * Get the eID of this table, which corresponds to the election ID for which this table holds results.
     * @return The name of the election ID.
     */
    public String getEID(){
        return eID;
     }
}
