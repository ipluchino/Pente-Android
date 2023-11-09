package edu.ramapo.ipluchino.pente.Model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;
import java.util.Random;

public class Player implements Serializable {
    //The color of the stones (black or white) that the player is playing as.
    protected char m_color;

    //The overall score for the player.
    protected int m_score;

    //The number of captured pairs for the player.
    protected int m_capturedPairs;

    /**
     Default constructor for the Player class.
     */
    public Player()
    {
        m_color = 'W';
        m_score = 0;
        m_capturedPairs = 0;
    }

    /**
     Gets the stone color of the player.
     @return a character, representing the stone color of the player.
     */
    public char GetColor() {
        return m_color;
    }

    /**
     Gets the tournament score of the player.
     @return An integer, representing the tournament score of the player.
     */
    public int GetScore() {
        return m_score;
    }

    /**
     Gets the captured pair count of the player.
     @return An integer, representing the player's captured pair count.
     */
    public int GetCapturedPairs() {
        return m_capturedPairs;
    }

    /**
     Sets the stone color of the player.
     @param a_color A character, representing the stone color to set the player's stone color to.
     @return A boolean, whether or not the tournament score was successfully set.
     */
    public boolean SetColor(char a_color)
    {
        if (a_color != 'W' && a_color != 'B') return false;

        m_color = a_color;

        return true;
    }

    /**
     Sets the tournament score of the player.
     @param a_score An integer, representing the tournament score to set the player's tournament score to.
     @return A boolean, whether or not the tournament score was successfully set.
     */
    public boolean SetScore(int a_score)
    {
        if (a_score < 0) return false;

        m_score = a_score;

        return true;
    }

    /**
     Sets the captured pair count of the player.
     @param a_capturedPairs An integer representing the captured pair count to set the player's captured pair count to.
     @return A boolean, whether or not the captured pair count was successfully set.
     */
    public boolean SetCapturedPairs(int a_capturedPairs)
    {
        if (a_capturedPairs < 0) return false;

        m_capturedPairs = a_capturedPairs;

        return true;
    }

    /**
     Virtual function that is redefined in the Human and Computer class. This function does nothing.
     @param a_board A Board object, representing the current board of the round.
     @param a_location A string, representing the location the player would like to place their stone.
     @return A string, representing the description of the player's move. This specific function only returns an empty string.
     */
    public String MakePlay(Board a_board, String a_location)
    {
        return "";
    }

    /**
     Converts a Vector index row/col to a board view representation in the format: "J10".
     @param a_row An integer, representing the vector index of a row.
     @param a_col An integer, representing the vector index of a column.
     @param a_board A Board object, representing the current board of the round.
     @return What the function returns - don't include if void. Also list special cases, such as what is returned if error.
     */
    public String ExtractLocation(int a_row, int a_col, Board a_board)
    {
        int boardRow = a_board.ConvertRowIndex(a_row);
        char boardCol = a_board.IntToCharacter(a_col);

        return boardCol + Integer.toString(boardRow);
    }

    /**
     Converts a directional index into its string representation.
     @param a_directionIndex An integer, representing the directional index to be converted.
     @return A string, representing the name of the direction.
     */
    public String GetDirection(int a_directionIndex)
    {
        return StrategyConstants.DIRECTION_NAMES.get(a_directionIndex);
    }

