package edu.ramapo.ipluchino.pente.Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Vector;
import java.io.FileWriter;

public class Round implements Serializable {
    //Constants
    final int NUM_PLAYERS = 2;

    //Holds a list of the players currently playing the game. In this case, there will be one human player and one computer player.
    //m_playerList[0] will be the human player.
    //m_playerList[1] will be the computer player.
    private Vector<Player> m_playerList;

    //The board for the round.
    private Board m_board;

    //The current player whose turn it is. Will be either 'H' for human or 'C' for computer.
    private int m_nextPlayerIndex;

    /**
     Default constructor of the Round class.
     */
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

    /**
     Copy constructor of the Round class.
     */
    public Round(Round a_otherRound)
    {
        //Create the Human and Computer objects.
        m_playerList.add(new Human());
        m_playerList.add(new Computer());

        //Copy the board.
        m_board = new Board(a_otherRound.m_board);

        //Copy the Human information.
        SetHumanColor(a_otherRound.GetHumanColor());
        SetHumanScore(a_otherRound.GetHumanScore());
        SetHumanCapturedPairs(a_otherRound.GetHumanCapturedPairs());

        //Copy the Computer information.
        SetComputerColor(a_otherRound.GetComputerColor());
        SetComputerScore(a_otherRound.GetComputerScore());
        SetComputerCapturedPairs(a_otherRound.GetComputerCapturedPairs());

        //Copy the next turn information.
        SetNextPlayerIndex(a_otherRound.m_nextPlayerIndex);
    }

    /**
     Gets the Human player's tournament score.
     @return An integer, representing the Human's tournament score.
     */
    public int GetHumanScore()
    {
        return m_playerList.get(0).GetScore();
    }

    /**
     Gets the Computer player's tournament score.
     @return An integer, representing the Computer's tournament score.
     */
    public int GetComputerScore()
    {
        return m_playerList.get(1).GetScore();
    }

    /**
     Gets the Human player's stone color.
     @return A character, representing the Human's stone color.
     */
    public char GetHumanColor()
    {
        return m_playerList.get(0).GetColor();
    }

    /**
     Gets the Computer player's stone color.
     @return A character, representing the Computer's stone color.
     */
    public char GetComputerColor()
    {
        return m_playerList.get(1).GetColor();
    }

    /**
     Gets the Human player's captured pair count.
     @return An integer, representing the Human's captured pair count.
     */
    public int GetHumanCapturedPairs()
    {
        return m_playerList.get(0).GetCapturedPairs();
    }

    /**
     Gets the Computer player's captured pair count.
     @return An integer, representing the Computer's captured pair count.
     */
    public int GetComputerCapturedPairs()
    {
        return m_playerList.get(1).GetCapturedPairs();
    }

    /**
     Gets the next player index.
     @return An integer, representing the next player index of the round.
     */
    public int GetNextPlayerIndex() { return m_nextPlayerIndex; }

    /**
     Sets the next player index.
     @param a_index An integer, representing what the next player index should be set to.
     @return A boolean, whether or not the next player index was successfully set.
     */
    public boolean SetNextPlayerIndex(int a_index)
    {
        if (a_index != -1 && a_index != 0 && a_index != 1) return false;

        m_nextPlayerIndex = a_index;

        return true;
    }

    /**
     Sets the Human player's tournament score.
     @param a_score An integer, representing the tournament score to set the Human's tournament score to.
     @return A boolean, whether or not the Human's tournament score was successfully set.
     */
    public boolean SetHumanScore(int a_score)
    {
        return m_playerList.get(0).SetScore(a_score);
    }

    /**
     Sets the Computer player's tournament score
     @param a_score An integer, representing the tournament score to set the Computer's tournament score to.
     @return A boolean, whether or not the Computer's tournament score was successfully set.
     */
    public boolean SetComputerScore(int a_score)
    {
        return m_playerList.get(1).SetScore(a_score);
    }

    /**
     Sets the Human player's stone color.
     @param a_color A character, representing the color to set the Human's stone color to.
     @return A boolean, whether or not the Human's stone color was successfully set.
     */
    public boolean SetHumanColor(char a_color)
    {
        return m_playerList.get(0).SetColor(a_color);
    }

    /**
     Sets the computer player's stone color.
     @param a_color A character, representing the color to set the Computer's stone color to.
     @return A boolean, whether or not the Computer's stone color was successfully set.
     */
    public boolean SetComputerColor(char a_color)
    {
        return m_playerList.get(1).SetColor(a_color);
    }

    /**
     Sets the Human player's captured pair count.
     @param a_capturedPairs An integer, representing the captured pair count the Human's captured pair count is being set to.
     @return A boolean, whether or not the Human's captured pair count was successfully set.
     */
    public boolean SetHumanCapturedPairs(int a_capturedPairs)
    {
        return m_playerList.get(0).SetCapturedPairs(a_capturedPairs);
    }

    /**
     Sets the Computer player's captured pair count.
     @param a_capturedPairs An integer, representing the captured pair count the Computer's captured pair count is being set to.
     @return A boolean, whether or not the Computer's captured pair count was successfully set.
     */
    public boolean SetComputerCapturedPairs(int a_capturedPairs)
    {
        return m_playerList.get(1).SetCapturedPairs(a_capturedPairs);
    }

    /**
     The main function of the Round class - used for testing purposes.
     @param args An array of strings, representing command line arguments.
     */
    public static void main(String[] args)
    {
        Round r = new Round();
        r.SetComputerScore(1);
        r.DetermineFirstPlayerViaScore();

        r.DisplayGame();
    }

