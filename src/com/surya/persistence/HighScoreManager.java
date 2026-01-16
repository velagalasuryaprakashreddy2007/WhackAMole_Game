package com.surya.persistence;

import com.surya.exceptions.HighScoreException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving and loading high scores from a file.
 */
public class HighScoreManager {

    private static final String HIGH_SCORE_FILE = "scores.dat";

    /**
     * Saves a list of scores to disk using serialization.
     */
    public void saveScores(List<PlayerScore> scores) throws HighScoreException {
        try (ObjectOutputStream oos =
                new ObjectOutputStream(new FileOutputStream(HIGH_SCORE_FILE))) {

            oos.writeObject(scores);

        } catch (IOException e) {
            throw new HighScoreException(
                "Could not save scores to " + HIGH_SCORE_FILE, e);
        }
    }

    /**
     * Loads the list of saved scores from disk.
     * Returns an empty list if the file is missing.
     */
    @SuppressWarnings("unchecked")
    public List<PlayerScore> loadScores() throws HighScoreException {
        File file = new File(HIGH_SCORE_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois =
                new ObjectInputStream(new FileInputStream(file))) {

            return (List<PlayerScore>) ois.readObject();

        } catch (FileNotFoundException e) {
            return new ArrayList<>();

        } catch (IOException e) {
            throw new HighScoreException("Error reading score file.", e);

        } catch (ClassNotFoundException e) {
            throw new HighScoreException("Score data is corrupted.", e);
        }
    }
}
