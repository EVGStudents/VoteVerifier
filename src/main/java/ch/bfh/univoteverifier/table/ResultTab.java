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

import ch.bfh.univoteverifier.action.ToggleResultOrganizationAction;
import ch.bfh.univoteverifier.gui.GUIconstants;
import ch.bfh.univoteverifier.gui.ProgressBar;
import java.util.logging.Logger;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
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
    private JPanel tabHeader;
    private String eID;
    private static final Logger LOGGER = Logger.getLogger(ResultTab.class.getName());
    private ResultTablesContainer rpEntity, rpSpec, rpType;
    private ResourceBundle rb;
    private ProgressBar progressBar;

    /**
     * Create an instance of this panel.
     */
    public ResultTab(String eID) {
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
        this.setLayout(new BorderLayout());
        tabHeader = createTabHeader();

        rpSpec = new ResultTablesContainer();
        rpEntity = new ResultTablesContainer();
        rpType = new ResultTablesContainer();
        scroll = new JScrollPane();
        scroll.getViewport().add(rpSpec);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        this.add(tabHeader, BorderLayout.NORTH);
        this.add(scroll, BorderLayout.CENTER);

    }

    /**
     * Create the header for the panel with in the tab. It contains a header
     * above the verification results which allows the user to change the
     * organisation styles, see the progress of the verification and view the
     * election results for votes.
     *
     * @return
     */
    public JPanel createTabHeader() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        panel.setBackground(GUIconstants.GREY);


        JRadioButton btnSpec, btnEntity, btnType;
        ButtonGroup btnGrp = new ButtonGroup();

        btnSpec = new JRadioButton(rb.getString("orgSpec"));
        btnSpec.setBackground(GUIconstants.GREY);
        btnSpec.setName("btnSpec");
        btnSpec.setSelected(true);
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

        ActionListener toggle = new ToggleResultOrganizationAction(this);
        btnSpec.addActionListener(toggle);
        btnEntity.addActionListener(toggle);
        btnType.addActionListener(toggle);

        JButton btnClose = new JButton(rb.getString("viewCandidateResults"));
        btnClose.setBorderPainted(true);
        btnClose.setBackground(GUIconstants.DARK_GREY);
        btnClose.setName("CandidateResults");
        //btnClose.setPreferredSize(new Dimension(17, 17));
        btnClose.setToolTipText("close this tab");
        btnClose.setUI(new BasicButtonUI());
        btnClose.setContentAreaFilled(false);
        btnClose.setFocusable(false);
        btnClose.setBorder(BorderFactory.createEtchedBorder());
        btnClose.setRolloverEnabled(true);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 0;
        c.weightx = .5;
//        panel.add(Box.createHorizontalGlue(), c);
        progressBar = new ProgressBar();
        panel.add(progressBar, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 0;
        panel.add(btnClose);

        return panel;
    }

    /**
     * Toggle which panel is showing. Show the results organized by
     * specification or by entity.
     *
     * @param str Identifier to declare which result panel to show.
     */
    public void showPanel(ResultTablesContainer rp) {
        scroll.getViewport().removeAll();
        scroll.getViewport().add(rp);
        scroll.revalidate();
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
     * Get the eID of this table, which corresponds to the election ID for which
     * this table holds results.
     *
     * @return The name of the election ID.
     */
    public String getEID() {
        return eID;
    }
}
