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
package ch.bfh.univoteverifier.gui;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JTabbedPane;

/**
 * This class manages the various JTabbedPanes which contain verification
 * results from various elections. It holds the tabbed panes in a list, and
 * implemented methods to append verification results to a given ResultTable.
 *
 * @author prinstin
 */
public class ResultPanelManager extends JTabbedPane{
    
    ArrayList<ResultTablePanel> resultsPanels;
    
    public ResultPanelManager(){
        this.setBackground(Color.WHITE);
        resultsPanels=new ArrayList<>();
    }

    public void addData(ResultSet rs) {
        if (hasTable(rs.getEID())) {
            ResultTablePanel rtp = getTableByName(rs.getEID());
            rtp.addData(rs);
        } else {
            ResultTablePanel newRTP = new ResultTablePanel(rs.getRunnerName(), rs.getEID());
            newRTP.addData(rs);
            this.addTab(rs.getEID(), newRTP);
            this.resultsPanels.add(newRTP);
        }

    }
    
    public void addDataToTable(String eID){

    }
    
    
    public void addTable(ResultSet rs){
     
        
    }
    
    public ResultTablePanel getTableByName(String eID){
        for (ResultTablePanel r : resultsPanels){
                String thisEID = r.getEID();
                if (eID.compareTo(thisEID)==0)
                    return r;
                }
        return null;
    }
    
    public boolean hasTable(String eID){
        boolean found = false;
        if (resultsPanels.isEmpty()) {
            return found;
        }
        for (ResultTablePanel r : resultsPanels) {
            String thisEID = r.getEID();
            if (eID.compareTo(thisEID) == 0) {
                found = true;
            }
        }
        return found;
    }
    
}
