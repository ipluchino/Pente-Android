/**
 **************************************************************
 * Name: Ian Pluchino                                         *
 * Project: OPL Project 3 Java/Android                        *
 * Class: StrategyConstants class                             *
 * Date: 11/15/23                                             *
 **************************************************************
 */

package edu.ramapo.ipluchino.pente.Model;

import java.io.Serializable;
import java.util.Vector;
import java.util.Arrays;

public class StrategyConstants implements Serializable {
    // Size of the board will always be 19x19.
    public static final int BOARD_SIZE = 19;

    // Represents the instructions for the 8 possible directions on the board.
    // Left, Right, Down, Up, and the four possible diagonals.
    // Syntax from: https://stackoverflow.com/questions/1005073/initialization-of-an-arraylist-in-one-line
    public static final Vector<Vector<Integer>> DIRECTIONS = new Vector<Vector<Integer>>()
    {
        {
            add(new Vector<Integer>(Arrays.asList(0, -1)));
            add(new Vector<Integer>(Arrays.asList(0, 1)));
            add(new Vector<Integer>(Arrays.asList(1, 0)));
            add(new Vector<Integer>(Arrays.asList(-1, 0)));
            add(new Vector<Integer>(Arrays.asList(1, 1)));
            add(new Vector<Integer>(Arrays.asList(-1, -1)));
            add(new Vector<Integer>(Arrays.asList(1, -1)));
            add(new Vector<Integer>(Arrays.asList(-1, 1)));
        }
    };

    // Represents the names of each of the eight directions.
    public static final Vector<String> DIRECTION_NAMES = new Vector<String>(Arrays.asList("horizontal", "horizontal", "vertical", "vertical", "main-diagonal", "main-diagonal", "anti-diagonal", "anti-diagonal"));

    // Represents the total number of possible directions, in this case 8.
    public static final int NUM_DIRECTIONS = 8;

    // Represents the number of spaces needed to search from a current location on the board to find a capture.
    public static final int CAPTURE_DISTANCE = 3;

    // Represents the increment required to skip over opposite directions.
    // Ex: When searching horizontals, left & right direction searches would be the same.
    public static final int DIRECTIONAL_OFFSET = 2;

    // Represents the total number of spaces required to make three consecutive stones.
    public static final int CONSECUTIVE_3_DISTANCE = 3;

    // Represents the total number of spaces required to make four consecutive stones.
    public static final int CONSECUTIVE_4_DISTANCE = 4;

    // Represents the total number of spaces required to make five consecutive stones.
    public static final int CONSECUTIVE_5_DISTANCE = 5;
}
