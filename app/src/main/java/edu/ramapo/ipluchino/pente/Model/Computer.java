package edu.ramapo.ipluchino.pente.Model;

import java.util.Vector;

public class Computer extends Player {

    public Computer() { }

    //CONVERT RETURN VALUE TO STRING OR VECTOR? SO THE EXPLANATION IS RETURNED.
    public boolean MakePlay(Board a_board, String a_location)
    {
        Vector<String> playInfo = OptimalPlay(a_board, m_color);
        String location = playInfo.get(0);
        String reasoning = playInfo.get(1);

        //Place the stone on the board.
        a_board.PlaceStone(location.charAt(0), Integer.parseInt(location.substring(1)), m_color);

        //Explain the play to the user.
        System.out.println(reasoning);

        //Clear any captures and update the player's capture count, if any occur.
        int captures = a_board.ClearCaptures(location.charAt(0), Integer.parseInt(location.substring(1)), m_color);
        m_capturedPairs += captures;

        return true;
    }

    public static void main(String[] args)
    {
        Board b = new Board();
        Computer c = new Computer();

        c.MakePlay(b, "");

    }
}

