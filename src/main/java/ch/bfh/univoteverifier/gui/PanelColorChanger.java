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

import ch.bfh.univoteverifier.table.TabBackground;
import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.JComponent;

/**
 *
 * @author prinstin
 */
public class PanelColorChanger {

    JComponent fComponent;
    Color fOriginalColor;

    public PanelColorChanger(JComponent aComponent) {

        fComponent = aComponent;
        fOriginalColor = aComponent.getBackground();

    }

    public void start() {

        Thread thread = new Thread(new Worker());
        thread.setDaemon(true);
        thread.start();
    }

    private class Worker implements Runnable {

        ColorCalculator cc = new ColorCalculator(fOriginalColor, Color.RED);

        public void run() {
            for (int i = 0; i < 1000; i++) {
                try {

                    EventQueue.invokeLater(new ChangeColor(cc.nextColor()));
                    Thread.sleep(500);

                } catch (InterruptedException ex) {
                }
            }
        }
    }

    private class ChangeColor implements Runnable {

        Color c;

        public ChangeColor(Color c) {
            this.c = c;
        }

        @Override
        public void run() {
//            fComponent.setOpaque(true);
            ((TabBackground) fComponent).setFontColor(c);
//            fComponent.setBackground(c);
        }
    }

    private class RevertColor implements Runnable {

        @Override
        public void run() {
//            fComponent.setBackground(fOriginalColor);
//            fComponent.setOpaque(fOriginalOpacity);
        }
    }

    private class ColorCalculator {

        int stepR, stepG, stepB;
        int cbR, cbG, cbB, ceR, ceG, ceB;
        int crntR, crntG, crntB;
        int allHere = 0;
        Color[] colors = {fComponent.getForeground(), Color.RED};
        int index = 0;

        public ColorCalculator(Color cBegin, Color cEnd) {

            this.cbR = cBegin.getRed();
            this.cbG = cBegin.getGreen();
            this.cbB = cBegin.getBlue();

            this.ceR = cEnd.getRed();
            this.ceG = cEnd.getGreen();
            this.ceB = cEnd.getBlue();

            crntR = cbR;
            crntG = cbG;
            crntB = cbB;

            stepR = calculateStep(cbR, ceR);
            stepG = calculateStep(cbG, ceG);
            stepB = calculateStep(cbB, ceB);
        }

        public int calculateStep(int cb, int ce) {
            int diff = cb - ce;
//            int step = -1 * (diff / 5);
//            System.out.println("step: " + step);
            int step = 7;
            if (diff > 0) {
                step *= -1;
            }
            return step;
        }

        public Color nextColor() {
            int newR = nextColorComponent(crntR, stepR, cbR, ceR);
            int newG = nextColorComponent(crntG, stepG, cbG, ceG);
            int newB = nextColorComponent(crntB, stepB, cbB, ceB);
            allHere = 0;
            crntR = newR;
            crntG = newG;
            crntB = newB;
            Color newColor = new Color(newR, newG, newB);
            return colors[index++ % 2];
        }

        public int nextColorComponent(int crnt, int step, int begin, int end) {

            int larger, smaller, direction;
            //which limit is the larger one
            if (begin > end) {
                larger = begin;
                smaller = end;
            } else {
                larger = end;
                smaller = begin;
            }
            //to which limit are we going
            if (step < 0) {
                direction = smaller;
            } else {
                direction = larger;
            }


            //make sure we are not out of bounds
            //if the difference between the current position and direction is less than step, change directions
            if (Math.abs(crnt - direction) < Math.abs(step)) {

//                step *= -1;
                allHere++;
                allHere %= 3;
                if (allHere == 0) {
                    swapSteps();
                }
                return crnt;
            }
            allHere--;
            return crnt += step;
        }

        public void swapSteps() {
            stepR *= -1;
            stepG *= -1;
            stepB *= -1;
        }
    }
}
