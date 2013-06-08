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

import ch.bfh.univote.common.Choice;
import ch.bfh.univoteverifier.action.ToggleResultOrganizationAction;
import ch.bfh.univoteverifier.gui.GUIconstants;
import ch.bfh.univoteverifier.gui.ProgressBar;
import java.util.logging.Logger;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;

/**
 * This class is a panel which contains all the results from the verifications.
 * It has one main panel and can contain multiple tables. A Vertical scroll bar
 * is added.
 *
 * @author prinstin
 */
public class ResultTab extends JPanel {

    private JScrollPane scroll;
    private JPanel vrfResultsPanel, tabHeader;
    private JLabel processCompleted;
    private String processID;
    private static final Logger LOGGER = Logger.getLogger(ResultTab.class.getName());
    private ResultTablesContainer rpEntity, rpSpec, rpType;
    private CandidateResultsPanel candidateResultsPanel;
    private ResourceBundle rb;
    private ProgressBar progressBar;
    private JTextArea errorText;
    private Boolean tabSpacer = true;
    private Boolean candidateResultsShowing = false;
    private ActionListener toggle;

    /**
     * Create an instance of this panel.
     */
    public ResultTab(String procesID, boolean individualVrf, int numberOfVrfs) {
        toggle = new ToggleResultOrganizationAction(this);
        this.processID = procesID;
        rb = ResourceBundle.getBundle("error", GUIconstants.getLocale());
        createContentPanel(individualVrf, numberOfVrfs);

        processCompleted = new JLabel(rb.getString("processComplete"));

    }

    /**
     * Removes the Progress Bar and displays the text that the verification
     * process has finished.
     */
    public void completeVerification() {

        //Change to true, if at the end of a verification the progress bar should be replaced by a message completion label
        if (false) {
            tabHeader.remove(progressBar);

            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 5;
            c.gridy = 0;
            c.weightx = .9;
            c.insets = new Insets(4, 20, 4, 20);

            tabHeader.add(processCompleted, c);
            tabHeader.revalidate();
        }
    }

    /**
     * Create the masterTablePanel which contains all other tablePanels.
     *
     * @return a JPanel
     */
    public void createContentPanel(boolean individualVrf, int numberOfVrfs) {
        this.setLayout(new GridLayout(1, 1));
        tabHeader = createTabHeader(numberOfVrfs);

        rpSpec = new ResultTablesContainer();
        rpEntity = new ResultTablesContainer();
        rpType = new ResultTablesContainer();
        candidateResultsPanel = new CandidateResultsPanel(toggle, individualVrf);
        scroll = new JScrollPane();
        scroll.getViewport().add(rpSpec);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        vrfResultsPanel = new JPanel();
        vrfResultsPanel.setLayout(new BorderLayout());
        vrfResultsPanel.add(tabHeader, BorderLayout.NORTH);
        vrfResultsPanel.add(scroll, BorderLayout.CENTER);
        this.add(vrfResultsPanel);

    }

    /**
     * Create the header for the panel with in the tab. It contains a header
     * above the verification results which allows the user to change the
     * organization styles, see the progress of the verification and view the
     * election results for votes.
     *
     * @return
     */
    public JPanel createTabHeader(int numberOfVrfs) {
        errorText = new JTextArea();
        errorText.setText("Errors and Exceptions: ");
        errorText.setWrapStyleWord(true);
        errorText.setLineWrap(true);
        errorText.setEditable(false);
        errorText.setFont(new Font("Serif", Font.PLAIN, 10));

        JScrollPane scrollPane = new JScrollPane(errorText);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createEtchedBorder());

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c;
        panel.setBackground(GUIconstants.GREY);


        JRadioButton btnSpec, btnEntity, btnType, btnViewResults;
        ButtonGroup btnGrp = new ButtonGroup();