    //https://www.educative.io/answers/how-to-generate-random-numbers-in-java
    /**
     To determine the most optimal location for a player to place their stone.
     @param a_board A Board object, representing the current board of the round.
     @param a_color A character, representing the player's stone color.
     @return A Vector of strings containing the location of the most optimal play, as well as the reasoning on why this is the most optimal play.
     */
    public Vector<String> OptimalPlay(Board a_board, char a_color)
    {
        //Location represents the location on the board of the most optimal play, while reasoning represents the explanation why it is the most optimal.
        String location = "";
        String reasoning = "The computer placed a stone on ";
        Vector<Integer> possiblePlay;

        //If the board is empty, the only play is the center position, J10.
        if (a_board.IsEmptyBoard())
        {
            location = "J10";
            reasoning += location + " because the first stone must be placed there.";

            return new Vector<String>(Arrays.asList(location, reasoning));
        }

        //There is a handicap for the second turn of the first player. The play must be within three intersections of the center stone.
        boolean handicap = a_board.CountPieces('W') == 1 && a_board.CountPieces('B') == 1;
        if (handicap)
        {
            possiblePlay = FindHandicapPlay(a_board);
            location = ExtractLocation(possiblePlay.get(0), possiblePlay.get(1), a_board);
            reasoning += location + " to counter the opponent's initiative and start building initiative. The location must be at least three intersections away from the center (J10).";
            return new Vector<String>(Arrays.asList(location, reasoning));
        }

        //Attempt to win the game, if possible.
        StringBuilder winReason = new StringBuilder();
        possiblePlay = MakeWinningMove(a_board, a_color, winReason);
        if (!possiblePlay.isEmpty())
        {
            location = ExtractLocation(possiblePlay.get(0), possiblePlay.get(1), a_board);
            reasoning += location + winReason;
            return new Vector<String>(Arrays.asList(location, reasoning));
        }

        //Prevent the opponent from winning the game, if possible.
        possiblePlay = PreventWinningMove(a_board, a_color);
        if (!possiblePlay.isEmpty())
        {
            location = ExtractLocation(possiblePlay.get(0), possiblePlay.get(1), a_board);
            reasoning += location + " to block the opponent from getting five consecutive stones in the " + GetDirection(possiblePlay.get(2)) + " direction.";
            return new Vector<String>(Arrays.asList(location, reasoning));
        }

        //Attempt to build a deadly tessera, aka four consecutive stones with an empty location on either side.
        possiblePlay = FindDeadlyTessera(a_board, a_color, a_color);
        if (!possiblePlay.isEmpty())
        {
            location = ExtractLocation(possiblePlay.get(0), possiblePlay.get(1), a_board);
            reasoning += location + " to build a deadly tessera in the " + GetDirection(possiblePlay.get(2)) + " direction.";

            return new Vector<String>(Arrays.asList(location, reasoning));
        }

        //Attempt to block a deadly tessera from forming.
        possiblePlay = FindDeadlyTessera(a_board, a_board.OpponentColor(a_color), a_color);
        if (!possiblePlay.isEmpty())
        {
            location = ExtractLocation(possiblePlay.get(0), possiblePlay.get(1), a_board);
            reasoning += location + " to block deadly tessera from forming in the " + GetDirection(possiblePlay.get(2)) + " direction.";

            return new Vector<String>(Arrays.asList(location, reasoning));
        }

        //Attempt to make the most possible captures, if possible.
        possiblePlay = MakeCapture(a_board, a_color);
        if (!possiblePlay.isEmpty())
        {
            location = ExtractLocation(possiblePlay.get(0), possiblePlay.get(1), a_board);
            reasoning += location + " to capture the opponent's stones.";
            return new Vector<String>(Arrays.asList(location, reasoning));
        }

        //Attempt to block the most possible captures, if possible.
        possiblePlay = PreventCapture(a_board, a_color);
        if (!possiblePlay.isEmpty())
        {
            location = ExtractLocation(possiblePlay.get(0), possiblePlay.get(1), a_board);
            reasoning += location + " to prevent the opponent from making a capture.";
            return new Vector<String>(Arrays.asList(location, reasoning));
        }

        //Attempt to build initiative with 3 stones already placed.
        possiblePlay = BuildInitiative(a_board, 3, a_color, a_color);
        if (!possiblePlay.isEmpty())
        {
            location = ExtractLocation(possiblePlay.get(0), possiblePlay.get(1), a_board);
            reasoning += location + " to build initiative and have 4 stones in an open 5 consecutive locations in the " + GetDirection(possiblePlay.get(2)) + " direction.";

            return new Vector<String>(Arrays.asList(location, reasoning));
        }

        //Attempt to counter initiative if the opponent has 3 stones already placed.
        possiblePlay = CounterInitiative(a_board, 3, a_color);
        if (!possiblePlay.isEmpty())
        {
            location = ExtractLocation(possiblePlay.get(0), possiblePlay.get(1), a_board);
            reasoning += location + " to block the opponent from getting 4 stones in an open 5 consecutive locations in the " + GetDirection(possiblePlay.get(2)) + " direction.";
            return new Vector<String>(Arrays.asList(location, reasoning));
        }

        //Attempt to build initiative with 2 stones already placed.
        possiblePlay = BuildInitiative(a_board, 2, a_color, a_color);
        if (!possiblePlay.isEmpty())
        {
            location = ExtractLocation(possiblePlay.get(0), possiblePlay.get(1), a_board);
            reasoning += location + " to build initiative and have 3 stones in an open 5 consecutive locations in the " + GetDirection(possiblePlay.get(2)) + " direction.";
            return new Vector<String>(Arrays.asList(location, reasoning));
        }

        //Attempt to initiate a flank.
        possiblePlay = CounterInitiative(a_board, 2, a_color);
        if (!possiblePlay.isEmpty())
        {
            location = ExtractLocation(possiblePlay.get(0), possiblePlay.get(1), a_board);
            reasoning += location + " to counter initiative and initiate a flank in the " + GetDirection(possiblePlay.get(2)) + " direction.";
            return new Vector<String>(Arrays.asList(location, reasoning));
        }

        //Attempt to build initiative with 1 stone already placed.
        possiblePlay = BuildInitiative(a_board, 1, a_color, a_color);
        if (!possiblePlay.isEmpty())
        {
            location = ExtractLocation(possiblePlay.get(0), possiblePlay.get(1), a_board);
            reasoning += location + " to build initiative and have 2 stones in an open 5 consecutive locations in the " + GetDirection(possiblePlay.get(2)) + " direction.";
            return new Vector<String>(Arrays.asList(location, reasoning));
        }

        //Attempt to counter initiative with stone piece already placed - used for the first move when going second.
        possiblePlay = CounterInitiative(a_board, 1, a_color);
        if (!possiblePlay.isEmpty())
        {
            location = ExtractLocation(possiblePlay.get(0), possiblePlay.get(1), a_board);
            reasoning += location + " to counter the opponent's initiative and start building initiative.";
            return new Vector<String>(Arrays.asList(location, reasoning));
        }

        //If no other strategies can be used, find an open spot on the board and place it there. This is a failsafe for the edge case where the board is near full.
        do {
            Random rand = new Random();
            int row = rand.nextInt(19);
            char col = (char) ('A' + rand.nextInt(19));

            location = col + Integer.toString(row);

        } while (!a_board.IsEmptyLocation(location.charAt(0), Integer.parseInt(location.substring(1))));

        reasoning += location + " because there is no other optimal move.";
        return new Vector<String>(Arrays.asList(location, reasoning));
    }

