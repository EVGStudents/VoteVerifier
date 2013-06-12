/*
 *
 *  Copyright (c) 2013 Berner Fachhochschule, Switzerland.
 *   Bern University of Applied Sciences, Engineering and Information Technology,
 *   Research Institute for Security in the Information Society, E-Voting Group,
 *   Biel, Switzerland.
 *
 *   Project independent VoteVerifier.
 *
 */
package ch.bfh.univoteverifier.table;

import ch.bfh.univote.common.Candidate;
import ch.bfh.univote.common.Choice;
import ch.bfh.univote.common.PoliticalList;
import ch.bfh.univoteverifier.gui.GUIconstants;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;

/**
 * This panel contains the election results for the number of votes each
 * candidate received.
 *
 * @author prinstin
 */
public class CandidateResultsPanel extends JPanel {

    private ArrayList<SectionResultsTable> tables;
    private static final Logger LOGGER = Logger.getLogger(ResultTablesContainer.class.getName());
    private CandidateResultsTable activeTable;
    private ResourceBundle rb;
    private JPanel resultsContent;
    private JLabel noResultsLabel;
    private boolean resultsArrived = false, individualVrf;

    /**
     * Create an instance of this class.
     */
    public CandidateResultsPanel(ActionListener toggle, boolean individualVrf) {
        this.individualVrf = individualVrf;
        rb = ResourceBundle.getBundle("error", GUIconstants.getLocale());

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

//        JScrollPane scrollPanel = createScrollPanel();
        resultsContent = new JPanel();
        resultsContent.setLayout(new BoxLayout(resultsContent, BoxLayout.Y_AXIS));
        String processingResults;
        if (individualVrf) {
            processingResults = rb.getString("notShownForIndVrf");
        } else {
            processingResults = rb.getString("processingResults");
        }

        noResultsLabel = new JLabel(processingResults);

        resultsContent.add(noResultsLabel);

        this.add(resultsContent);
        ArrayList<ResultSet> data = new ArrayList<>();
    }

    public JScrollPane createScrollPanel() {
        resultsContent = new JPanel();
        resultsContent.setLayout(new BoxLayout(resultsContent, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.getViewport().add(resultsContent);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createEtchedBorder());
        return scrollPane;
    }

    /**
     * Create the header for the panel with in the tab. It contains a header
     * above the verification results which allows the user to change the
     * organization styles, see the progress of the verification and view the
     * election results for votes.
     *
     * @return
     */
    public JPanel createHeaderPanel(ActionListener toggle) {

        JPanel panel = new JPanel(new GridBagLayout());

        panel.setBackground(GUIconstants.GREY);
        panel.setBorder(new EtchedBorder());
        String electionResultsTitle = rb.getString("electionResults");
        JLabel eResultsLabel = new JLabel(electionResultsTitle);
        eResultsLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(20, 20, 20, 20);
        c.fill = GridBagConstraints.CENTER;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(eResultsLabel, c);



        JButton btnBack = new JButton(rb.getString("back"));
        btnBack.setName("btnBack");
        btnBack.addActionListener(toggle);
        c = new GridBagConstraints();
        c.insets = new Insets(0, 20, 20, 0);
        c.fill = GridBagConstraints.CENTER;
        c.weightx = .01;
        c.gridx = 0;
        c.gridy = 1;
        panel.add(btnBack, c);

        String backInstructionsText = rb.getString("clickBackToReturn");
        JLabel backInstructionsLabel = new JLabel(backInstructionsText);
        c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 20, 0);
        c.fill = GridBagConstraints.LINE_START;
        c.weightx = 0.95;
        c.gridx = 1;
        c.gridy = 1;
        panel.add(backInstructionsLabel, c);



        String processingResults = rb.getString("processingResults");
        noResultsLabel = new JLabel(processingResults);
        c = new GridBagConstraints();
        c.insets = new Insets(40, 40, 40, 40);
        c.fill = GridBagConstraints.CENTER;
        c.weightx = 0.9;
        c.weighty = 0.9;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 2;
        panel.add(noResultsLabel, c);

        return panel;
    }

    /**
     * Adds data to a given table.
     *
     * @param rt The table to which to add data.
     * @param r The data to add.
     */
    public void addData(Map<Choice, Integer> electionResult) {
        LOGGER.log(Level.INFO, "ELECTION RESULTS RECEIVED BY CANDIDATE RESULT PANEL");
        for (Entry<Choice, Integer> e : electionResult.entrySet()) {
            Choice c = e.getKey();
            Integer count = e.getValue();
            if (c instanceof PoliticalList) {
                String cellValue = e.getValue().toString();
                String cellName = ((PoliticalList) e.getKey()).getTitle().get(0).getText();
                createNewTable(cellName, cellValue);
                PoliticalList pl = (PoliticalList) c;
            } else if (c instanceof Candidate) {
                //RSIS election sends a list with out a political list first, create generic header in this case
                if (activeTable == null) {
                    String cellValue = e.getValue().toString();
                    String cellName = "No political party";
                    createNewTable(cellName, cellValue);
                } else {
                    //add to existing table
                    activeTable.getTableModel().addEntry(e);
                    activeTable.revalidate();
                    Candidate can = (Candidate) c;
                }
            }
        }
        this.revalidate();
        this.repaint();
    }

    public void createNewTable(String cellName, String cellValue) {
        if (!resultsArrived) {
            resultsContent.remove(noResultsLabel);
            resultsArrived = !resultsArrived;
        }

        CandidateResultsTableModel crtm = new CandidateResultsTableModel(cellName, cellValue);
        activeTable = new CandidateResultsTable(crtm);

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(activeTable, BorderLayout.CENTER);
        tablePanel.add(activeTable.getTableHeader(), BorderLayout.NORTH);
        activeTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        resultsContent.add(tablePanel);
        resultsContent.revalidate();
    }

    /**
     * Get the table with a given name.
     *
     * @param eID the name of the table to find.
     * @return The table whose name is eID.
     */
    public SectionResultsTable getTableByName(String tableSectionName) {
        for (SectionResultsTable tbl : tables) {
            String tableSectionNameFound = tbl.getSectionName();
            if (tableSectionNameFound.equals(tableSectionName)) {
                return tbl;
            }
        }
        return null;
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
        for (SectionResultsTable tbl : tables) {
            String tableSectionNameFound = tbl.getSectionName();
            if (tableSectionNameFound.equals(tableSectionName)) {
                found = true;
                LOGGER.log(Level.INFO, "hasTable: yes, match found");
            }
        }
        if (!found) {
            LOGGER.log(Level.INFO, "hasTable: No, not found");
        }
        return found;
    }
}
