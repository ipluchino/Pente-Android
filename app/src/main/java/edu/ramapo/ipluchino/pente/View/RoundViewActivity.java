package edu.ramapo.ipluchino.pente.View;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import edu.ramapo.ipluchino.pente.Model.Round;

import edu.ramapo.ipluchino.pente.R;

public class RoundViewActivity extends AppCompatActivity {
    private final int WRITE_PERMISSION = 200;

    private Intent m_intent;
    private Round m_round;
    private GridLayout m_buttonGridLayout;
    private Vector<Vector<Button>> m_locationButtons;
    private Button m_highlightedButton;
    private Button m_placeStoneButtonHuman;
    private Button m_getHelpButton;
    private Button m_saveAndExitButtonHuman;
    private Button m_placeStoneButtonComputer;
    private Button m_saveAndExitButtonComputer;
    private Button m_playAgainButton;
    private Button m_finishTournamentButton;
    private Button m_logButton;
    private TextView m_nextTurnTextView;
    private TextView m_humanInformationTextView;
    private TextView m_computerInformationTextView;
    private TextView m_scoringTextView;
    private Vector<String> m_logData;
    private int m_logCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roundview);

        //Initialize the private variables.
        m_intent = getIntent();
        m_round = (Round) m_intent.getSerializableExtra("round");
        m_locationButtons = new Vector<Vector<Button>>(361);
        m_highlightedButton = null;
        m_buttonGridLayout = findViewById(R.id.buttonsGridLayout);
        m_placeStoneButtonHuman = findViewById(R.id.placeStoneButtonHuman);
        m_getHelpButton = findViewById(R.id.getHelpButton);
        m_saveAndExitButtonHuman = findViewById(R.id.saveAndExitButtonHuman);
        m_placeStoneButtonComputer = findViewById(R.id.placeStoneButtonComputer);
        m_saveAndExitButtonComputer = findViewById(R.id.saveAndExitButtonComputer);
        m_playAgainButton = findViewById(R.id.playAgainButton);
        m_finishTournamentButton = findViewById(R.id.finishTournamentButton);
        m_logButton = findViewById(R.id.logButton);
        m_nextTurnTextView = findViewById(R.id.nextTurnTextView);
        m_humanInformationTextView = findViewById(R.id.humanInformationTextView);
        m_computerInformationTextView = findViewById(R.id.computerInformationTextView);
        m_scoringTextView = findViewById(R.id.scoringTextView);
        m_logData = new Vector<String>();

        //Initialize the board
        InitializeBoard();

        //SET LISTENERS HERE.
        m_placeStoneButtonHuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //The user hasn't selected a location yet.
                if (m_highlightedButton == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RoundViewActivity.this);
                    builder.setTitle("No location selected!");
                    builder.setMessage("You must select a location to place your stone before you click the \"Place Stone\" button!");

                    //OK button to clear the alert dialog.
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //No need to do anything here.
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                //The user has selected an invalid location.
                else if (!m_round.ValidMove(GetHighlightedButtonColumn()+GetHighlightedButtonRow()).equals(""))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RoundViewActivity.this);
                    builder.setTitle("Invalid Location Selected!");
                    builder.setMessage(m_round.ValidMove(GetHighlightedButtonColumn()+GetHighlightedButtonRow()));

                    //OK button to clear the alert dialog.
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //No need to do anything here.
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                //The location is valid.
                else
                {
                    String boardRow = GetHighlightedButtonRow();
                    String boardColumn = GetHighlightedButtonColumn();
                    String location = boardColumn + boardRow;
                    m_highlightedButton = null;

                    String move = m_round.PlayTurn(location);

                    //Add the move to the log and update the display.
                    m_logCounter++;
                    m_logData.add(Integer.toString(m_logCounter) + ". " + move);
                    UpdateRoundInformation();

                    //Continue alternating turns until the round is over.
                    if (!m_round.RoundOver().equals(""))
                    {
                        RoundCompleted();
                    }
                    else
                    {
                        DisplayComputerComponents();
                    }
                }
            }
        });

        m_placeStoneButtonComputer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Note: parameter is an empty string because the computer will automatically choose its optimal play location.
                String move = m_round.PlayTurn("");

                //Add the move to the log and update the display.
                m_logCounter++;
                m_logData.add(Integer.toString(m_logCounter) + ". " + move);
                UpdateRoundInformation();

                //Continue alternating turns until the round is over.
                if (!m_round.RoundOver().equals(""))
                {
                    RoundCompleted();
                }
                else
                {
                    DisplayHumanComponents();
                }
            }
        });

        m_getHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RoundViewActivity.this);
                builder.setTitle("Computer's Recommendation:");

                //The vector help is in the form (location, reasoning). The reasoning is what should be displayed to the user.
                Vector<String> help = m_round.GetHelp();
                Vector<Integer> helpLocation = ConvertLocation(help.get(0));
                String helpMsg = help.get(1);

                builder.setMessage(helpMsg);

                //OK button to clear the alert dialog.
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (m_highlightedButton != null)
                        {
                            m_highlightedButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.blackborder));
                        }


                        Button reccomendedButton = m_locationButtons.get(helpLocation.get(0)).get(helpLocation.get(1));
                        reccomendedButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.highlightedbutton));
                        m_highlightedButton = reccomendedButton;
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        m_saveAndExitButtonHuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveTournament();
            }
        });

        m_saveAndExitButtonComputer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveTournament();
            }
        });

        m_playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Restart this activity and pass the current round object to it so it can begin the next round.
                m_intent = new Intent(getApplicationContext(), RoundViewActivity.class);
                m_intent.putExtra("round", m_round);
                startActivity(m_intent);
            }
        });

        m_finishTournamentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //If the user is done playing the tournament, pass the scores of each player to the TournamentOverActivity so the winner can be displayed.
                m_intent = new Intent(getApplicationContext(), TournamentOverActivity.class);
                m_intent.putExtra("humanScore", m_round.GetHumanScore());
                m_intent.putExtra("computerScore", m_round.GetComputerScore());
                startActivity(m_intent);
            }
        });

        //https://stackoverflow.com/questions/50083803/how-to-make-a-alertdialog-list-scrollable-android
        //https://stackoverflow.com/questions/51703725/scroll-view-not-working-in-alert-dialog
        //https://stackoverflow.com/questions/14834685/android-alertdialog-setview-rules
        m_logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Dynamically create a scrollView that will hold all the logs. This ensures it will be scrollable (since there will be many log entries).
                ScrollView scrollView = new ScrollView(getApplicationContext());
                TextView textView = new TextView(getApplicationContext());

                //Put each entry in the log vector into a single string so that it can be displayed.
                String allLogs = "";
                for (String entry : m_logData)
                {
                    allLogs += entry + "\n\n";
                }

                textView.setText(allLogs);
                scrollView.addView(textView);

                //Create the alert dialog.
                AlertDialog.Builder builder = new AlertDialog.Builder(RoundViewActivity.this);
                builder.setTitle("Logs");

                //Attach the ScrollView containing all of the logs to the alert dialog.
                builder.setView(scrollView);

                //OK button to clear the alert dialog.
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //No need to do anything here.
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        //Determine first player of the round, if necessary.
        if (m_round.GetNextPlayerIndex() == -1)
        {
            //A coin toss is necessary to determine the first player.
            if (m_round.ScoresTied())
            {
                //Pass the current round object to the CoinTossActivity.
                m_intent = new Intent(getApplicationContext(), CoinTossActivity.class);
                m_intent.putExtra("round", m_round);
                startActivity(m_intent);
            }
            else
            {
                //The first player can be determined by their scores.
                m_round.DetermineFirstPlayerViaScore();

                //Announce who is going first in this case via an alert dialog.
                AlertDialog.Builder builder = new AlertDialog.Builder(RoundViewActivity.this);
                builder.setTitle("First Player");

                String msg = "";
                if (m_round.IsHumanTurn())
                {
                    msg = "You will be going first because you have a higher score than the computer.";

                    //Add who went first to the logs.
                    m_logCounter++;
                    m_logData.add(Integer.toString(m_logCounter) + ". FIRST PLAYER: The human player will go first because they have a higher score.");
                }
                else
                {
                    msg = "The computer will be going first because they have a higher score than you.";

                    //Add who went first to the logs.
                    m_logCounter++;
                    m_logData.add(Integer.toString(m_logCounter) + ". FIRST PLAYER: The computer player will go first because they have a higher score.");
                }

                builder.setMessage(msg);

                //OK button to clear the alert dialog.
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //No need to do anything here.
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }
        else
        {
            //User must have started a new game, and the first player was determined in CoinTossActivity OR a game was loaded.
            //For logging purposes, log if the game was loaded from a file or who won the coin toss.
            boolean loadedFromFile = m_intent.getBooleanExtra("loadedFromFile", false);

            if (loadedFromFile)
            {
                //The tournament was loaded from a file.
                m_logCounter++;
                m_logData.add(Integer.toString(m_logCounter) + ". The tournament was loaded from a file.");
            }
            else
            {
                if (m_round.IsHumanTurn())
                {
                    //The user won the coin toss.
                    m_logCounter++;
                    m_logData.add(Integer.toString(m_logCounter) + ". FIRST PLAYER: The human player will go first because they won the coin toss.");
                }
                else
                {
                    //The user lost the coin toss.
                    m_logCounter++;
                    m_logData.add(Integer.toString(m_logCounter) + ". FIRST PLAYER: The computer player will go first because the human lost the coin toss.");
                }
            }


        }

        //Initial turn display.
        if (m_round.IsHumanTurn())
        {
            //Display the Human components if it is the Human's turn.
            DisplayHumanComponents();
        }
        else
        {
            //Display the Computer components if it is the Computer's turn.
            DisplayComputerComponents();
        }

        UpdateRoundInformation();
    }

    private void DisplayHumanComponents()
    {
        EnableLocationButtons();

        m_nextTurnTextView.setText("Next turn: Human - " + m_round.GetHumanColor());

        m_placeStoneButtonHuman.setVisibility(View.VISIBLE);
        m_getHelpButton.setVisibility(View.VISIBLE);
        m_saveAndExitButtonHuman.setVisibility(View.VISIBLE);

        m_placeStoneButtonComputer.setVisibility(View.GONE);
        m_saveAndExitButtonComputer.setVisibility(View.GONE);
    }

    private void DisplayComputerComponents()
    {
        DisableLocationButtons();

        m_nextTurnTextView.setText("Next turn: Computer - " + m_round.GetComputerColor());

        m_placeStoneButtonComputer.setVisibility(View.VISIBLE);
        m_saveAndExitButtonComputer.setVisibility(View.VISIBLE);

        m_placeStoneButtonHuman.setVisibility(View.GONE);
        m_getHelpButton.setVisibility(View.GONE);
        m_saveAndExitButtonHuman.setVisibility(View.GONE);
    }

    private void InitializeBoard()
    {
        //Create the column and row headers
        CreateHeaders();

        //Initialize the individual buttons
        for(int i = 0; i < 19; i++)
        {
            Vector<Button> rowOfButtons = new Vector<Button>();
            for(int j = 0; j < 19; j++)
            {
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                Button button = new Button(this);

                //Set tags on the button so that their row and column can easily be identified as well as if their is a stone placed on it.
                button.setTag(R.id.row, i);
                button.setTag(R.id.column, j);
                button.setTag(R.id.stonePlaced, false);

                button.setTextColor(Color.BLUE);
                button.setBackgroundColor(Color.WHITE);
                button.setPadding(0, 0, 0, 0);

                //https://stackoverflow.com/questions/7815689/how-do-you-obtain-a-drawable-object-from-a-resource-id-in-android-package
                //https://stackoverflow.com/questions/7690416/android-border-for-button
                button.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.blackborder));

                //Note: The row and column are incremented by 1 because of the row and column headers.
                params.rowSpec= GridLayout.spec(i+1);
                params.columnSpec= GridLayout.spec(j+1);
                params.width = GenerateCellSize(380/20, this);
                params.height = GenerateCellSize(380/20, this);
                params.setMargins(0, 0, 0, 0);

                //Set the onClick listener for the button.
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Reset the current highlighted button, if one exists.
                        if (m_highlightedButton != null)
                        {
                            m_highlightedButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.blackborder));
                        }

                        //Set the button that was clicked to be highlighted and store it.
                        view.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.highlightedbutton));
                        m_highlightedButton = (Button) view;

                    }
                });

                m_buttonGridLayout.addView(button, params);

                //Add the button to the vector of buttons so it can be referenced later.
                rowOfButtons.add(button);
            }

            //Add the row of buttons to the main storage vector.
            m_locationButtons.add(rowOfButtons);
        }
    }

    private void CreateHeaders()
    {
        //Generate the column headers.
        for (int col = 0; col < 19; col++)
        {
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            TextView colHeader = new TextView(this);

            //Set the TextView object's parameters.
            colHeader.setText(Character.toString((char)('A' + col)));
            colHeader.setTextColor(Color.BLACK);
            colHeader.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.blackborder));
            colHeader.setGravity(Gravity.CENTER);
            colHeader.setPadding(0, 0, 0, 0);

            //Set the GridLayout Parameters.
            params.rowSpec= GridLayout.spec(0);
            params.columnSpec= GridLayout.spec(col+1);
            params.height = GenerateCellSize(380/20, this);
            params.width = GenerateCellSize(380/20, this);
            params.setMargins(0, 0, 0, 0);

            m_buttonGridLayout.addView(colHeader, params);
        }

        //Generate the row headers
        for (int row = 0; row < 19; row++)
        {
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            TextView rowHeader = new TextView(this);

            //Set the TextView object's parameters.
            rowHeader.setText(Integer.toString(19-row));
            rowHeader.setTextColor(Color.BLACK);
            rowHeader.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.blackborder));
            rowHeader.setGravity(Gravity.CENTER);
            rowHeader.setPadding(0, 0, 0, 0);

            //Set the GridLayout Parameters.
            params.rowSpec= GridLayout.spec(row+1);
            params.columnSpec= GridLayout.spec(0);
            params.height = GenerateCellSize(380/20, this);
            params.width = GenerateCellSize(380/20, this);
            params.setMargins(0, 0, 0, 0);

            m_buttonGridLayout.addView(rowHeader, params);
        }

    }

    private int GenerateCellSize(int a_dpToConvert, Context a_context)
    {
        //Convert dp to its pixel representation.
        float scale = a_context.getResources().getDisplayMetrics().density;
        int pixelRepresentation = (int) (a_dpToConvert * scale + 0.5f);

        return pixelRepresentation;
    }

    private String GetHighlightedButtonRow()
    {
        int rowTag = (int) m_highlightedButton.getTag(R.id.row);
        String boardRow = Integer.toString(19 - rowTag);

        return boardRow;
    }

    private String GetHighlightedButtonColumn()
    {
        int columnTag = (int) m_highlightedButton.getTag(R.id.column);
        String boardColumn = Character.toString((char) (columnTag + 'A'));

        return boardColumn;
    }

    private void UpdateRoundInformation()
    {
        //Update the human's round information.
        int humanScore = m_round.GetHumanScore();
        int humanCapturedPairs = m_round.GetHumanCapturedPairs();

        String updatedHumanInformation = "Tournament Score: " + humanScore + "\n" + "Captured Pairs: " + humanCapturedPairs;
        m_humanInformationTextView.setText(updatedHumanInformation);

        //Update the computer's round information.
        int computerScore = m_round.GetComputerScore();
        int computerCapturedPairs = m_round.GetComputerCapturedPairs();

        String updatedComputerInformation = "Tournament Score: " + computerScore + "\n" + "Captured Pairs: " + computerCapturedPairs;
        m_computerInformationTextView.setText(updatedComputerInformation);

        //Update all the images on the button to make sure they are correct.
        for (int i = 0; i < 19; i++)
        {
            for (int j = 0; j < 19; j++)
            {
                Button button = m_locationButtons.get(i).get(j);
                char stone = m_round.StoneAt(i, j);

                //If the location isn't empty, update the board to display the stone placed there.
                if (stone != '-')
                {
                    button.setTag(R.id.stonePlaced, true);
                    if (stone == 'W')
                    {
                        button.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.whitestone));
                    }
                    else
                    {
                        button.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.blackstone));
                    }
                }
                else
                {
                    button.setTag(R.id.stonePlaced, false);
                    button.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.blackborder));
                }
            }
        }
    }

    private void EnableLocationButtons()
    {
        for (Vector<Button> row : m_locationButtons)
        {
            for (Button button : row)
            {
                //If the location already has a stone placed on it, make sure to leave the button disabled. Otherwise enable them.
                boolean stonePlaced = (boolean) button.getTag(R.id.stonePlaced);
                if (stonePlaced)
                {
                    button.setEnabled(false);
                }
                else
                {
                    button.setEnabled(true);
                }
            }
        }
    }

    private void DisableLocationButtons()
    {
        for (Vector<Button> row : m_locationButtons)
        {
            for (Button button : row)
            {
                //Disable every button.
                button.setEnabled(false);
            }
        }
    }

    private Vector<Integer> ConvertLocation(String a_location)
    {
        Vector<Integer> location = new Vector<Integer>();

        int row = 19 - Integer.parseInt(a_location.substring(1));
        int column = (int) (a_location.charAt(0) - 'A');

        //Extract the row and convert it to the correct vector index representation.
        location.add(row);

        //Extract the column and convert it to the correct vector index representation.
        location.add(column);

        return location;
    }

    private void RoundCompleted()
    {
        //Explain to the user that the round has ended through a pop-up.
        AlertDialog.Builder builder = new AlertDialog.Builder(RoundViewActivity.this);
        builder.setTitle("The round is over!");

        String roundOverMsg = m_round.RoundOver();
        builder.setMessage(roundOverMsg);

        //OK button to clear the alert dialog.
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //No need to do anything here.
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        //Remove the Human and Computer action buttons from the screen.
        m_placeStoneButtonHuman.setVisibility(View.GONE);
        m_getHelpButton.setVisibility(View.GONE);
        m_saveAndExitButtonHuman.setVisibility(View.GONE);
        m_placeStoneButtonComputer.setVisibility(View.GONE);
        m_saveAndExitButtonComputer.setVisibility(View.GONE);
        m_nextTurnTextView.setText("Next turn: Game over!");

        //Obtain the scores earned by each player for the current round.
        String humanRoundScore = "Points scored by the Human this round: " + m_round.ScoreHuman() + "\n";
        String computerRoundScore = "Points scored by the Computer this round: " + m_round.ScoreComputer() + "\n\n";

        //Update the scores, and reset the round in preparation for another (if the user decides to keep playing).
        m_round.UpdateScores();
        m_round.ResetRound();

        //Obtain the updated tournament scores for each player.
        String updatedHumanTournamentScore = "Updated Human's tournament score: " + m_round.GetHumanScore() + "\n";
        String updatedComputerTournamentScore = "Updated Computer's tournament score: " + m_round.GetComputerScore();

        //Display the round and updated tournament scores of both players to the user.
        m_scoringTextView.setText(humanRoundScore + computerRoundScore + updatedHumanTournamentScore + updatedComputerTournamentScore);

        //Determine if the user wishes to keep playing by making the choice buttons visible.
        m_playAgainButton.setVisibility(View.VISIBLE);
        m_finishTournamentButton.setVisibility(View.VISIBLE);
    }

    //https://stackoverflow.com/questions/10903754/input-text-dialog-android
    private void SaveTournament()
    {
        //Request the permission to write to external storage, if the user has not already granted it.
        if (ActivityCompat.checkSelfPermission(RoundViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RoundViewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION);
        }

        //Ask the user for the file name to save the file to.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save Tournament");
        builder.setMessage("Choose a file name to save the tournament to (without the .txt). The file will be saved to the downloads folder.");

        //Create an "EditText" object to store the input from the user.
        final EditText input = new EditText(this);
        builder.setView(input);

        //Confirm button.
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                File saveLocation = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/Pente");

                //Create the directory if it does not exist already exist.
                if (!saveLocation.exists()) {
                    saveLocation.mkdir();
                }

                //Get the location of the Pente folder inside the Documents folder.
                String penteFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/Pente";

                //Get the full path to the file that the user would like to save the tournament to.
                String userInput = input.getText().toString();
                String fileName = penteFolder + "/" + userInput + ".txt";

                //If the user entered an invalid character, let them know.
                if (InvalidFileName(userInput))
                {
                    DisplayInvalidFileName();
                }
                else
                {
                    //If the file name is valid, save the tournament.
                    try {
                        m_round.SaveGame(fileName);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    //Go back to the main welcome screen after a file is saved.
                    m_intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                    startActivity(m_intent);
                }
            }
        });

        //Cancel button - user does not wish to save the tournament anymore.
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //No need to do anything here.
            }
        });

        builder.show();
    }

    private boolean InvalidFileName(String a_fileName)
    {
        String[] invalidCharacters = {"\\", "/", "?", "<", ">", ":", ";", "|", "\""};

        for (String invalid :invalidCharacters)
        {
            if (a_fileName.contains(invalid))
            {
                return true;
            }
        }

        return false;
    }

    private void DisplayInvalidFileName()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(RoundViewActivity.this);
        builder.setTitle("Invalid file name!");

        builder.setMessage("The file name you provided is invalid! Please try again.");

        //OK button to clear the alert dialog.
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //No need to do anything here.
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