        btnSpec = new JRadioButton(rb.getString("orgSpec"));
        btnSpec.setBackground(GUIconstants.GREY);
        btnSpec.setName("btnSpec");
        btnSpec.setSelected(true);
        c = new GridBagConstraints();
        c.insets = new Insets(0, 10, 0, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(btnSpec, c);


        btnEntity = new JRadioButton(rb.getString("orgEntity"));
        btnEntity.setBackground(GUIconstants.GREY);
        btnEntity.setName("btnEntity");
        c = new GridBagConstraints();
        c.insets = new Insets(0, 2, 0, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        panel.add(btnEntity, c);


        btnType = new JRadioButton(rb.getString("orgType"));
        btnType.setBackground(GUIconstants.GREY);
        btnType.setName("btnType");
        c = new GridBagConstraints();
        c.insets = new Insets(0, 2, 0, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 0;
        panel.add(btnType, c);

        c = new GridBagConstraints();
        c.insets = new Insets(0, 4, 0, 0);
        c.fill = GridBagConstraints.VERTICAL;
        c.gridx = 3;
        c.gridy = 0;
        c.ipadx = 2;
        panel.add(new JSeparator(JSeparator.VERTICAL), c);




        btnViewResults = new JRadioButton(rb.getString("viewCandidateResults"));
        btnViewResults.setBackground(GUIconstants.GREY);
        btnViewResults.setName("btnViewResults");
        c = new GridBagConstraints();
        c.insets = new Insets(0, 5, 0, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 4;
        c.gridy = 0;
        panel.add(btnViewResults, c);


        btnGrp.add(btnSpec);
        btnGrp.add(btnEntity);
        btnGrp.add(btnType);
        btnGrp.add(btnViewResults);

        btnSpec.addActionListener(toggle);
        btnEntity.addActionListener(toggle);
        btnType.addActionListener(toggle);
        btnViewResults.addActionListener(toggle);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 5;
        c.gridy = 0;
        c.weightx = .9;
        c.insets = new Insets(4, 20, 4, 20);
        progressBar = new ProgressBar(numberOfVrfs);
        panel.add(progressBar, c);


        c.insets = new Insets(0, 0, 0, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.0;
        c.gridwidth = 6;
        c.ipady = 30;
        c.gridx = 0;
        c.gridy = 1;
        panel.add(scrollPane, c);

        return panel;
    }

    /**
     * Toggle which panel is showing. Show the results organized by
     * specification or by entity.
     *
     * @param str Identifier to declare which result panel to show.
     */
    public void showPanel(JPanel panel) {
        scroll.getViewport().removeAll();
        scroll.getViewport().add(panel);
        scroll.revalidate();
    }

    /**
     * Add text to the error message text area in a result pane.
     *
     * @param str The message to add.
     */
    public void showElectionSpecError(String str) {
        String offset;
        if (tabSpacer) {
            offset = "\n";
            tabSpacer = false;
        } else {
            offset = "\t\t\t";
            tabSpacer = true;
        }
        errorText.append(offset + str);
    }

    /**
     * Add election results to the election result panel.
     */
    public void addElectionResults(Map<Choice, Integer> electionResult) {
        LOGGER.log(Level.INFO, "ELECTION RESULTS RECEIVED BY RESULT TAB");
        candidateResultsPanel.addData(electionResult);
    }

    /**
     * Add data to the result panel.
     *
     * @param rs Data to add.
     */
    public void addData(ResultSet rs) {
        progressBar.increaseProgress();

        rs.setSectionName(rs.getRunnerName().toString());
        rpSpec.addData(rs);

        rs.setSectionName(rs.getEntityType());
        rpEntity.addData(rs);

        rs.setSectionName(rs.getImplementerType());
        rpType.addData(rs);

    }

    /**
     * Change visible pane between candidate results and verification results.
     */
    public void toggleMainPanel() {
        JPanel panelToRemove, panelToAdd;
        if (candidateResultsShowing) {
            panelToRemove = candidateResultsPanel;
            panelToAdd = vrfResultsPanel;
        } else {
            panelToRemove = vrfResultsPanel;
            panelToAdd = candidateResultsPanel;
        }
        candidateResultsShowing = !candidateResultsShowing;
        this.remove(panelToRemove);
        this.add(panelToAdd);
        this.revalidate();
        this.repaint();
    }

    /**
     * Show the panel that is organized according to entity.
     */
    public void showPanelEntity() {
        showPanel(rpEntity);
    }

    /**
     * Show the panel that is organized according to specification.
     */
    public void showPanelSpec() {
        showPanel(rpSpec);
    }

    /**
     * Show the panel that is organized according to type.
     */
    public void showPanelType() {
        showPanel(rpType);
    }

    /**
     * Show the panel that is organized according to type.
     */
    public void showCandidateResults() {
        showPanel(candidateResultsPanel);
    }

    /**
     * Get the ID of this tab, which corresponds to the election ID for which
     * this tab holds results.
     *
     * @return The tab ID.
     */
    public String getProcessID() {
        return processID;
    }
}
