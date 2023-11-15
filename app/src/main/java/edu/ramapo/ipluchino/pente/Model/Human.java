/**
 **************************************************************
 * Name: Ian Pluchino                                         *
 * Project: OPL Project 3 Java/Android                        *
 * Class: Human class                                         *
 * Date: 11/15/23                                             *
 **************************************************************
 */

package edu.ramapo.ipluchino.pente.Model;

import java.util.Vector;

public class Human extends Player {

    /**
     Default constructor for the Human class.
     */
    public Human() { }

    /**
     The main function of the Human class - used for testing purposes.
     @param args An array of strings, representing command line arguments.
     */
    public static void main(String[] args)
    {
        Board b = new Board();
        Human h = new Human();

        System.out.println(h.AskForHelp(b));
    }

    /**
     Lets the Human player make its play - virtual function from the Player class.
     @param a_board A Board object, representing the current board of the round.
     @param a_location A string, representing the location the Human player is placing their stone.
     @return A string, representing the description of the Human player's move for logging purposes.
     */
    public String MakePlay(Board a_board, String a_location)
    {
        //Place the stone on the board.
        a_board.PlaceStone(a_location.charAt(0), Integer.parseInt(a_location.substring(1)), m_color);

        //Clear any captures and update the player's capture count, if any occur.
        int captures = a_board.ClearCaptures(a_location.charAt(0), Integer.parseInt(a_location.substring(1)), m_color);
        m_capturedPairs += captures;

        String playMade = "The human placed their stone on " + a_location + ".";
        return playMade;
    }

    /**
     Gets help for the Human player by determining the most optimal play.
     @param a_board A Board object, representing the current board of the round.
     @return  A Vector of strings, representing the most optimal play, as well as the reasoning on why it is the most optimal play.
     */
    public Vector<String> AskForHelp(Board a_board)
    {
        Vector<String> result = new Vector<String>();
        Vector<String> playInfo = OptimalPlay(a_board, m_color);
        String explanation = playInfo.get(1);

        //Alter the output of the explanation to present it as a suggestion.
        explanation = explanation.replaceFirst("placed", "recommends you place");

        //Return the location, along with the explanation in a vector.
        result.add(playInfo.get(0));
        result.add(explanation);
        return result;
    }
}
