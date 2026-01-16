package com.surya.game;

import com.surya.objects.*;
import com.surya.persistence.*;
import java.util.*;
import javax.swing.*;

/**
 * Main engine controlling game loop, spawning, scoring, and timing.
 */
public class GameEngine implements Runnable {

    private final WhackAMoleApp app;      // Reference to UI
    private final GameGrid grid;          // Game board
    private final HighScoreManager storage; // Handles high score saving/loading
    private final Random rng = new Random();

    private volatile boolean running = false; // Game loop flag
    private int score;                        // Player score
    private int time;                         // Time left
    private List<PlayerScore> leaders;        // High score list

    public GameEngine(WhackAMoleApp app) {
        this.app = app;
        this.grid = new GameGrid();
        this.storage = new HighScoreManager();
        reloadScores(); // Load stored leaderboard
    }

    public GameGrid getGrid() { return grid; }

    /**
     * Starts a fresh game state.
     */
    public void resetGame() {
        score = 0;
        time =15;
        running = true;
        grid.resetGrid();
    }
    
    /**
     * Stops the game loop.
     */
    public void stop() {
        running = false;
    }

    /**
     * Main loop: ticks every second while running.
     */
    public void run() {
        while (running && time >= 0) {
            time--;
            refreshDisplay();
            
            updateOccupants(); // Update disappearing objects
            spawnRandom();     // Try to spawn new hole items

            try {
                Thread.sleep(1000); // 1-second tick
            } catch (InterruptedException e) {
                running = false;
                Thread.currentThread().interrupt();
            }
        }
        finishGame();
    }

    /**
     * Updates each hole occupant (reduces visible time).
     */
    private void updateOccupants() {
        for (int id : grid.getOccupiedHoleIds()) {
            HoleOccupant occ = grid.getOccupant(id);
            if (occ != null) {
                occ.tick();
                if (!occ.isVisible()) {
                    SwingUtilities.invokeLater(() -> app.getButton(id).setOccupantImage(null));
                    grid.clearOccupant(id);
                }
            }
        }
    }

    /**
     * Randomly spawns Mole, Bomb, or BonusMole in an empty hole.
     */
    private void spawnRandom() {
        int id = rng.nextInt(grid.getSize());
        if (grid.getOccupant(id) == null) {
            HoleOccupant obj;
            int roll = rng.nextInt(100);
            
            if (roll < 70) obj = new Mole(1);
            else if (roll < 90) obj = new Bomb(3);
            else obj = new BonusMole(3);

            grid.setOccupant(id, obj);
            SwingUtilities.invokeLater(() -> app.getButton(id).setOccupantImage(obj.getImage()));
        }
    }

    /**
     * Adds points and updates HUD.
     */
    public synchronized void processScore(int val) {
        score += val;
        if (val == 1000) time += 2; // Bonus mole adds time
        refreshDisplay();
    }

    /**
     * Updates on-screen values: score, high score, time.
     */
    private void refreshDisplay() {
        int top = leaders.isEmpty() ? 0 : leaders.get(0).getScore();
        app.updateHUD(score, top, Math.max(0, time));
    }

    /**
     * Loads and sorts leaderboard.
     */
    private void reloadScores() {
        try {
            leaders = storage.loadScores();
            leaders.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
        } catch (Exception e) {
            leaders = new ArrayList<>();
        }
    }

    /**
     * Ends game and saves high score if eligible.
     */
    private void finishGame() {
        running = false;
        boolean isRecord = leaders.size() < 5 || score > leaders.get(leaders.size() - 1).getScore();

        if (isRecord && score > 0) {
            SwingUtilities.invokeLater(() -> {
                String name = JOptionPane.showInputDialog(app, "New High Score! Name:");
                if (name == null || name.trim().isEmpty()) name = "Player";
                
                leaders.add(new PlayerScore(name, score));
                leaders.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
                if (leaders.size() > 5) leaders = leaders.subList(0, 5);
                
                try { storage.saveScores(leaders); } catch (Exception e) {}
                
                app.showGameOver(score, leaders);
                SoundManager.playGameOver();
            });
        } else {
            SwingUtilities.invokeLater(() -> {
                app.showGameOver(score, leaders);
                SoundManager.playGameOver();
            });
        }
    }
}