    //https://stackoverflow.com/questions/33088677/sort-list-of-objects-using-collection-sort-with-lambdas-only
    /**
     Finds a location on the board that results in the most captures possible, if any exist.
     @param a_board A Board object, representing the current board of the round.
     @param a_color A character, representing the player's stone color.
     @return A Vector of integers containing the row and column of the best possible location that results in the most captures.
     */
    public Vector<Integer> MakeCapture(Board a_board, char a_color)
    {
        Vector<Vector<Character>> board = a_board.GetBoard();

        Vector<Vector<Integer>> allPossibleCaptures = new Vector<Vector<Integer>>();

        //Search the entire board for empty spaces.
        for (int row = 0; row < StrategyConstants.BOARD_SIZE; row++)
        {
            for (int col = 0; col < StrategyConstants.BOARD_SIZE; col++)
            {
                if (board.get(row).get(col) == '-')
                {
                    //If the space is empty, determine the number of captures that would occur if you were to place your piece here.
                    int numCaptures = CanCaptureIfPlaced(a_board, a_color, row, col);
                    if (numCaptures > 0)
                    {
                        //If you can make a capture at this location, log it in the form {row, col, number of captures}
                        Vector<Integer> captureInfo = new Vector<Integer>(Arrays.asList(row, col, numCaptures));

                        allPossibleCaptures.add(captureInfo);
                    }
                }
            }
        }

        //If there are no possible captures, simply return an empty vector.
        if (allPossibleCaptures.isEmpty())
        {
            return new Vector<Integer>();
        }

        //If there are multiple possible capture locations, the one that captures the most pieces should be prioritized.
        //To accomplish this, a lambda function is used to sort by capture number for each possible play.
        allPossibleCaptures.sort((a, b) -> (b.get(2)).compareTo(a.get(2)));

        //Return the play that captures the most possible pieces in one play. Since the vector is sorted by capture number,
        //the first play is considered the most optimized.
        return allPossibleCaptures.get(0);

    }

    /**
     Determines the number of captures that would occur if a player places their stone at a specified location.
     @param a_board A Board object, representing the current board of the round.
     @param a_color A character, representing the player's stone color.
     @param a_row An integer, representing a row a player is placing their stone.
     @param a_col An integer, representing a column a player is placing their stone.
     @return An integer, representing the number of captured pairs that would occur if a player plays their stone at the specified location.
     */
    public int CanCaptureIfPlaced(Board a_board, char a_color, int a_row, int a_col)
    {
        Vector<Vector<Character>> board = a_board.GetBoard();

        char opponentColor = a_board.OpponentColor(a_color);
        int numCaptures = 0;

        //Loop through all 8 of the possible directions starting from
        for (int direction = 0; direction < StrategyConstants.NUM_DIRECTIONS; direction++)
        {
            Vector<Vector<Integer>> newLocations = new Vector<Vector<Integer>>();

            //To see if a capture exists, you must go three spaces out in each direction and store them.
            for (int distance = 1; distance <= StrategyConstants.CAPTURE_DISTANCE; distance++)
            {
                int newRow = a_row + (StrategyConstants.DIRECTIONS.get(direction).get(0) * distance);
                int newCol = a_col + (StrategyConstants.DIRECTIONS.get(direction).get(1) * distance);

                if (a_board.IsValidIndices(newRow, newCol)) newLocations.add(new Vector<Integer>(Arrays.asList(newRow, newCol)));
            }

            if (newLocations.size() == StrategyConstants.CAPTURE_DISTANCE)
            {
                //A capture is possible if the three valid locations are in the pattern: * O O P where * represents an empty space,
                //O is the opponent's pieces, and P is the player's piece.
                if (board.get(newLocations.get(0).get(0)).get(newLocations.get(0).get(1)) == opponentColor &&
                        board.get(newLocations.get(1).get(0)).get(newLocations.get(1).get(1)) == opponentColor &&
                        board.get(newLocations.get(2).get(0)).get(newLocations.get(2).get(1)) == a_color)
                {
                    numCaptures++;
                }
            }
        }

        return numCaptures;
    }

    /**
     Finds a location on the board that prevents the opponent from making a capture on their following turn.
     @param a_board A Board object, representing the current board of the round.
     @param a_color A character, representing the player's stone color.
     @return A Vector of integers containing the row and column of the location that blocks the opponent from making a capture on their next turn.
     */
    public Vector<Integer> PreventCapture(Board a_board, char a_color)
    {
        char opponentColor = a_board.OpponentColor(a_color);

        //If the opponent has the ability to make a capture, or multiple, on their next turn, it should be blocked.
        Vector<Integer> possiblePreventCapture = MakeCapture(a_board, opponentColor);

        return possiblePreventCapture;
    }

    /**
     Finds all possible sets of locations given constraints regarding the stone color, stone count, and a number of consecutive locations to search for.
     @param a_board A Board object, representing the current board of the round.
     @param a_numPlaced An integer, representing the number of a player's stone to search for.
     @param a_color A character, representing the player's stone color to search for.
     @param a_distance An integer, representing the number of consecutive locations to be included in the search.
     @return A 3-D Vector of integers, representing sets of locations that match the provided search parameters.
     */
    Vector<Vector<Vector<Integer>>> FindAllMoves(Board a_board, int a_numPlaced, char a_color, int a_distance)
    {
        //A copy of the board's data.
        Vector<Vector<Character>> board = a_board.GetBoard();

        //Will hold all possible sets of consecutive a_distance locations that has a_numPlaced pieces of the specified color, and a_distance - a_numPlaced empty locations.
        Vector<Vector<Vector<Integer>>> result = new Vector<Vector<Vector<Integer>>>();

        //The empty locations required to be valid is a_distance - a_numPlaced.
        int emptyRequired = a_distance - a_numPlaced;

        for (int row = 0; row < StrategyConstants.BOARD_SIZE; row++)
        {
            for (int col = 0; col < StrategyConstants.BOARD_SIZE; col++)
            {
                //Only need to search the main directions: horizontals, verticals, main diagonals, and anti-diagonals.
                for (int direction = 0; direction < StrategyConstants.NUM_DIRECTIONS; direction += StrategyConstants.DIRECTIONAL_OFFSET)
                {
                    int pieceCounter = 0;
                    int emptyCounter = 0;

                    Vector<Vector<Integer>> locations = new Vector<Vector<Integer>>();

                    for (int distance = 0; distance < a_distance; distance++)
                    {
                        int newRow = row + StrategyConstants.DIRECTIONS.get(direction).get(0) * distance;
                        int newCol = col + StrategyConstants.DIRECTIONS.get(direction).get(1) * distance;

                        //No need to keep searching the current set of 5 if one of the locations go out of bounds.
                        if (!a_board.IsValidIndices(newRow, newCol)) break;

                        if (board.get(newRow).get(newCol) == a_color)
                        {
                            pieceCounter++;
                        }
                        else if (board.get(newRow).get(newCol) == '-')
                        {
                            emptyCounter++;
                        }

                        locations.add(new Vector<Integer>(Arrays.asList(newRow, newCol, direction)));
                    }

                    //If the number of pieces and empty locations satisfy their conditions, record this set of locations to the result vector.
                    if (pieceCounter == a_numPlaced && emptyCounter == emptyRequired)
                    {
                        result.add(locations);
                    }
                }
            }
        }

        return result;
    }

