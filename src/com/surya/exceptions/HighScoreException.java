package com.surya.exceptions;

/**
 * Custom checked exception for handling high-score related errors.
 */
public class HighScoreException extends Exception {

    /**
     * Creates an exception with a message.
     */
    public HighScoreException(String message) {
        super(message);
    }

    /**
     * Creates an exception with a message and the original cause.
     */
    public HighScoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
