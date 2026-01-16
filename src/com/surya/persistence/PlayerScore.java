package com.surya.persistence;

import java.io.Serializable;

/**
 * Holds a player's name and score for saving and loading.
 */
public class PlayerScore implements Serializable {

    private static final long serialVersionUID = 1L; 

    private final String playerName;
    private final int score;

    public PlayerScore(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }

    /**
     * Returns the player's name.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Returns the player's score.
     */
    public int getScore() {
        return score;
    }
}