    /**
     Finds all of the indices that are empty, given a set of locations.
     @param a_board A Board object, representing the current board of the round.
     @param a_locations A 2-D Vector of integers, representing a set of locations on the board.
     @return A vector of integers containing the indices of a_locations that are empty locations on the board.
     */
    public Vector<Integer> FindEmptyIndices(Board a_board, Vector<Vector<Integer>> a_locations)
    {
        Vector<Vector<Character>> board = a_board.GetBoard();
        Vector<Integer> emptyIndices = new Vector<Integer>();

        //Search through the set of locations and mark the empty indices.
        for (int i = 0; i < a_locations.size(); i++)
        {
            int row = a_locations.get(i).get(0);
            int col = a_locations.get(i).get(1);

            if (board.get(row).get(col) == '-')
            {
                emptyIndices.add(i);
            }
        }

        return emptyIndices;
    }

    /**
     Finds the number of consecutive pieces that would be placed on the board given a set of locations and an index to place the stone.
     @param a_board A Board object, representing the current board of the round.
     @param a_locations A 2-D Vector of integers, representing a set of locations on the board.
     @param a_emptyIndex An integer, representing the index in a_locations that is empty on the board.
     @return An integer, representing the number of consecutive stones that will occur if a stone is placed at a_emptyIndex in a_locations.
     */
    public int FindConsecutiveIfPlaced(Board a_board, Vector<Vector<Integer>> a_locations, int a_emptyIndex)
    {
        Vector<Vector<Character>> board = a_board.GetBoard();

        int beforeTotal = 0;
        int afterTotal = 0;

        //Find the number of consecutive pieces before emptyIndex in a_locations.
        int index = a_emptyIndex;
        while (--index >= 0)
        {
            int row = a_locations.get(index).get(0);
            int col = a_locations.get(index).get(1);

            if (board.get(row).get(col) != '-')
            {
                beforeTotal++;
            }
            else {
                break;
            }
        }

        //Find the number of consecutive pieces after emptyIndex in a_locations.
        index = a_emptyIndex;
        while (++index < a_locations.size())
        {
            int row = a_locations.get(index).get(0);
            int col = a_locations.get(index).get(1);

            if (board.get(row).get(col) != '-')
            {
                afterTotal++;
            }
            else {
                break;
            }
        }

        //The number of consecutive pieces if placed in the emptyIndex is the number of consecutive pieces before it, plus the number of pieces after it, plus itself.
        return beforeTotal + afterTotal + 1;
    }

    //https://www.geeksforgeeks.org/java-program-to-shuffle-vector-elements/#
    /**
     Finds a location on the board that builds initiative for the player.
     @param a_board A Board object, representing the current board of the round.
     @param a_numPlaced An integer, representing the amount of stones placed by the opponent in an open five consecutive locations.
     @param a_color A character, representing the player's stone color.
     @param a_dangerColor A character, representing the stone color to check if placing it at a location would put it at risk of being captured.
     @return A Vector of integers containing the row, column, and direction being built in of the best location to build initiative, if any exist.
     */
    public Vector<Integer> BuildInitiative(Board a_board, int a_numPlaced, char a_color, char a_dangerColor)
    {
        Vector<Vector<Character>> board = a_board.GetBoard();
        Vector<Vector<Vector<Integer>>> possibleMoves = FindAllMoves(a_board, a_numPlaced, a_color, StrategyConstants.CONSECUTIVE_5_DISTANCE);

        //If there are no possible moves that satisfy the conditions passed, simply return.
        if (possibleMoves.isEmpty()) return new Vector<Integer>();;

        if (a_numPlaced == 1)
        {
            Vector<Vector<Integer>> playLocations = new Vector<Vector<Integer>>();

            for (int i = 0; i < possibleMoves.size(); i++)
            {
                //There are 5 locations within each list of possible moves.
                //The indices 0 and 4 represents the ends of the set of 5 - Ex: * - - - *
                int firstRow = possibleMoves.get(i).get(0).get(0);
                int firstCol = possibleMoves.get(i).get(0).get(1);
                int lastRow = possibleMoves.get(i).get(4).get(0);
                int lastCol = possibleMoves.get(i).get(4).get(1);

                //Find the possible set of 5 where the piece found one of the ends to make it easier to find the middle. Ex: W - - - - OR - - - - W
                if ((board.get(firstRow).get(firstCol) != '-' || board.get(lastRow).get(lastCol) != '-') && !InDangerOfCapture(a_board, possibleMoves.get(i).get(2), a_dangerColor))
                {
                    //possibleMoves[i][2] represents the middle of the set of 5 locations. Ex: W - * - - where * represents the middle.
                    //This ensures the new pieces is placed one away from the placed piece and not at risk of capture.
                    playLocations.add(possibleMoves.get(i).get(2));
                }
            }

            //Shuffle the possible locations (there will often be many) so that the computer builds in different directions, not only the first direction it finds.
            if (!playLocations.isEmpty())
            {
                Collections.shuffle(playLocations);
                return playLocations.get(0);
            }
            else
            {
                return new Vector<Integer>();
            }
        }
        else if (a_numPlaced == 2)
        {
            //Search for possible 3 in a rows. If there is one, that is the most optimal play.
            Vector<Integer> possibleThreeConsecutive = FindThreeConsecutive(a_board, possibleMoves, a_dangerColor);

            if (!possibleThreeConsecutive.isEmpty())
            {
                return possibleThreeConsecutive;
            }

            //If you can't make a 3 in a row, prefer to place the piece with the least neighbors to avoid being captured. Ex: W - * - W
            int leastConsecutive = Integer.MAX_VALUE;
            int leastIndex = Integer.MAX_VALUE;
            int locationIndex = -1;
            for (int i = 0; i < possibleMoves.size(); i++) {
                Vector<Integer> emptyIndices = FindEmptyIndices(a_board, possibleMoves.get(i));

                for (int j = 0; j < emptyIndices.size(); j++) {
                    int possibleConsecutive = FindConsecutiveIfPlaced(a_board, possibleMoves.get(i), emptyIndices.get(j));
                    if (possibleConsecutive < leastConsecutive && !InDangerOfCapture(a_board, possibleMoves.get(i).get(emptyIndices.get(j)), a_dangerColor)) {
                        leastConsecutive = possibleConsecutive;
                        leastIndex = emptyIndices.get(j);
                        locationIndex = i;
                    }
                }
            }

            if (locationIndex != -1)
            {
                return possibleMoves.get(locationIndex).get(leastIndex);
            }
            else
            {
                return new Vector<Integer>();
            }
        }
        else if (a_numPlaced == 3)
        {
            //Search for possible 4 in a rows. If there is one, that is the most optimal play.
            Vector<Integer> possibleFourConsecutive = FindFourConsecutive(a_board, possibleMoves, a_dangerColor);
            if (!possibleFourConsecutive.isEmpty())
            {
                return possibleFourConsecutive;
            }

            //Search for possible 3 in a rows. If there is one, that is the most optimal play.
            Vector<Integer> possibleThreeConsecutive = FindThreeConsecutive(a_board, possibleMoves, a_dangerColor);
            if (!possibleThreeConsecutive.isEmpty())
            {
                return possibleThreeConsecutive;
            }

            return new Vector<Integer>();
        }
        else
        {
            return new Vector<Integer>();
        }
    }

