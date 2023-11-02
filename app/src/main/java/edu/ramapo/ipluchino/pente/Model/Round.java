package edu.ramapo.ipluchino.pente.Model;

import java.util.Random;
import java.util.Vector;
import java.io.FileReader;
import java.io.FileWriter;

public class Round {
    //Constants
    final int NUM_PLAYERS = 2;
    final int HEADS = 1;
    final int TAILS = 2;

    //Holds a list of the players currently playing the game. In this case, there will be one human player and one computer player.
    //m_playerList[0] will be the human player.
    //m_playerList[1] will be the computer player.
    private Vector<Player> m_playerList;

    //The board for the round.
    private Board m_board;

    //The current player whose turn it is. Will be either 'H' for human or 'C' for computer.
    private int m_nextPlayerIndex;

    //File reader/writers to read and write to a file.
    private FileReader m_fileReader;
    private FileWriter m_fileWriter;

    //Default Constructor
    public Round()
    {
        //Initialize the human and computer player.
        m_playerList = new Vector<Player>();
        m_playerList.add(new Human());
        m_playerList.add(new Computer());

        //Initialize the board.
        m_board = new Board();

        //Initialize the next player index.
        m_nextPlayerIndex = -1;
    }

    //Copy constructor - to be implemented.
    public Round(Round a_otherRound)
    {

    }

    //Selectors
    public int GetHumanScore()
    {
        return m_playerList.get(0).GetScore();
    }

    public int GetComputerScore()
    {
        return m_playerList.get(1).GetScore();
    }

    public char GetHumanColor()
    {
        return m_playerList.get(0).GetColor();
    }

    public char GetComputerColor()
    {
        return m_playerList.get(1).GetColor();
    }

    public int GetHumanCapturedPairs()
    {
        return m_playerList.get(0).GetCapturedPairs();
    }

    public int GetComputerCapturedPairs()
    {
        return m_playerList.get(1).GetCapturedPairs();
    }

    //Mutators
    public boolean SetNextPlayerIndex(int a_index)
    {
        if (a_index != -1 && a_index != 0 && a_index != 1) return false;

        m_nextPlayerIndex = a_index;

        return true;
    }

    public boolean SetHumanScore(int a_score)
    {
        return m_playerList.get(0).SetScore(a_score);
    }

    public boolean SetComputerScore(int a_score)
    {
        return m_playerList.get(1).SetScore(a_score);
    }

    public boolean SetHumanColor(char a_color)
    {
        return m_playerList.get(0).SetColor(a_color);
    }

    public boolean SetComputerColor(char a_color)
    {
        return m_playerList.get(1).SetColor(a_color);
    }

    public boolean SetHumanCapturedPairs(int a_capturedPairs)
    {
        return m_playerList.get(0).SetCapturedPairs(a_capturedPairs);
    }

    public boolean SetComputerCapturedPairs(int a_capturedPairs)
    {
        return m_playerList.get(1).SetCapturedPairs(a_capturedPairs);
    }

    //POTENTIALLY IMPLEMENT - CHANGE TO PLAY HUMAN/COMPUTER TURN? NEED TO DECIDE.
    public void StartRound()
    {

    }

    //CONSIDER CHANGING VOID TO STRING?
    public void DetermineFirstPlayer()
    {
        //The player who has the higher score gets to play first for the round.
        //If the scores or tied, or a new tournament is started, the first player is determined via coin toss.

        if (GetHumanScore() > GetComputerScore())
        {
            SetHumanColor('W');
            SetComputerColor('B');
            SetNextPlayerIndex(0);

            System.out.println("You will be going first since you have a higher score.");
        }
        else if (GetComputerScore() > GetHumanScore())
        {
            SetComputerColor('W');
            SetHumanColor('B');
            SetNextPlayerIndex(1);

            System.out.println("The computer will be going first because the computer has a higher score.");
        }
        else
        {
            //REMOVE THIS PART??? FOR COIN TOSS --> COULD SEPARATELY CALL COINTOSS() IN VIEW CLASS...
        }

    }

    public boolean CoinToss(String a_choice)
    {
        Random rand = new Random();

        //Randomly generate either 1 or 2. 1 Represents heads, while 2 represents tails.
        int coin = 1 + rand.nextInt(2);

        //Output the coin toss result to the screen so the user can see if it was heads or tails.
        if (coin == HEADS)
        {
            System.out.println("The result of the coin toss was Heads!");
        }
        else
        {
            System.out.println("The result of the coin toss was Tails!");
        }

        //If the user correctly called the toss, return true, otherwise return false.
        return (a_choice.equals("H") && coin == HEADS) || (a_choice.equals("T") && coin == TAILS);
    }

