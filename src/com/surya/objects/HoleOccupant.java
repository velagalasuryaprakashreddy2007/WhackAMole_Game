package com.surya.objects;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * Base class for any object that appears inside a hole.
 */
public abstract class HoleOccupant {

    protected boolean isVisible = true; // Whether it is still active on screen
    protected int lifespan;             // Time before it disappears

    public HoleOccupant(int lifespan) {
        this.lifespan = lifespan;
    }

    /**
     * Action when player clicks the occupant.
     * Each subclass returns its own score value.
     */
    public abstract int whack();

    /**
     * Returns the image displayed in the hole.
     */
    public abstract Image getImage();

    /**
     * Called each second to reduce remaining time.
     */
    public void tick() {
        if (isVisible && lifespan > 0) {
            lifespan--;
            if (lifespan == 0) hide();
        }
    }

    /**
     * Makes the occupant disappear.
     */
    public void hide() { 
        isVisible = false; 
    }

    /**
     * Returns whether the occupant is still active.
     */
    public boolean isVisible() { 
        return isVisible; 
    }

    /**
     * Loads an image from the /images/ directory.
     */
    protected Image loadImage(String name) {
        java.net.URL u = getClass().getResource("/images/" + name);
        if (u == null) throw new RuntimeException("Asset missing: " + name);
        return new ImageIcon(u).getImage();
    }
}