    /**
     Finds a location on the board that counters the initiative of the opponent.
     @param a_board A Board object, representing the current board of the round.
     @param a_numPlaced An integer, representing the amount of stones placed by the opponent in an open five consecutive locations.
     @param a_color A character, representing the player's stone color.
     @return A Vector of integers containing the row, column, and direction being blocked of the best location to counter initiative, if any exist.
     */
    public Vector<Integer> CounterInitiative(Board a_board, int a_numPlaced, char a_color)
    {
        char opponentColor = a_board.OpponentColor(a_color);

        if (a_numPlaced == 2)
        {
            //Finding any potential flanks.
            Vector<Integer> possibleFlank = FindFlanks(a_board, a_color);

            if (!possibleFlank.isEmpty())
            {
                return possibleFlank;
            }

            return new Vector<Integer>();

        }
        else if (a_numPlaced == 1 || a_numPlaced == 3)
        {
            //When blocking, the most optimal placement to block is where your opponent wants to place their stone on their following turn.
            Vector<Integer> counterLocation = BuildInitiative(a_board, a_numPlaced, opponentColor, a_color);

            if (!counterLocation.isEmpty())
            {
                return counterLocation;
            }

            return new Vector<Integer>();
        }

        return new Vector<Integer>();
    }

    //https://docs.oracle.com/javase/8/docs/api/java/lang/StringBuilder.html --> StringBuilder because you can't pass a string by reference in Java.
    /**
     Find a location on the board that would cause the player to win the round.
     @param a_board A Board object, representing the current board of the round.
     @param a_color A character, representing the player's stone color.
     @param a_winReason A StringBuilder object, representing the reasoning of the win, or if the win is being delayed.
     @return A Vector of integers containing the row and column of the location that would result in the player winning the game.
     */
    public Vector<Integer> MakeWinningMove(Board a_board, char a_color, StringBuilder a_winReason)
    {
        //First check if there are any moves that allow for five consecutive pieces.
        Vector<Vector<Vector<Integer>>> possibleMoves = FindAllMoves(a_board, 4, a_color, StrategyConstants.CONSECUTIVE_5_DISTANCE);

        if (possibleMoves.size() > 1)
        {
            //If there are multiple win moves, see if the win can be delayed to earn additional points.
            //The win will only be delayed if the player is not in danger of getting captured and giving more points to the opponent.
            //The player must also not be at risk of losing on the following turn.
            Vector<Integer> opponentCaptures = PreventCapture(a_board, a_color);
            Vector<Integer> opponentWin = PreventWinningMove(a_board, a_color);
            if (opponentCaptures.isEmpty() && opponentWin.isEmpty())
            {
                //Check if any captures can be made to score additional points.
                Vector<Integer> potentialCaptures = MakeCapture(a_board, a_color);

                if (!potentialCaptures.isEmpty())
                {
                    a_winReason.append(" to capture the opponent's stones. The win is being delayed to score additional points.");
                    return potentialCaptures;
                }
            }

        }

        if (!possibleMoves.isEmpty())
        {
            //If there's only one possible move that creates five consecutive pieces, make it and win the round.
            Vector<Integer> emptyIndices = FindEmptyIndices(a_board, possibleMoves.get(0));

            //Note: The vector containing the empty location is in the format {row, col, directionIndex},
            a_winReason.append(" to make five consecutive piece in the ").append(GetDirection(possibleMoves.get(0).get(emptyIndices.get(0)).get(2))).append(" direction and win the round.");
            return possibleMoves.get(0).get(emptyIndices.get(0));
        }

        //Next, check if you can make a capture to end the game.
        //Note: potentialCaptures is in the form {row, col, numCaptures}
        Vector<Integer> potentialCaptures = MakeCapture(a_board, a_color);

        if (!potentialCaptures.isEmpty())
        {
            int numCaptures = potentialCaptures.get(2);

            if (numCaptures + m_capturedPairs >= 5)
            {
                a_winReason.append(" to capture the opponent's pieces and have at least five captured pairs to win the round.");
                return potentialCaptures;
            }
        }

        return new Vector<Integer>();
    }

