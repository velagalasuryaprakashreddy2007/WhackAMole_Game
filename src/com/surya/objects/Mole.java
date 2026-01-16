package com.surya.objects;

import com.surya.game.SoundManager;
import java.awt.Image;

/**
 * The regular mole that gives points when clicked.
 */
public class Mole extends HoleOccupant {

    private static final int SCORE_VALUE = 100;
    private final Image moleImage;

    public Mole(int lifespan) {
        super(lifespan);
        this.moleImage = loadImage("mole.png");
    }

    /**
     * Handles scoring and sound when tapped.
     */
    public int whack() {
        if (isVisible) {
            hide(); 
            SoundManager.playWhack();
            return SCORE_VALUE;
        }
        return 0;
    }

    /**
     * Returns the mole image for display.
     */
    public Image getImage() {
        return moleImage;
    }
}
