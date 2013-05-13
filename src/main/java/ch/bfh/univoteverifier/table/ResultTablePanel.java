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

import ch.bfh.univoteverifier.action.ActionManager;
import ch.bfh.univoteverifier.action.ToggleResultOrganizationAction;
import ch.bfh.univoteverifier.gui.GUIconstants;
import java.util.logging.Logger;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

/**
 * This class is a panel which contains all the results from the verifications.
 * It has one main panel and can contain multiple tables. A Vertical scroll bar
 * is added.
 *
 * @author prinstin
 */
public class ResultTablePanel extends JPanel {

    private JScrollPane scroll;
    private JPanel tabHeader;
    private String eID;
    private static final Logger LOGGER = Logger.getLogger(ResultTablePanel.class.getName());
    private ResultPanel rp;
    private ResourceBundle rb;

    /**
     * Create an instance of this panel.
     */
    public ResultTablePanel(String eID) {
        this.eID = eID;
        rb = ResourceBundle.getBundle("error", GUIconstants.getLocale());
        createContentPanel();
    }

    /**
     * Create the masterTablePanel which contains all other tablePanels
     *
     * @return a JPanel
     */
    public void createContentPanel() {
        this.setLayout(new BorderLayout());
        tabHeader = createTabHeader();

        rp = new ResultPanel();
        scroll = new JScrollPane();
        scroll.getViewport().add(rp);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        this.add(tabHeader, BorderLayout.NORTH);
        this.add(scroll, BorderLayout.CENTER);

    }

    public JPanel createTabHeader() {
        ActionManager am = ActionManager.getInstance();
        ActionListener alOrgSpec = (ActionListener) am.getAction("orgSpec");
        ActionListener alOrgEntity = (ActionListener) am.getAction("orgEntity");

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        JRadioButton btnSpec, btnEntity, btnRSA;
        ButtonGroup btnGrp = new ButtonGroup();

        btnSpec = new JRadioButton(rb.getString("orgSpec"));
        btnSpec.setBackground(GUIconstants.GREY);
        btnSpec.setName("btnSpec");
        btnSpec.addActionListener(alOrgSpec);

        btnEntity = new JRadioButton(rb.getString("orgEntity"));
        btnEntity.setBackground(GUIconstants.GREY);
        btnEntity.setName("btnEntity");
        btnEntity.addActionListener(alOrgEntity);

        btnGrp.add(btnSpec);
        btnGrp.add(btnEntity);
        btnSpec.setSelected(true);

        ActionListener toggle = new ToggleResultOrganizationAction(this);
        btnSpec.addActionListener(toggle);
        btnEntity.addActionListener(toggle);

        panel.add(btnSpec);
        panel.add(btnEntity);
        return panel;
    }

    /**
     * Toggle which panel is showing. Show the results organized by
     * specification or by entity.
     *
     * @param str Identifier to declare which result panel to show.
     */
    public void showPanel(String str) {
        //TODO
    }

    /**
     * Add data to the result panel.
     *
     * @param rs Data to add.
     */
    public void addData(ResultSet rs) {
        //TODO  Must distinguish between organization types.
        rp.addData(rs);
    }

    /**
     * Get the eID of this table, which corresponds to the election ID for which
     * this table holds results.
     *
     * @return The name of the election ID.
     */
    public String getEID() {
        return eID;
    }

    public class ResultPanel extends JPanel {

        private ResultTableModel masterTableModel;
        private ArrayList<ResultTable> tables;

        public ResultPanel() {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            tables = new ArrayList<>();
        }

        /**
         * Add data to the appropriate table
         *
         * @param r data to insert into a table.
         */
        public void addData(ResultSet r) {
            String runnerName = r.getRunnerName().toString();
            if (hasTable(runnerName)) {
                ResultTable rt = getTableByName(runnerName);
                rt.getTableModel().addResultSet(r);
                rt.revalidate();
                this.revalidate();
                this.repaint();
            } else {
                addTable(r);
            }
        }

        /**
         * Get the table with a given name.
         *
         * @param eID the name of the table to find.
         * @return The table whose name is eID.
         */
        public ResultTable getTableByName(String runnerName) {
            for (ResultTable tbl : tables) {
                String runnerNameFound = tbl.getRunnerName();
                if (0 == runnerNameFound.compareTo(runnerName)) {
                    return tbl;
                }
            }
            return null;
        }

        /**
         * Add a table to this result panel
         *
         * @param data Data with which to initialize this table. It must contain
         * at least one entry of a ResultPair.
         */
        public void addTable(ResultSet r) {
            JPanel tablePanel = createTablePanel();
            ArrayList<ResultSet> data = new ArrayList<>();
            data.add(r);

            masterTableModel = new ResultTableModel(data);
            ResultTable rt = new ResultTable(masterTableModel, data.get(0).getRunnerName().toString());
            tables.add(rt);

            tablePanel.add(rt, BorderLayout.CENTER);
            tablePanel.add(rt.getTableHeader(), BorderLayout.NORTH);
            rt.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

            this.add(tablePanel);
            this.revalidate();
            this.repaint();
        }

        /**
         * Check if a table with a given runnername exists
         *
         * @param runnerName the runnername for which to find a table.
         * @param eID The election ID for which to find a table.
         * @return boolean true if the list contains a table that matches that
         * parameters.
         */
        public boolean hasTable(String runnerName) {
            boolean found = false;
            if (tables.isEmpty()) {
                LOGGER.log(Level.INFO, "hasTable: No, list empty");
                return found;
            }
            for (ResultTable tbl : tables) {
                String runnerNameFound = tbl.getRunnerName();
                if (0 == runnerNameFound.compareTo(runnerName)) {
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
         * Create a new panel in which to place the individual tables. Border
         * layout is used and then header of the tables are placed in the
         * Border.NORTH area and the tables themselves are placed into
         * Border.CENTER.
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
}