    /**
     Finds a location on the board that prevents the opponent from winning the round.
     @param a_board A Board object, representing the current board of the round.
     @param a_color A character, representing the player's stone color.
     @return A Vector of integers containing the row and column of the location that would result in the player preventing the opponent from winning the game.
     */
    public Vector<Integer> PreventWinningMove(Board a_board, char a_color)
    {
        char opponentColor = a_board.OpponentColor(a_color);

        //If the opponent has any winning moves they can do on their next turn, this is the location that you should block.
        Vector<Vector<Vector<Integer>>> possibleMoves = FindAllMoves(a_board, 4, opponentColor, StrategyConstants.CONSECUTIVE_5_DISTANCE);

        if (!possibleMoves.isEmpty())
        {
            //If there's a move that creates a 5 in a row for the opponent, you must block it.
            Vector<Integer> emptyIndices = FindEmptyIndices(a_board, possibleMoves.get(0));
            return possibleMoves.get(0).get(emptyIndices.get(0));
        }
        else
        {
            return new Vector<Integer>();
        }
    }

    /**
     Determines if placing a stone in a specified location would put the player in danger of being captured on the following turn.
     @param a_board A Board object, representing the current board of the round.
     @param a_location A Vector of integers, representing the row and column of the location being checked.
     @param a_color A character, representing the stone color of the player placing the stone at a_location.
     @return A boolean, whether or not the player is at risk of being captured the following turn.
     */
    public boolean InDangerOfCapture(Board a_board, Vector<Integer> a_location, char a_color)
    {
        Vector<Vector<Character>> board = a_board.GetBoard();

        int row = a_location.get(0);
        int col = a_location.get(1);

        char opponentColor = a_board.OpponentColor(a_color);

        //To determine if playing a piece will put the player at risk of being captured, locations will need to be checked in opposite directions.
        Vector<Integer> currentDirection;
        Vector<Integer> oppositeDirection;
        for (int direction = 0; direction < StrategyConstants.NUM_DIRECTIONS; direction++)
        {
            //Multiplying the row instruction and the column instruction by -1 results in the instructions for the opposite direction.
            currentDirection = StrategyConstants.DIRECTIONS.get(direction);
            oppositeDirection = new Vector<Integer>(Arrays.asList(StrategyConstants.DIRECTIONS.get(direction).get(0) * -1, StrategyConstants.DIRECTIONS.get(direction).get(1) * -1));

            //To be in danger of being captured there must be this pattern: - * P O OR this pattern: - P * O
            // - is an empty space, * is the current location being evaluated, P is the current players piece, and O is the opponents piece.

            //The row, col pair that is a distance of one away in the opposite direction being evaluated.
            int oppositeDirRow = row + oppositeDirection.get(0);
            int oppositeDirCol = col + oppositeDirection.get(1);

            //The row, col pair that is a distance of one away in the current direction being evaluated.
            int currentDirOneAwayRow = row + currentDirection.get(0);
            int currentDirOneAwayCol = col + currentDirection.get(1);

            //The row, col pair that is a distance of two away in the current direction being evaluated.
            int currentDirTwoAwayRow = row + currentDirection.get(0) * 2;
            int currentDirTwoAwayCol = col + currentDirection.get(1) * 2;

            if (a_board.IsValidIndices(oppositeDirRow, oppositeDirCol) && a_board.IsValidIndices(currentDirOneAwayRow, currentDirOneAwayCol) && a_board.IsValidIndices(currentDirTwoAwayRow, currentDirTwoAwayCol))
            {
                //Handling the pattern: - * P O
                if (board.get(oppositeDirRow).get(oppositeDirCol) == '-' && board.get(currentDirOneAwayRow).get(currentDirOneAwayCol) == a_color && board.get(currentDirTwoAwayRow).get(currentDirTwoAwayCol) == opponentColor)
                {
                    return true;
                }

                //Handling the pattern: - P * O
                if (board.get(oppositeDirRow).get(oppositeDirCol) == opponentColor && board.get(currentDirOneAwayRow).get(currentDirOneAwayCol) == a_color && board.get(currentDirTwoAwayRow).get(currentDirTwoAwayCol) == '-')
                {
                    return true;
                }
            }

        }

        return false;
    }

    /**
     Finds a location on the board that will initiate a flank.
     @param a_board A Board object, representing the current board of the round.
     @param a_color A character, representing the stone color to search for flanks.
     @return Return Value: A Vector of integers containing the row, column, and direction being built in that initiates a flank, if it exists.
     */
    public Vector<Integer> FindFlanks(Board a_board, char a_color)
    {
        char opponentColor = a_board.OpponentColor(a_color);

        //Search all possible sets of 4 locations that have two opponent's stones and 2 empty locations.
        Vector<Vector<Vector<Integer>>> possibleMoves = FindAllMoves(a_board, 2, opponentColor, 4);

        for (int i = 0; i < possibleMoves.size(); i++)
        {
            Vector<Integer> emptyIndices = FindEmptyIndices(a_board, possibleMoves.get(i));

            //If the empty indices are 0 and 3, they are on the ends of the consecutive 4 locations (* W W *), and it means that a flank can be initiated.
            if (emptyIndices.get(0) == 0 && emptyIndices.get(1) == 3)
            {
                if (!InDangerOfCapture(a_board, possibleMoves.get(i).get(0), a_color))
                {
                    return possibleMoves.get(i).get(emptyIndices.get(0));
                }
                else if (!InDangerOfCapture(a_board, possibleMoves.get(i).get(3), a_color))
                {
                    return possibleMoves.get(i).get(emptyIndices.get(1));
                }
            }
        }

        return new Vector<Integer>();
    }