    //Probably can remove this later.
    public void DisplayGame()
    {
        //Display the board first.
        m_board.DisplayBoard();

        //Display the Human's information.
        System.out.println("Human - " + GetHumanColor() + ":");
        System.out.println("Captured Pairs: " + GetHumanCapturedPairs());
        System.out.println("Tournament Score: " + GetHumanScore() + "\n");

        //Display the Computer's information.
        System.out.println("Computer - " + GetComputerColor() + ":");
        System.out.println("Captured Pairs: " + GetComputerCapturedPairs());
        System.out.println("Tournament Score: " + GetComputerScore() + "\n");

        //Display who will be playing next.
        if (m_nextPlayerIndex == 0)
        {
            System.out.print("Next Turn: Human - ");

            if (GetHumanColor() == 'W')
            {
                System.out.println("White\n");
            }
            else
            {
                System.out.println("Black\n");
            }
        }
        else
        {
            System.out.print("Next Turn: Computer - ");

            if (GetComputerColor() == 'W')
            {
                System.out.println("White\n");
            }
            else
            {
                System.out.println("Black\n");
            }
        }
    }

    public String RoundOver()
    {
        //If one of the players has achieved five consecutive pieces in any direction, the round is over.
        if (m_board.FiveConsecutive('W'))
        {
            if (GetHumanColor() == 'W')
            {
                return "The round has ended because the Human player has five consecutive stones on the board.";
            }
            else
            {
                return "The round has ended because the Computer player has five consecutive stones on the board.";
            }
        }
        else if (m_board.FiveConsecutive('B'))
        {
            if (GetHumanColor() == 'B')
            {
                return "The round has ended because the Human player has five consecutive stones on the board.";
            }
            else
            {
                return "The round has ended because the Computer player has five consecutive stones on the board.";
            }
        }
        //If one of the players has achieved at least 5 captured pairs, the round is over.
        else if (GetHumanCapturedPairs() >= 5)
        {
            return "The round has ended because the Human player has at least 5 captured pairs.";
        }
        else if (GetComputerCapturedPairs() >= 5)
        {
            return "The round has ended because the Computer player has at least 5 captured pairs.";
        }
        //If none of the above situations occurred but the board is full, the round is over.
        else if (m_board.IsBoardFull())
        {
            return "The round has ended because the board is full.";
        }

        //Otherwise, the round can continue (an empty string in this case means the round is not over).
        return "";
    }

    //Implement later
    public void SaveGame(String a_fileName)
    {

    }

    //Implement later
    public void LoadGameData(String a_fileName)
    {

    }

    public void UpdateScores()
    {
        SetHumanScore(GetHumanScore() + m_board.ScoreBoard(GetHumanColor(), GetHumanCapturedPairs()));
        SetComputerScore(GetComputerScore() + m_board.ScoreBoard(GetComputerColor(), GetComputerCapturedPairs()));
    }

    public void ResetRound()
    {
        m_board.ClearBoard();
        SetHumanCapturedPairs(0);
        SetComputerCapturedPairs(0);
        SetNextPlayerIndex(-1);
    }

    //CHANGE TO STRING SO IT CAN BE DISPLAYED TO SCREEN?
    public void DisplayRoundScore()
    {
        int humanRoundScore = m_board.ScoreBoard(GetHumanColor(), GetHumanCapturedPairs());
        int computerRoundScore = m_board.ScoreBoard(GetComputerColor(), GetComputerCapturedPairs());

        System.out.println("Points scored by the Human this round: " + humanRoundScore);
        System.out.println("Points scored by the Computer this round: " + computerRoundScore);
    }

    public String PlayTurn(String a_location)
    {
        //Let the player make their move.
        String move = m_playerList.get(m_nextPlayerIndex).MakePlay(m_board, a_location);

        //Switch turns so the correct player plays next.
        SetNextPlayerIndex((m_nextPlayerIndex + 1) % NUM_PLAYERS);

        return move;
    }

    public String ValidMove(String a_location)
    {
        //There is a handicap for the second turn of the first player. The play must be within three intersections of the center piece.
        boolean handicap = m_board.CountPieces('W') == 1 && m_board.CountPieces('B') == 1;

        if (m_board.IsEmptyBoard() && !a_location.equals("J10"))
        {
            return "The first stone of the game must be played on J10!";
        }
        else if (handicap && ((a_location.charAt(0) > 'G' && a_location.charAt(0) < 'M') && Integer.parseInt(a_location.substring(1)) > 7 && Integer.parseInt(a_location.substring(1)) < 13))
        {
            return "The location must be at least three intersections away from the center (J10) on this turn!";
        }

        //If the location is valid, return an empty string.
        return "";
    }

    public char StoneAt(int a_row, int a_col)
    {
        Vector<Vector<Character>> board = m_board.GetBoard();
        return board.get(a_row).get(a_col);
    }

    public Vector<String> GetHelp()
    {
        Human humanPlayer = (Human) m_playerList.get(0);
        return humanPlayer.AskForHelp(m_board);
    }

    public boolean ScoresTied()
    {
        return GetHumanScore() == GetComputerScore();
    }

    public boolean IsHumanTurn()
    {
        return m_nextPlayerIndex == 0;
    }





    public static void main(String[] args)
    {
        Round r = new Round();
        r.SetComputerScore(1);
        r.DetermineFirstPlayer();

        r.DisplayGame();

    }
}

