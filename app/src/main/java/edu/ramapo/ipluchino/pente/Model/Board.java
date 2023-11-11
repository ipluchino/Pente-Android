package edu.ramapo.ipluchino.pente.Model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Vector;

public class Board implements Serializable {
    // Holds all the data for the entire board.
    private Vector<Vector<Character>> m_board;

    /**
     Default constructor of the Board class.
     */
    public Board()
    {
        m_board = new Vector<Vector<Character>>(StrategyConstants.BOARD_SIZE);

        //Set each value to '-' to represent empty spaces.
        for (int row = 0; row < StrategyConstants.BOARD_SIZE; row++)
        {
            Vector<Character> aRow = new Vector<>();
            for (int col = 0; col < StrategyConstants.BOARD_SIZE; col++)
            {
                aRow.add('-');
            }
            m_board.add(aRow);
        }
    }

    /**
     Copy constructor of the Board class.
     */
    public Board(Board a_otherBoard)
    {
        m_board = a_otherBoard.GetBoard();
    }

    /**
     Gets a copy of the vector that holds all of the board data.
     @return The 2-D vector representing the current board.
     */
    public Vector<Vector<Character>> GetBoard() {
        //Return a copy of the board, not a reference to it.
        Vector<Vector<Character>> copy = new Vector<>();

        for (int row = 0; row < StrategyConstants.BOARD_SIZE; row++)
        {
            Vector<Character> aRow = new Vector<Character>(StrategyConstants.BOARD_SIZE);
            for (int col = 0; col < StrategyConstants.BOARD_SIZE; col++)
            {
                aRow.add(m_board.get(row).get(col));
            }

            copy.add(aRow);
        }

        return copy;
    }

    /**
     Sets the board vector of the Board class.
     @param a_board A 2-D Vector of characters representing a Pente board.
     @return A boolean, whether or not the board was successfully set.
     */
    public boolean SetBoard(Vector<Vector<Character>> a_board)
    {
        //The board must be 19x19 to be considered valid.
        if (a_board.size() != StrategyConstants.BOARD_SIZE) return false;

        for (int i = 0; i < a_board.size(); i++)
        {
            if (a_board.get(i).size() != StrategyConstants.BOARD_SIZE) return false;
        }

        //The only valid characters for the board are White ('W'), Black ('B'), and empty locations ('-')
        for (int row = 0; row < a_board.size(); row++)
        {
            for (int col = 0; col < a_board.size(); col++)
            {
                if (a_board.get(row).get(col) != '-' && a_board.get(row).get(col) != 'W' && a_board.get(row).get(col) != 'B')
                {
                    return false;
                }
            }
        }

        m_board = a_board;

        return true;
    }

    /**
     Places a stone on the board.
     @param a_column A character, representing the column of the board to place on.
     @param a_row An integer, representing the row of the board to place on.
     @param a_stoneColor A character, representing the stone color that is being placed on the board.
     @return A boolean, whether or not the stone was successfully placed on the board.
     */
    public boolean PlaceStone(char a_column, int a_row, char a_stoneColor)
    {
        if (a_column < 'A' || a_column > 'S') return false;

        if (a_row < 0 || a_row > 19) return false;

        //Must convert the location's column as a character to its numeric value so it can be located on the board.
        int numericColumn = CharacterToInt(a_column);

        //Must convert the location's row to its correct numeric value since the rows are labeled 1-19 starting from the bottom (opposite of index order).
        a_row = ConvertRowIndex(a_row);


        //Place the stone on the board.
        m_board.get(a_row).set(numericColumn, a_stoneColor);

        return true;

    }

