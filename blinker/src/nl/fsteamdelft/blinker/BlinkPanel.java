package nl.fsteamdelft.blinker;

import javax.swing.*;
import java.awt.*;

public class BlinkPanel extends JPanel {

    private boolean onoff;
    private String descritpion;

    public BlinkPanel() {
        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    public Dimension getPreferredSize() {
        return new Dimension(250, 250);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(this.onoff ? Color.WHITE : Color.BLACK);
        g.fillRect(0, 0, 250, 250);
        g.setColor(this.onoff ? Color.BLACK : Color.WHITE);
        g.drawString(descritpion, 50, 50);
    }

    public void tortchToggle(boolean onoff, String descritpion) {
        this.onoff = onoff;
        this.descritpion = descritpion;
        this.repaint();
    }
}