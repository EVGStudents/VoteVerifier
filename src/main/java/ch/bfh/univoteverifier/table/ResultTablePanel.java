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
import java.util.logging.Logger;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
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
    private ResultPanel rpEntity, rpSpec, rpType;
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
     * Create the masterTablePanel which contains all other tablePanels.
     *
     * @return a JPanel
     */
    public void createContentPanel() {
        this.setLayout(new BorderLayout());
        tabHeader = createTabHeader();

        rpSpec = new ResultPanel();
        rpEntity = new ResultPanel();
        rpType = new ResultPanel();
        scroll = new JScrollPane();
        scroll.getViewport().add(rpSpec);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        this.add(tabHeader, BorderLayout.NORTH);
        this.add(scroll, BorderLayout.CENTER);

    }

    public JPanel createTabHeader() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        JRadioButton btnSpec, btnEntity, btnType;
        ButtonGroup btnGrp = new ButtonGroup();

        btnSpec = new JRadioButton(rb.getString("orgSpec"));
        btnSpec.setBackground(GUIconstants.GREY);
        btnSpec.setName("btnSpec");

        btnEntity = new JRadioButton(rb.getString("orgEntity"));
        btnEntity.setBackground(GUIconstants.GREY);
        btnEntity.setName("btnEntity");

        btnType = new JRadioButton(rb.getString("orgType"));
        btnType.setBackground(GUIconstants.GREY);
        btnType.setName("btnType");



        btnGrp.add(btnSpec);
        btnGrp.add(btnEntity);
        btnGrp.add(btnType);

        btnSpec.setSelected(true);

        ActionListener toggle = new ToggleResultOrganizationAction(this);
        btnSpec.addActionListener(toggle);
        btnEntity.addActionListener(toggle);
        btnType.addActionListener(toggle);

        panel.add(btnSpec);
        panel.add(btnEntity);
        panel.add(btnType);

        JPanel fillerPanel = new JPanel();
        fillerPanel.setBackground(GUIconstants.GREY);
        fillerPanel.add(panel);
        return fillerPanel;
    }

    /**
     * Toggle which panel is showing. Show the results organized by
     * specification or by entity.
     *
     * @param str Identifier to declare which result panel to show.
     */
    public void showPanel(ResultPanel rp) {
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
        rs.setSectionName(rs.getRunnerName().toString());
        rpSpec.addData(rs);

        rs.setSectionName(rs.getEntityType());
        rpEntity.addData(rs);

        rs.setSectionName(rs.getImplementerType());
        rpType.addData(rs);

    }

    public void showPanelEntity() {
        showPanel(rpEntity);
    }

    public void showPanelSpec() {
        showPanel(rpSpec);
    }

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
