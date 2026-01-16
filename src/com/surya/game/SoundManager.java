package com.surya.game;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.*;

/**
 * Utility class for playing sound effects in the game.
 * Uses Java Clip API for quick audio playback.
 */
public class SoundManager {

    /**
     * Loads and plays a sound file from /audio/ folder.
     */
    private static void playSound(String filename) {
        URL url = SoundManager.class.getResource("/audio/" + filename);

        if (url == null) {
            System.err.println("Audio Warning: Missing sound file: /audio/" + filename);
            return;
        }

        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(stream);
            clip.start();

            // Close clip after playback
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Audio Error: Cannot play " + filename + " : " + e.getMessage());
        }
    }

    /**
     * Plays whack sound.
     */
    public static void playWhack() {
        playSound("whack.wav");
    }

    /**
     * Plays bomb explosion sound.
     */
    public static void playBomb() {
        playSound("bomb_explosion.wav");
    }

    /**
     * Plays game-over sound.
     */
    public static void playGameOver() {
        playSound("game_over.wav");
    }
}
