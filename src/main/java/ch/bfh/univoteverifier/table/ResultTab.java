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
import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.plaf.basic.BasicButtonUI;

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
    private String eID;
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
    public ResultTab(String eID) {
        toggle = new ToggleResultOrganizationAction(this);
        this.eID = eID;
        rb = ResourceBundle.getBundle("error", GUIconstants.getLocale());
        createContentPanel();
    }

    /**
     * Create the masterTablePanel which contains all other tablePanels.
     *
     * @return a JPanel
     */
    public void createContentPanel() {
        this.setLayout(new GridLayout(1, 1));
        tabHeader = createTabHeader();

        rpSpec = new ResultTablesContainer();
        rpEntity = new ResultTablesContainer();
        rpType = new ResultTablesContainer();
        candidateResultsPanel = new CandidateResultsPanel(toggle);
        scroll = new JScrollPane();
        scroll.getViewport().add(rpSpec);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

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
    public JPanel createTabHeader() {
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
        GridBagConstraints c = new GridBagConstraints();
        panel.setBackground(GUIconstants.GREY);


        JRadioButton btnSpec, btnEntity, btnType;
        ButtonGroup btnGrp = new ButtonGroup();

        btnSpec = new JRadioButton(rb.getString("orgSpec"));
        btnSpec.setBackground(GUIconstants.GREY);
        btnSpec.setName("btnSpec");
        btnSpec.setSelected(true);
        c.insets = new Insets(0, 20, 0, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(btnSpec);


        btnEntity = new JRadioButton(rb.getString("orgEntity"));
        btnEntity.setBackground(GUIconstants.GREY);
        btnEntity.setName("btnEntity");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        panel.add(btnEntity);


        btnType = new JRadioButton(rb.getString("orgType"));
        btnType.setBackground(GUIconstants.GREY);
        btnType.setName("btnType");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 0;
        panel.add(btnType);

        btnGrp.add(btnSpec);
        btnGrp.add(btnEntity);
        btnGrp.add(btnType);


        JButton btnViewResults = new JButton(rb.getString("viewCandidateResults"));
        btnViewResults.setName("btnViewResults");
        btnViewResults.setBorderPainted(true);
        btnViewResults.setBackground(GUIconstants.DARK_GREY);
        btnViewResults.setToolTipText("close this tab");
        btnViewResults.setUI(new BasicButtonUI());
        btnViewResults.setContentAreaFilled(false);
        btnViewResults.setFocusable(false);
        btnViewResults.setBorder(BorderFactory.createEtchedBorder());
        btnViewResults.setRolloverEnabled(true);


        btnSpec.addActionListener(toggle);
        btnEntity.addActionListener(toggle);
        btnType.addActionListener(toggle);
        btnViewResults.addActionListener(toggle);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 0;
        c.weightx = .9;
        c.insets = new Insets(4, 20, 4, 20);
        progressBar = new ProgressBar();
        panel.add(progressBar, c);

        c = new GridBagConstraints();
        c.weightx = .9;
        c.fill = GridBagConstraints.LINE_END;
        c.insets = new Insets(0, 0, 0, 20);
        c.gridx = 4;
        c.gridy = 0;
        panel.add(btnViewResults, c);

        c.insets = new Insets(0, 0, 0, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.0;
        c.gridwidth = 5;
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
        LOGGER.log(Level.OFF, "ELECTION RESULTS RECEIVED BY RESULT TAB");
        candidateResultsPanel.addData(electionResult);
    }

    /**
     * Add data to the result panel.
     *
     * @param rs Data to add.
     */
    public void addData(ResultSet rs) {
        progressBar.increaseProgress(2);

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
        if (candidateResultsShowing) {
            toggleMainPanel();
        }

        showPanel(rpEntity);
    }

    /**
     * Show the panel that is organized according to specification.
     */
    public void showPanelSpec() {
        if (candidateResultsShowing) {
            toggleMainPanel();
        }

        showPanel(rpSpec);
    }

    /**
     * Show the panel that is organized according to type.
     */
    public void showPanelType() {
        if (candidateResultsShowing) {
            toggleMainPanel();
        }
        showPanel(rpType);
    }

    /**
     * Show the panel that is organized according to type.
     */
    public void showCandidateResults() {
        if (!candidateResultsShowing) {
            toggleMainPanel();
        }
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
}
