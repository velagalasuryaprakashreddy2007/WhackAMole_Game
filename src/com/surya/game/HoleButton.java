package com.surya.game;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 * Represents one hole button on the grid.
 * Draws either the occupant image (mole/bomb) or the empty hole.
 */
public class HoleButton extends JButton {

    private final int id;        // Hole ID
    private final Image bgImage; // Empty hole image
    private Image fgImage;       // Occupant image (mole/bomb)

    public HoleButton(int id, Image bgImage, ActionListener listener) {
        this.id = id;
        this.bgImage = bgImage;
        this.fgImage = null;

        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);

        addActionListener(listener);
    }

    public int getId() {
        return id;
    }

    /**
     * Sets the active occupant image, or null to clear.
     */
    public void setOccupantImage(Image img) {
        this.fgImage = img;
        repaint();
    }

    /**
     * Draws the button:
     * - If occupant exists, draw it.
     * - Otherwise, draw empty hole.
     */
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int w = getWidth();
        int h = getHeight();
        int size = Math.min(w, h);

        int drawSize = (int) (size * 0.90);
        int x = (w - drawSize) / 2;
        int y = (h - drawSize) / 2;

        if (fgImage != null) {
            // Draw mole/bomb only
            int yOffset = (int)(drawSize * 0.10);
            g2.drawImage(fgImage, x, y - yOffset, drawSize, drawSize, this);

        } else {
            // Draw empty hole
            if (bgImage != null) {
                g2.drawImage(bgImage, x, y, drawSize, drawSize, this);
            } else {
                // Fallback shape
                g2.setColor(new Color(60, 40, 20));
                g2.fillOval(x, y, drawSize, drawSize);
            }
        }

        g2.dispose();
    }
}