    /**
     Finds a location on the board that would create a deadly tessera (four consecutive pieces with an empty location on either side).
     @param a_board A Board object, representing the current board of the round.
     @param a_color A character, representing the stone color to search for deadly tesseras.
     @param a_dangerColor A character, representing the stone color to check if it is in danger of being captured.
     @return A Vector of integers containing the row, column, and direction being built in that creates a deadly tessera, if it exists.
     */
    public Vector<Integer> FindDeadlyTessera(Board a_board, char a_color, char a_dangerColor)
    {
        //Search for all valid consecutive 6 locations that have 3 of the specified piece color and 3 empty locations.
        //Deadly tesseras are in the form: - B B * B - , where both ends of the set of six are empty.
        Vector<Vector<Vector<Integer>>> possibleMoves = FindAllMoves(a_board, 3, a_color, 6);

        Vector<Vector<Integer>> possiblePlacements = new Vector<Vector<Integer>>();

        for (int i = 0; i < possibleMoves.size(); i++)
        {
            Vector<Integer> emptyIndices = FindEmptyIndices(a_board, possibleMoves.get(i));

            //If both ends of the set of 6 locations are empty (the empty indices are 0 and 5), this means that a deadly tessera can be formed.
            if (emptyIndices.get(0) == 0 && emptyIndices.get(2) == 5)
            {
                //emptyIndices[1] is the empty location that is NOT on one of the ends.
                //This creates four consecutive stones with two open ends when a stone is placed there. Store it for later.
                possiblePlacements.add(possibleMoves.get(i).get(emptyIndices.get(1)));
            }
        }

        if (!possiblePlacements.isEmpty())
        {
            //Return the location that does NOT put the player in danger of getting captured.
            for (int i = 0; i < possiblePlacements.size(); i++)
            {
                if (!InDangerOfCapture(a_board, possiblePlacements.get(i), a_dangerColor))
                {
                    return possiblePlacements.get(i);
                }
            }

            //If all possible locations result in capture, deadly tesseras should still be built/blocked as it likely results in a win in the following turns.
            return possiblePlacements.get(0);
        }
        else
        {
            return new Vector<Integer>();
        }
    }

    /**
     Finds a location on the board that would create three consecutive pieces, if any are possible, given multiple sets of possible locations to build on.
     @param a_board A Board object, representing the current board of the round.
     @param a_possibleMoves A 3-D Vector of integers, representing sequences of five locations on the board.
     @param a_dangerColor A character, representing the stone color to check if it is in danger of being captured.
     @return A Vector of integers containing the row, column, and direction being built in that creates three consecutive stones, if it exists.
     */
    public Vector<Integer> FindThreeConsecutive(Board a_board, Vector<Vector<Vector<Integer>>> a_possibleMoves, char a_dangerColor)
    {
        //Search for possible 3 in a rows. If there is one, that is the most optimal play.
        for (int i = 0; i < a_possibleMoves.size(); i++) {
            Vector<Integer> emptyIndices = FindEmptyIndices(a_board, a_possibleMoves.get(i));

            for (int j = 0; j < emptyIndices.size(); j++) {
                int possibleConsecutive = FindConsecutiveIfPlaced(a_board, a_possibleMoves.get(i), emptyIndices.get(j));

                //If it's possible to create four consecutive stones, and it doesn't put the player in danger of being captured, return it.
                if (possibleConsecutive == StrategyConstants.CONSECUTIVE_3_DISTANCE && !InDangerOfCapture(a_board, a_possibleMoves.get(i).get(emptyIndices.get(j)), a_dangerColor))
                {
                    return a_possibleMoves.get(i).get(emptyIndices.get(j));
                }
            }
        }

        return new Vector<Integer>();
    }

    /**
     Finds a location on the board that would create four consecutive pieces, if any are possible, given multiple sets of possible locations to build on.
     @param a_board A Board object, representing the current board of the round.
     @param a_possibleMoves A 3-D Vector of integers, representing sequences of five locations on the board.
     @param a_dangerColor A character, representing the stone color to check if it is in danger of being captured.
     @return A Vector of integers containing the row, column, and direction being built in that creates four consecutive stones, if it exists.
     */
    public Vector<Integer> FindFourConsecutive(Board a_board, Vector<Vector<Vector<Integer>>> a_possibleMoves, char a_dangerColor)
    {
        //Search for possible 4 in a rows. If there is one, that is the most optimal play.
        for (int i = 0; i < a_possibleMoves.size(); i++) {
            Vector<Integer> emptyIndices = FindEmptyIndices(a_board, a_possibleMoves.get(i));

            for (int j = 0; j < emptyIndices.size(); j++) {
                int possibleConsecutive = FindConsecutiveIfPlaced(a_board, a_possibleMoves.get(i), emptyIndices.get(j));

                //If it's possible to create four consecutive stones, and it doesn't put the player in danger of being captured, return it.
                if (possibleConsecutive == StrategyConstants.CONSECUTIVE_4_DISTANCE && !InDangerOfCapture(a_board, a_possibleMoves.get(i).get(emptyIndices.get(j)), a_dangerColor))
                {
                    return a_possibleMoves.get(i).get(emptyIndices.get(j));
                }
            }
        }

        return new Vector<Integer>();
    }

