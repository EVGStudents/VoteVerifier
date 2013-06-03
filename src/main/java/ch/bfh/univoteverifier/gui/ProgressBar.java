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

/**
 * Create a new progress bar to visualize the progress of the verification
 * process.
 *
 * @author prinstin
 */
public class ProgressBar extends JPanel {

    private JProgressBar progressBar;
    private int progress = 0;

    /**
     * Create an instance of this class.
     */
    public ProgressBar(int numberOfVrfs) {
        this.setLayout(new BorderLayout());
        progressBar = new JProgressBar(0, numberOfVrfs);
        progressBar.setValue(0);
        //progressBar.setStringPainted(true);
        this.add(progressBar, BorderLayout.CENTER);
    }

    /**
     * Increase the progress of the progress bar.
     *
     * @param chunkProgressed
     */
    public void increaseProgress() {
        progress += 1;
        progressBar.setValue(progress);
    }
}
