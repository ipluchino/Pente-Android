package edu.ramapo.ipluchino.pente.Model;

import java.util.Vector;

public class Computer extends Player {

    /**
     Default constructor for the Computer class.
     */
    public Computer() { }

    /**
     The main function of the Computer class - used for testing purposes.
     @param args An array of strings, representing command line arguments.
     */
    public static void main(String[] args)
    {
        Board b = new Board();
        Computer c = new Computer();

        c.MakePlay(b, "");
    }

    /**
     Lets the Computer player make its play - virtual function from the Player class.
     @param a_board A Board object, representing the current board of the round.
     @param a_location A string, representing the location the Computer player is placing their stone.
     @return A string, representing the description of the Computer player's move for logging purposes.
     */
    public String MakePlay(Board a_board, String a_location)
    {
        Vector<String> playInfo = OptimalPlay(a_board, m_color);
        String location = playInfo.get(0);
        String reasoning = playInfo.get(1);

        //Place the stone on the board.
        a_board.PlaceStone(location.charAt(0), Integer.parseInt(location.substring(1)), m_color);

        //Clear any captures and update the player's capture count, if any occur.
        int captures = a_board.ClearCaptures(location.charAt(0), Integer.parseInt(location.substring(1)), m_color);
        m_capturedPairs += captures;

        return reasoning;
    }
}

