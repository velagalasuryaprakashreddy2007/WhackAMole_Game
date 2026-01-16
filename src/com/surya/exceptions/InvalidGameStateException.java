package com.surya.exceptions;

/**
 * Custom unchecked exception for invalid game states.
 */
public class InvalidGameStateException extends RuntimeException {

    /**
     * Creates the exception with a message.
     */
    public InvalidGameStateException(String message) {
        super(message);
    }
}