    /**
     Removes a stone from the board.
     @param a_column A character, representing the column of the stone that is being removed.
     @param a_row An integer, representing the row of the stone that is being removed.
     @return A boolean, whether or not the stone was successfully placed on the board.
     */
    public boolean RemoveStone(char a_column, int a_row)
    {
        if (a_column < 'A' || a_column > 'S') return false;

        if (a_row < 0 || a_row > 19) return false;

        //Must convert the location's column as a character to its numeric value so it can be located on the board.
        int numericColumn = CharacterToInt(a_column);

        //Must convert the location's row to its correct numeric value since the rows are labeled 1-19 starting from the bottom.
        a_row = ConvertRowIndex(a_row);

        m_board.get(a_row).set(numericColumn, '-');

        return true;
    }

    /**
     The main function of the Board class - used for testing purposes.
     @param args An array of strings, representing command line arguments.
     */
    public static void main(String[] args)
    {
        Vector<Vector<Character>> testBoard = new Vector<Vector<Character>>() {
            {
                add(new Vector<Character>(Arrays.asList('-', '-', '-', '-', '-', '-', '-', '-', 'W', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-')));
                add(new Vector<Character>(Arrays.asList('-', '-', '-', 'W', '-', '-', '-', 'W', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-')));
                add(new Vector<Character>(Arrays.asList('-', '-', '-', '-', 'W', '-', 'W', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-')));
                add(new Vector<Character>(Arrays.asList('-', '-', '-', '-', '-', 'W', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-')));
                add(new Vector<Character>(Arrays.asList('-', '-', '-', '-', '-', '-', 'W', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-')));
                add(new Vector<Character>(Arrays.asList('-', '-', '-', '-', '-', '-', '-', 'W', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-')));
                add(new Vector<Character>(Arrays.asList('-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-')));
                add(new Vector<Character>(Arrays.asList('-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-')));
                add(new Vector<Character>(Arrays.asList('-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-')));
                add(new Vector<Character>(Arrays.asList('-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-')));
                add(new Vector<Character>(Arrays.asList('-', '-', '-', '-', '-', '-', '-', 'W', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-')));
                add(new Vector<Character>(Arrays.asList('-', '-', '-', '-', '-', '-', '-', 'W', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-')));
                add(new Vector<Character>(Arrays.asList('-', '-', '-', '-', '-', '-', 'W', 'W', 'W', 'W', '-', '-', '-', '-', '-', '-', '-', '-', '-')));
                add(new Vector<Character>(Arrays.asList('-', '-', '-', '-', '-', 'B', '-', 'W', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-')));
                add(new Vector<Character>(Arrays.asList('-', '-', '-', '-', 'W', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-')));
                add(new Vector<Character>(Arrays.asList('-', '-', '-', 'W', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-')));
                add(new Vector<Character>(Arrays.asList('-', '-', 'W', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-')));
                add(new Vector<Character>(Arrays.asList('-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-')));
                add(new Vector<Character>(Arrays.asList('-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-')));

            }
        };

        Board b = new Board();
        b.SetBoard(testBoard);
        b.DisplayBoard();

        System.out.println(b.ScoreBoard('W', 0));
    }

    /**
     Displays the board textually on to the screen.
     */
    public void DisplayBoard()
    {
        //Print the column headers at the top of the board (A-S going from left to right)
        System.out.print("     ");
        for (char col = 'A'; col <= 'S'; col++)
        {
            System.out.print(col + " ");
        }
        System.out.print("\n\n");

        //Print the entire board and the row headers to the left of each row (1-19 going from bottom to top)
        for (int row = 0; row < StrategyConstants.BOARD_SIZE; row++)
        {
            System.out.print((ConvertRowIndex(row)) + "   ");
            if (row >= 10) System.out.print(" ");

            for (int col = 0; col < StrategyConstants.BOARD_SIZE; col++)
            {
                System.out.print(m_board.get(row).get(col) + " ");
            }

            System.out.print("\n");
        }

        System.out.print("\n");
    }

    /**
     Checks if a row and column pair are valid within board constraints.
     @param a_column An integer, representing the column to be checked.
     @param a_row An integer, representing the row to be checked.
     @return A boolean, whether or not the row and column pair provided are valid within board constraints.
     */
    public boolean IsValidIndices(int a_row, int a_column)
    {
        return (a_row >= 0 && a_row <= 18) && (a_column >= 0 && a_column <= 18);
    }

    /**
     Checks if a provided location is empty on the board.
     @param a_column An integer, representing the column of the location to be checked.
     @param a_row An integer, representing the row of the location to be checked.
     @return A boolean, whether or not the location defined by the row and column is empty on the board.
     */
    public boolean IsEmptyLocation(char a_column, int a_row)
    {
        return m_board.get(ConvertRowIndex(a_row)).get(CharacterToInt(a_column)) == '-';
    }

    /**
     Counts the number of stones placed on the board of a provided color.
     @param a_color A character, representing the color of stones to be counted.
     @return An integer, representing the total number of stones of that color on the board.
     */
    public int CountStones(char a_color)
    {
        int total = 0;

        //Loop through the entire board and count up the number of stones that are the color passed.
        for (int row = 0; row < StrategyConstants.BOARD_SIZE; row++)
        {
            for (int col = 0; col < StrategyConstants.BOARD_SIZE; col++)
            {
                if (m_board.get(row).get(col) == a_color)
                {
                    total++;
                }
            }
        }

        return total;
    }

    /**
     Determines if the entire board is empty.
     @return A boolean, whether or not the board is completely empty.
     */
    public boolean IsEmptyBoard()
    {
        return CountStones('W') == 0 && CountStones('B') == 0;
    }

    /**
     To clear captures off of the board, if any occur.
     @param a_column A character, representing the column of the location a stone was just placed.
     @param a_row An integer, representing the row of the location a stone was just placed.
     @param a_color A character, representing the color of the stone just placed.
     @return An integer, representing the total number of captures that were removed off the board.
     */
    public int ClearCaptures(char a_column, int a_row, char a_color)
    {
        //Represents the total number of pairs captured after placing a stone at this current location.
        int numCaptures = 0;

        //Represents the color of the opponent's stone.
        char opponentColor = OpponentColor(a_color);

        //Represents the numerical representation of the alphabetical column and correctly converted index of the row.
        int convertedColumn = CharacterToInt(a_column);
        int convertedRow = ConvertRowIndex(a_row);

        //Must loop through all 8 of the possible directions starting from the location passed since captures can happen in any direction.
        for (int direction = 0; direction < StrategyConstants.NUM_DIRECTIONS; direction++)
        {
            //For each of the 8 directions, you must go three spaces out to check if a capture has occurred.
            //For example, if the color passed was white, a capture follows the pattern * B B W where * is the location passed to this function.
            Vector<Vector<Integer>> newLocations = new Vector<Vector<Integer>>();

            for (int distance = 1; distance <= StrategyConstants.CAPTURE_DISTANCE; distance++)
            {
                int newRow = convertedRow + (StrategyConstants.DIRECTIONS.get(direction).get(0) * distance);
                int newCol = convertedColumn + (StrategyConstants.DIRECTIONS.get(direction).get(1) * distance);

                //If the location is valid, it must be stored so the stones there can be removed if it turns out to be a successful capture.
                if (IsValidIndices(newRow, newCol)) newLocations.add(new Vector<Integer>(Arrays.asList(newRow, newCol)));
            }

            //There must be at least 3 valid board spaces going in the current direction being evaluated for a capture to be possible.
            if (newLocations.size() == StrategyConstants.CAPTURE_DISTANCE)
            {
                //If the stones found are in the pattern O O P, where O is the opponent's stone and P is the players stone, a capture can be made.
                if (m_board.get(newLocations.get(0).get(0)).get(newLocations.get(0).get(1)) == opponentColor &&
                        m_board.get(newLocations.get(1).get(0)).get(newLocations.get(1).get(1)) == opponentColor &&
                        m_board.get(newLocations.get(2).get(0)).get(newLocations.get(2).get(1)) == a_color)
                {
                    numCaptures++;

                    //Remove the two captured stones from the board. The first and second row/col pairs of 'newLocations' are the two stones being captured.
                    //The column and row need to be converted to their board view representations when passed to the RemoveStone function.
                    RemoveStone(IntToCharacter(newLocations.get(0).get(1)), ConvertRowIndex(newLocations.get(0).get(0)));
                    RemoveStone(IntToCharacter(newLocations.get(1).get(1)), ConvertRowIndex(newLocations.get(1).get(0)));
                }
            }
        }

        return numCaptures;
    }

    /**
     Determines the opponent's stone color.
     @param a_color A character, representing the current player's stone color.
     @return A character, representing the stone color of the opponent.
     */
    public char OpponentColor(char a_color) {
        if (a_color == 'W')
        {
            return 'B';
        }
        else
        {
            return 'W';
        }
    }

    /**
     Determines if the board is completely full.
     @return A boolean, whether or not the board is completely full and there are no more empty locations.
     */
    public boolean IsBoardFull()
    {
        //Loop through every stone on the board, and if one is empty the board is not full. Otherwise, it is.
        for (int row = 0; row < StrategyConstants.BOARD_SIZE; row++)
        {
            for (int col = 0; col < StrategyConstants.BOARD_SIZE; col++)
            {
                if (m_board.get(row).get(col) == '-') return false;
            }
        }

        return true;
    }

    /**
     Determines if five consecutive stones has been achieved on the board for a provided stone color.
     @param a_color A character, representing the color of stone that is being checked.
     @return A boolean, whether or not five consecutive stones has been achieved on the board for the provided stone color.
     */
    public boolean FiveConsecutive(char a_color)
    {
        //From every position on the board, every horizontal, vertical, and diagonal needs to be searched.
        for (int row = 0; row < StrategyConstants.BOARD_SIZE; row++)
        {
            for (int col = 0; col < StrategyConstants.BOARD_SIZE; col++)
            {
                //Only have to loop through main directions, since searching opposite directions (left&right, up&down, etc. is redundant)
                //All horizontals are checked with just the left direction, all verticals with just the up direction, and so on.
                for (int direction = 0; direction < StrategyConstants.NUM_DIRECTIONS; direction += StrategyConstants.DIRECTIONAL_OFFSET)
                {
                    int consecutiveCounter = 0;

                    //For each direction, consider the current space and 4 spaces out (to make a consecutive 5).
                    for (int distance = 0; distance < StrategyConstants.CONSECUTIVE_5_DISTANCE; distance++)
                    {
                        int newRow = row + (StrategyConstants.DIRECTIONS.get(direction).get(0) * distance);
                        int newCol = col + (StrategyConstants.DIRECTIONS.get(direction).get(1) * distance);

                        //No need to keep searching if the 5 spaces go out of the board's bounds.
                        if (!IsValidIndices(newRow, newCol)) break;

                        if (m_board.get(newRow).get(newCol) == a_color) consecutiveCounter++;
                    }

                    if (consecutiveCounter == 5) return true;
                }
            }
        }

        return false;
    }

    /**
     Scores the board given a player's stone color and number of captured pairs they have.
     @param a_color A character, representing the color of the player that is being scored.
     @param a_numCaptures An integer representing the number of captured pairs that player has.
     @return An integer, representing the score earned by that player.
     */
    public int ScoreBoard(char a_color, int a_numCaptures)
    {
        //Holds the overall score for a provided color (a_color).
        int totalScore = 0;

        for (int direction = 0; direction < StrategyConstants.NUM_DIRECTIONS; direction += StrategyConstants.DIRECTIONAL_OFFSET)
        {
            //The first step in scoring is the find all 5 or more consecutives in each horizontal, vertical, and diagonal.
            //NOTE: There can be multiple 5 or more consecutives in an L shape if the last stone placed connects the L together.
            Vector<Vector<Character>> boardCopy = GetBoard();

            //Loop through every stone and search the current direction for any consecutive 5 or more stones. This is considered a "winning move".
            for (int row = 0; row < StrategyConstants.BOARD_SIZE; row++)
            {
                for (int col = 0; col < StrategyConstants.BOARD_SIZE; col++)
                {
                    int totalConsecutive = 0;
                    int newRow = row;
                    int newCol = col;

                    //If there is a winning move found in the current direction, it will need to be saved to be marked later.
                    Vector<Vector<Integer>> seenLocations = new Vector<Vector<Integer>>();

                    while (IsValidIndices(newRow, newCol) && boardCopy.get(newRow).get(newCol) == a_color)
                    {
                        seenLocations.add(new Vector<Integer>(Arrays.asList(newRow, newCol)));

                        totalConsecutive++;

                        newRow += StrategyConstants.DIRECTIONS.get(direction).get(0);
                        newCol += StrategyConstants.DIRECTIONS.get(direction).get(1);
                    }

                    //If a winning move was found, the player is awarded 5 points.
                    if (totalConsecutive >= 5)
                    {
                        totalScore += 5;

                        //Mark the winning move in this direction as "S" to represent seen. This ensures that the winning move does not also get counted as a consecutive 4.
                        for (int i = 0; i < seenLocations.size(); i++)
                        {
                            boardCopy.get(seenLocations.get(i).get(0)).set(seenLocations.get(i).get(1), 'S');
                        }
                    }

                }
            }

            //After the "winning move" in the current direction has been found and marked as seen, search for any consecutive 4s in that same direction.
            for (int row = 0; row < StrategyConstants.BOARD_SIZE; row++)
            {
                for (int col = 0; col < StrategyConstants.BOARD_SIZE; col++)
                {
                    int totalConsecutive = 0;

                    //Search 3 more spaces out from the current stone.
                    for (int distance = 0; distance < StrategyConstants.CONSECUTIVE_4_DISTANCE; distance++)
                    {
                        int newRow = row + StrategyConstants.DIRECTIONS.get(direction).get(0) * distance;
                        int newCol = col + StrategyConstants.DIRECTIONS.get(direction).get(1) * distance;

                        if (!IsValidIndices(newRow, newCol)) break;

                        if (boardCopy.get(newRow).get(newCol) == a_color) totalConsecutive++;
                    }

                    //If there was a consecutive 4 found, the player is awarded 1 point.
                    if (totalConsecutive == 4) totalScore++;
                }
            }
        }

        //Lastly, add 1 point for each captured pair.
        totalScore += a_numCaptures;

        return totalScore;
    }

    /**
     Clears the board, making it completely empty.
     */
    public void ClearBoard()
    {
        //Clear the board.
        m_board.clear();

        //Set each value to '-' to represent empty spaces.
        for (int i = 0; i < StrategyConstants.BOARD_SIZE; i++)
        {
            Vector<Character> row = new Vector<>();
            for (int j = 0; j < StrategyConstants.BOARD_SIZE; j++)
            {
                row.add('-');
            }
            m_board.add(row);
        }
    }

    /**
     Converts an alphabetical column into its numerical counterpart.
     @param a_column A character representing the column to be converted.
     @return An integer, representing the converted column.
     */
    public int CharacterToInt(char a_column)
    {
        return (int) (a_column - 'A');
    }

    /**
     Converts a numerical column into its alphabetical counterpart.
     @param a_column An integer representing the column to be converted.
     @return A character, representing the converted column.
     */
    public char IntToCharacter(int a_column)
    {
        return (char) (a_column + 'A');
    }

    /**
     Converts a row from its vector index to board view index or vice versa.
     @param a_row An integer, representing the row to be converted.
     @return An integer, representing the row's converted index.
     */
    public int ConvertRowIndex(int a_row)
    {
        return 19 - a_row;
    }
}