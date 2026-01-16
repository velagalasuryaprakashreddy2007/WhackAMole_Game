package com.surya.objects;

import com.surya.game.SoundManager;
import java.awt.Image;

/**
 * Represents a bomb object.  
 * When clicked, it deducts points.
 */
public class Bomb extends HoleOccupant {

    private static final int PENALTY_VALUE = -500;
    private final Image bombImage;

    public Bomb(int lifespan) {
        super(lifespan);
        this.bombImage = loadImage("bomb.png"); 
    }

    /**
     * Handles clicking on the bomb.
     * Returns negative score and plays sound.
     */
    public int whack() {
        if (isVisible) {
            hide();
            SoundManager.playBomb();
            return PENALTY_VALUE;
        }
        return 0;
    }

    /**
     * Provides the image shown in the hole.
     */
    public Image getImage() {
        return bombImage;
    }
}
