package nl.fsteamdelft.blinker;

import javax.swing.*;

public class Main {

    public static final int MORSE_TIMEUNIT = 400;

    // low timeouts
    public static final int MORSE_TIMEUNIT_LETTER = 3*MORSE_TIMEUNIT; //600
    public static final int MORSE_TIMEUNIT_SPACE = 7 * MORSE_TIMEUNIT; //1400
    public static final int MORSE_TIMEUNIT_CODES = MORSE_TIMEUNIT; // 200

    // hi timeouts
    public static final int MORSE_TIMEUNIT_DASH = 3*MORSE_TIMEUNIT;//600
    public static final int MORSE_TIMEUNIT_DOT = MORSE_TIMEUNIT;//200

    public static BlinkPanel blinkPanel;
    public static JFrame f;

    public static void main(String[] args) {
        blinkPanel = new BlinkPanel();
        SwingUtilities.invokeLater(() -> {
            f = new JFrame("Blinker");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.add(blinkPanel);
//            f.pack();
            f.setSize(250,250);
            f.setVisible(true);
        });
        new Thread(() -> send("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789")).run();
    }

    private static void send(String text) {
        char[] letters = text.toLowerCase().toCharArray();
        for (int letterIndex = 0; letterIndex < letters.length; letterIndex++) {
            if (letters[letterIndex] == ' ') {
                //The space between words is 7 time units.
                sleep(MORSE_TIMEUNIT_SPACE);

                // After waiting for a space we continue to the next letter immediatly
                continue;
            }

            //The space between letters is 3 time units.
            if (letterIndex != 0 && letters[letterIndex-1] != ' ') {
                // Do not sleep if it's the first character.
                // Do not sleep when the previous letter was a space
                sleep(MORSE_TIMEUNIT_LETTER);
            }

            String code = MorseCode.dict.get(letters[letterIndex]);
            if (code == null) {
                //for now, skip characters unknown to our dictionary
                continue;
            }

            char[] signals = code.toCharArray();
            for (int i = 0; i < signals.length; i++) {
                blinkPanel.tortchToggle(true, letters[letterIndex] + " " + code + " " + i);
                blinkPanel.repaint();

                long start = System.currentTimeMillis();
                if (signals[i] == '.') {
                    //The length of a dot is 1 time unit.
                    sleep(MORSE_TIMEUNIT_DOT);
                } else if (signals[i] == '_') {
                    //A dash is 3 time units.
                    sleep(MORSE_TIMEUNIT_DASH);
                } else {
                    throw new IllegalStateException("Illegal Charaacter");
                }
                blinkPanel.tortchToggle(false, letters[letterIndex] + " " + code + " " + i);
                blinkPanel.repaint();
                System.out.println(signals[i] + " " + (System.currentTimeMillis()-start));
                if (i < signals.length - 1) {
                    //The space between symbols (dots and dashes) of the same letter is 1 time unit.
                    sleep(MORSE_TIMEUNIT_CODES);
                }
            }
        }
    }

    private static void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