    /**
     Finds the most optimal play when the handicap (second turn of the first player) is active.
     @param a_board A Board object, representing the current board of the round.
     @return A Vector of integers, representing the row and column of the most optimal handicap play.
     */
    public Vector<Integer> FindHandicapPlay(Board a_board)
    {
        Vector<Vector<Vector<Integer>>> possibleMoves = FindAllMoves(a_board, 1, 'W', StrategyConstants.CONSECUTIVE_5_DISTANCE);

        Vector<Vector<Character>> board = a_board.GetBoard();
        Vector<Vector<Integer>> playLocations = new Vector<Vector<Integer>>();
        for (int i = 0; i < possibleMoves.size(); i++)
        {
            //There are 5 locations within each list of possible moves.
            //The index 0 represents the beginning of the set of 5 locations and index 4 represents the end. Ex: * - - - - and - - - - *
            int firstRow = possibleMoves.get(i).get(0).get(0);
            int firstCol = possibleMoves.get(i).get(0).get(1);
            int lastRow = possibleMoves.get(i).get(4).get(0);
            int lastCol = possibleMoves.get(i).get(4).get(1);

            //Handles the W - - - - case.
            if (board.get(firstRow).get(firstCol) != '-')
            {
                //possibleMoves[i][3] represents a location on the edge of the handicap. Ex: W - - * - where * represents the play location.
                playLocations.add(possibleMoves.get(i).get(3));
            }

            //Handles the - - - - W case.
            else if (board.get(lastRow).get(lastCol) != '-')
            {
                //possibleMoves[i][3] represents a location on the edge of the handicap. Ex: - * - - W where * represents the play location..
                playLocations.add(possibleMoves.get(i).get(1));
            }
        }

        //Shuffle the possible play locations so that the computer plays in different directions in different games when it is playing on the handicap turn.
        Collections.shuffle(playLocations);
        return playLocations.get(0);
    }

    /**
     The main function of the Player class - used for testing purposes.
     @param args An array of strings, representing command line arguments.
     */
    public static void main(String[] args)
    {
        Vector<Vector<Character>> testBoard = new Vector<Vector<Character>>() {
            {
                add(new Vector<Character>(Arrays.asList('-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', 'B')));
                add(new Vector<Character>(Arrays.asList('-', '-', '-', 'B', '-', '-', '-', '-', '-', '-', '-', '-', 'B', '-', '-', '-', '-', 'W', '-')));
                add(new Vector<Character>(Arrays.asList('-', '-', 'B', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', 'W', '-', '-')));
                add(new Vector<Character>(Arrays.asList('-', 'W', '-', '-', '-', '-', '-', '-', '-', '-', 'W', '-', '-', '-', '-', 'W', '-', '-', '-')));
                add(new Vector<Character>(Arrays.asList('B', '-', '-', '-', '-', 'B', '-', '-', '-', '-', '-', '-', '-', '-', 'W', '-', '-', '-', '-')));
                add(new Vector<Character>(Arrays.asList('-', '-', '-', '-', '-', '-', 'W', '-', '-', '-', '-', '-', '-', 'B', '-', '-', '-', '-', '-')));
                add(new Vector<Character>(Arrays.asList('-', '-', '-', '-', '-', '-', 'W', '-', '-', '-', '-', '-', 'W', '-', '-', '-', '-', '-', '-')));
                add(new Vector<Character>(Arrays.asList('-', '-', '-', '-', '-', '-', 'W', '-', '-', '-', '-', 'B', '-', '-', '-', '-', '-', '-', '-')));
                add(new Vector<Character>(Arrays.asList('-', '-', '-', '-', '-', '-', 'W', '-', '-', '-', 'B', '-', '-', '-', '-', '-', '-', '-', '-')));
                add(new Vector<Character>(Arrays.asList('-', '-', '-', '-', '-', '-', 'B', '-', '-', '-', '-', '-', '-', '-', '-', '-', 'W', '-', 'B')));
                add(new Vector<Character>(Arrays.asList('-', '-', '-', 'W', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', 'B', '-', '-')));
                add(new Vector<Character>(Arrays.asList('-', '-', 'B', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', 'B', '-', '-', '-')));
                add(new Vector<Character>(Arrays.asList('-', '-', 'B', 'B', 'B', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-')));
                add(new Vector<Character>(Arrays.asList('-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-')));
                add(new Vector<Character>(Arrays.asList('-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', 'B', 'B', 'W')));
                add(new Vector<Character>(Arrays.asList('-', '-', '-', '-', '-', '-', '-', 'B', '-', '-', '-', 'B', '-', '-', '-', 'B', '-', 'B', '-')));
                add(new Vector<Character>(Arrays.asList('-', '-', '-', '-', '-', '-', '-', '-', 'B', '-', '-', '-', '-', '-', 'W', '-', '-', 'W', 'W')));
                add(new Vector<Character>(Arrays.asList('-', '-', '-', '-', '-', '-', '-', '-', '-', 'B', '-', '-', '-', '-', '-', '-', '-', '-', '-')));
                add(new Vector<Character>(Arrays.asList('-', '-', '-', '-', '-', '-', '-', '-', '-', '-', 'B', '-', '-', '-', '-', '-', '-', '-', '-')));

            }
        };

        Board b = new Board();
        Player p = new Player();
        b.SetBoard(testBoard);

        Vector<Vector<Vector<Integer>>> testSequences = p.FindAllMoves(b, 3, 'B', 5);
        Vector<Vector<Integer>> sequence = testSequences.get(0);

        p.SetCapturedPairs(0);
        StringBuilder win_reason = new StringBuilder();
        Vector<Integer> winMove = p.MakeWinningMove(b, 'W', win_reason);

        System.out.println();
        System.out.println("Win Move: " + winMove);
        System.out.println("Win reason:" + win_reason);

        System.out.println();
        Vector<Integer> preventWinMove = p.PreventWinningMove(b, 'W');
        System.out.println("Prevent Win Move: " + preventWinMove);
        System.out.println();

        System.out.println("Danger at (2, 11): " + p.InDangerOfCapture(b, new Vector<Integer>(Arrays.asList(2, 11)), 'B'));
        System.out.println();

        System.out.println("Flank: " + p.FindFlanks(b, 'W'));
        System.out.println();

        System.out.println("Deadly Tessera: " + p.FindDeadlyTessera(b, 'B', 'B'));
        System.out.println();

        Board anotherBoard = new Board();
        anotherBoard.PlaceStone('J', 10, 'W');
        anotherBoard.PlaceStone('J', 12, 'B');

        anotherBoard.DisplayBoard();
        System.out.println("Handicap play: " + p.FindHandicapPlay(anotherBoard));
        System.out.println();
    }
}

