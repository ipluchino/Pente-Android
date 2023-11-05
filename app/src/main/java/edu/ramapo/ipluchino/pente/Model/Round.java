package edu.ramapo.ipluchino.pente.Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Random;
import java.util.Vector;
import java.io.FileWriter;

public class Round implements Serializable {
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

    public int GetNextPlayerIndex() { return m_nextPlayerIndex; }

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

    public void SetHumanFirst()
    {
        SetHumanColor('W');
        SetComputerColor('B');
        SetNextPlayerIndex(0);
    }

    public void SetComputerFirst()
    {
        SetComputerColor('W');
        SetHumanColor('B');
        SetNextPlayerIndex(1);
    }

    public void DetermineFirstPlayerViaScore()
    {
        //The player who has the higher score gets to play first for the round if the scores are not tied.
        if (GetHumanScore() > GetComputerScore())
        {
            SetHumanFirst();
        }
        else if (GetComputerScore() > GetHumanScore())
        {
            SetComputerFirst();
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

    //https://www.w3schools.com/java/java_files_create.asp
    //https://stackoverflow.com/questions/15711098/trying-to-create-a-file-in-android-open-failed-erofs-read-only-file-system
    public void SaveGame(String a_fileName, String downloadFolder) throws IOException
    {
        String fullPath = downloadFolder + "/" + a_fileName;
        File file = new File(fullPath);

        try
        {
            FileWriter fileWriter = new FileWriter(file);

            //Write the board to the file.
            Vector<Vector<Character>> board = m_board.GetBoard();

            fileWriter.write("Board:\n");
            for (int row = 0; row < board.size(); row++)
            {
                for (int col = 0; col < board.size(); col++)
                {
                    if (board.get(row).get(col) == '-')
                    {
                        fileWriter.write('O');
                    }
                    else
                    {
                        fileWriter.write(board.get(row).get(col));
                    }
                }

                fileWriter.write("\n");
            }

            //Write the human's game information to the file.
            fileWriter.write("\nHuman:\n");
            fileWriter.write("Captured Pairs: " + GetHumanCapturedPairs() + "\n");
            fileWriter.write("Score: " + GetHumanScore() + "\n\n");

            //Write the computer's game information to the file.
            fileWriter.write("Computer:\n");
            fileWriter.write("Captured Pairs: " + GetComputerCapturedPairs() + "\n");
            fileWriter.write("Score: " + GetComputerScore() + "\n\n");

            //Write the next player's turn to the file.
            fileWriter.write("Next Player: ");
            if (m_nextPlayerIndex == 0)
            {
                fileWriter.write("Human - ");
                if (GetHumanColor() == 'W')
                {
                    fileWriter.write("White");
                }
                else
                {
                    fileWriter.write("Black");
                }

            }
            else
            {
                fileWriter.write("Computer - ");
                if (GetComputerColor() == 'W')
                {
                    fileWriter.write("White");
                }
                else
                {
                    fileWriter.write("Black");
                }
            }

            fileWriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //https://www.baeldung.com/java-buffered-reader
    public boolean LoadGameData(InputStream a_inputStream) throws IOException
    {
        Vector<Vector<Character>> board = new Vector<Vector<Character>>();

        int humanCaptured = 0, computerCaptured = 0, humanScore = 0, computerScore = 0, nextPlayerIndex = 0, pos = 0;
        char humanColor = ' ', computerColor = ' ';

        try
        {
            //File reader/writers to read and write to a file.
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(a_inputStream));

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                //First read in the board.
                if (line.contains("Board:"))
                {
                    for (int i = 0; i < 19; i++)
                    {
                        line = bufferedReader.readLine();

                        Vector<Character> row = new Vector<Character>();

                        //Loop through the entire line to read in the row.
                        for (int j = 0; j < line.length(); j++)
                        {
                            if (line.charAt(j) == 'O')
                            {
                                row.add('-');
                            }
                            else
                            {
                                row.add(line.charAt(j));
                            }

                        }

                        board.add(row);
                    }
                }

                //Next read in the human information
                if (line.contains("Human:"))
                {
                    line = bufferedReader.readLine();

                    //Obtaining the number of captures for the human player.
                    pos = line.indexOf(":") + 2;
                    humanCaptured = Integer.parseInt(line.substring(pos));

                    //Obtaining the score for the human player.
                    line = bufferedReader.readLine();
                    pos = line.indexOf(":") + 2;
                    humanScore = Integer.parseInt(line.substring(pos));
                }

                //Next read in the computer information
                if (line.contains("Computer:"))
                {
                    line = bufferedReader.readLine();

                    //Obtaining the number of captures for the human player.
                    pos = line.indexOf(":") + 2;
                    computerCaptured = Integer.parseInt(line.substring(pos));

                    //Obtaining the score for the human player.
                    line = bufferedReader.readLine();
                    pos = line.indexOf(":") + 2;
                    computerScore = Integer.parseInt(line.substring(pos));
                }

                //Last, read in the next player information.
                if (line.contains("Next Player:"))
                {
                    if (line.contains("Human"))
                    {
                        //Human plays next
                        nextPlayerIndex = 0;

                        //Determine the colors of the players.
                        if (line.contains("White"))
                        {
                            humanColor = 'W';
                            computerColor = 'B';
                        }
                        else
                        {
                            humanColor = 'B';
                            computerColor = 'W';
                        }
                    }
                    else
                    {
                        //Computer plays next
                        nextPlayerIndex = 1;

                        //Determine the colors of the players.
                        if (line.contains("White"))
                        {
                            computerColor = 'W';
                            humanColor = 'B';
                        }
                        else
                        {
                            computerColor = 'B';
                            humanColor = 'W';
                        }
                    }
                }
            }

            bufferedReader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        //Set the round's data to the variables read in.
        if (!m_board.SetBoard(board))
        {
            System.out.println("Could not correctly load board from the file!");
            return false;
        }

        if (!SetHumanCapturedPairs(humanCaptured) || !SetHumanScore(humanScore) || !SetHumanColor(humanColor))
        {
            System.out.println("Could not correctly load the human's information from the file!");
            return false;
        }

        if (!SetComputerCapturedPairs(computerCaptured) || !SetComputerScore(computerScore) || !SetComputerColor(computerColor))
        {
            System.out.println("Could not correctly load the computer's information from the file!");
            return false;
        }

        if (!SetNextPlayerIndex(nextPlayerIndex))
        {
            System.out.println("Could not correctly load the next player from the file!");
            return false;
        }

        return true;
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

    public int ScoreHuman()
    {
        return m_board.ScoreBoard(GetHumanColor(), GetHumanCapturedPairs());
    }

    public int ScoreComputer()
    {
        return m_board.ScoreBoard(GetComputerColor(), GetComputerCapturedPairs());
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
        r.DetermineFirstPlayerViaScore();

        r.DisplayGame();

    }
}

