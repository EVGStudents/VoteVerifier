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

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ProgressBar extends JPanel {

    private JProgressBar progressBar;
    int progress = 0;

    public ProgressBar() {
        this.setLayout(new BorderLayout());
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        this.add(progressBar, BorderLayout.CENTER);
    }

    public void increaseProgress(int chunkProgressed) {
        progress += chunkProgressed;
        progressBar.setValue(progress);
    }
}
