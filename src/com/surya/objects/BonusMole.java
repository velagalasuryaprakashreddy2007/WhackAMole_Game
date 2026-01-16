package com.surya.objects;

import com.surya.game.SoundManager;
import java.awt.Image;

/**
 * A rare mole that gives a large score bonus when hit.
 */
public class BonusMole extends HoleOccupant {

    private static final int BONUS_SCORE = 1000;
    private final Image bonusMoleImage;

    public BonusMole(int lifespan) {
        super(lifespan);
        this.bonusMoleImage = loadImage("bonus.png");
    }

    /**
     * Awards bonus points and plays sound when tapped.
     */
    public int whack() {
        if (isVisible) {
            hide();
            SoundManager.playWhack();
            return BONUS_SCORE;
        }
        return 0;
    }

    /**
     * Returns the image used for display.
     */
    public Image getImage() {
        return bonusMoleImage;
    }
}
