package edu.ramapo.ipluchino.pente.Model;

import java.util.Vector;

public class Human extends Player {

    public Human() { }

    //CONVERT RETURN VALUE TO STRING OR VECTOR? SO THE HUMAN PLAY HISTORY CAN BE SAVED?
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

    public String AskForHelp(Board a_board)
    {
        Vector<String> playInfo = OptimalPlay(a_board, m_color);
        String explanation = playInfo.get(1);

        //Alter the output of the explanation to present it as a suggestion.
        explanation = explanation.replaceFirst("placed", "recommends you place");
        return explanation;
    }

    public static void main(String[] args)
    {
        Board b = new Board();
        Human h = new Human();

        System.out.println(h.AskForHelp(b));
    }
}