    /**
     Sets the Human player to go first for the round.
     */
    public void SetHumanFirst()
    {
        SetHumanColor('W');
        SetComputerColor('B');
        SetNextPlayerIndex(0);
    }

    /**
     Sets the Computer player to go first for the round.
     */
    public void SetComputerFirst()
    {
        SetComputerColor('W');
        SetHumanColor('B');
        SetNextPlayerIndex(1);
    }

    /**
     Determines and sets who should go first at the start of the round, via tournament scores.
     */
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

    /**
     Displays all of the information about a current round of Pente textually.
     */
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

    /**
     Determines if the current round of Pente has ended.
     @return A string, representing if the current round of Pente is over. An empty string means the round is not over.
     */
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

    /**
     Saves the Pente tournament to a file.
     @param a_fileName A string, representing the name of the file to save the tournament to.
     @return An IOException will be thrown if one occurs.
     Assistance Received: https://www.w3schools.com/java/java_files_create.asp
                          https://stackoverflow.com/questions/15711098/trying-to-create-a-file-in-android-open-failed-erofs-read-only-file-system
     */
    public void SaveGame(String a_fileName) throws IOException
    {
        File file = new File(a_fileName);

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
            fileWriter.write("Captured pairs: " + GetHumanCapturedPairs() + "\n");
            fileWriter.write("Score: " + GetHumanScore() + "\n\n");

            //Write the computer's game information to the file.
            fileWriter.write("Computer:\n");
            fileWriter.write("Captured pairs: " + GetComputerCapturedPairs() + "\n");
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

    /**
     Loads a tournament from a file.
     @param a_inputStream An InputStream object, representing the InputStream of the file being read from.
     @return A boolean, representing whether or not the tournament was successfully loaded from the file. An IOException will be thrown if one occurs.
     Assistance Received: https://www.baeldung.com/java-buffered-reader
     */
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
            return false;
        }

        if (!SetHumanCapturedPairs(humanCaptured) || !SetHumanScore(humanScore) || !SetHumanColor(humanColor))
        {
            return false;
        }

        if (!SetComputerCapturedPairs(computerCaptured) || !SetComputerScore(computerScore) || !SetComputerColor(computerColor))
        {
            return false;
        }

        if (!SetNextPlayerIndex(nextPlayerIndex))
        {
            return false;
        }

        return true;
    }

    /**
     Updates the tournament scores of both player after the conclusion of a round.
     */
    public void UpdateScores()
    {
        SetHumanScore(GetHumanScore() + m_board.ScoreBoard(GetHumanColor(), GetHumanCapturedPairs()));
        SetComputerScore(GetComputerScore() + m_board.ScoreBoard(GetComputerColor(), GetComputerCapturedPairs()));
    }

    /**
     Resets the round information in preparation to play another round, if the user wishes to.
     */
    public void ResetRound()
    {
        m_board.ClearBoard();
        SetHumanCapturedPairs(0);
        SetComputerCapturedPairs(0);
        SetNextPlayerIndex(-1);
    }

    /**
     Determines the score earned by the Human player after a round has concluded.
     @return An integer, representing the Human player's tournament score.
     */
    public int ScoreHuman()
    {
        return m_board.ScoreBoard(GetHumanColor(), GetHumanCapturedPairs());
    }

    /**
     Determines the score earned by the Computer player after a round has concluded.
     @return An integer, representing the Computer player's tournament score.
     */
    public int ScoreComputer()
    {
        return m_board.ScoreBoard(GetComputerColor(), GetComputerCapturedPairs());
    }

    /**
     Plays through one turn of the current round of Pente.
     @param a_location A string, representing the location of the stone being placed.
     @return A string, describing the move that was just made. Used for logging purposes.
     */
    public String PlayTurn(String a_location)
    {
        //Let the player make their move.
        String move = m_playerList.get(m_nextPlayerIndex).MakePlay(m_board, a_location);

        //Switch turns so the correct player plays next.
        SetNextPlayerIndex((m_nextPlayerIndex + 1) % NUM_PLAYERS);

        return move;
    }

    /**
     Determines if a provided move is valid given the current round conditions.
     @param a_location A string, representing the location being checked.
     @return A string, representing whether or not the location is valid. An empty string represents the move is valid, otherwise the move is invalid.
     */
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

    /**
     Determines if there is a stone at a provided location on the board.
     @param a_row An integer, representing the row of the location being checked.
     @param a_col An integer, representing the column of the location being checked.
     @return A character, representing what is currently at the location being checked.
     */
    public char StoneAt(int a_row, int a_col)
    {
        Vector<Vector<Character>> board = m_board.GetBoard();
        return board.get(a_row).get(a_col);
    }

    /**
     Generates the most optimal move for the Human player given the current conditions of the round.
     @return A Vector of strings representing the location of the optimal play and the reasoning behind the most optimal play.
     */
    public Vector<String> GetHelp()
    {
        Human humanPlayer = (Human) m_playerList.get(0);
        return humanPlayer.AskForHelp(m_board);
    }

    /**
     Determines if the scores of the Human player and Computer player are equal.
     @return A boolean, whether or not the scores of both players are tied.
     */
    public boolean ScoresTied()
    {
        return GetHumanScore() == GetComputerScore();
    }

    /**
     Determines if it is currently the Human's turn.
     @return A boolean, whether or not it is the Human player's turn.
     */
    public boolean IsHumanTurn()
    {
        return m_nextPlayerIndex == 0;
    }
}

