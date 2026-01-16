package com.surya.game;

import com.surya.exceptions.InvalidGameStateException;
import com.surya.objects.HoleOccupant;
import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Represents the 12-hole grid used in the game.
 */
public class GameGrid {

    private final HoleOccupant[] occupants = new HoleOccupant[12]; // Stores objects in holes

    /**
     * Clears all holes.
     */
    public void resetGrid() {
        Arrays.fill(occupants, null);
    }

    /**
     * Returns the object in the given hole.
     */
    public HoleOccupant getOccupant(int id) {
        if (id < 0 || id >= 12)
            throw new IndexOutOfBoundsException("Bad ID: " + id);
        return occupants[id];
    }

    /**
     * Places an object in a hole if it's empty.
     */
    public void setOccupant(int id, HoleOccupant obj) {
        if (occupants[id] != null) {
            throw new InvalidGameStateException("Slot " + id + " is occupied!");
        }
        occupants[id] = obj;
    }

    /**
     * Removes the object from a hole.
     */
    public void clearOccupant(int id) {
        occupants[id] = null;
    }

    /**
     * Returns all occupied hole IDs.
     */
    public int[] getOccupiedHoleIds() {
        return IntStream.range(0, 12)
                .filter(i -> occupants[i] != null)
                .toArray();
    }

    /**
     * Returns the number of holes.
     */
    public int getSize() {
        return 12;
    }
}
