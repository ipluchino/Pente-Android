package edu.ramapo.ipluchino.pente.View;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.w3c.dom.Text;

import java.util.Vector;

import edu.ramapo.ipluchino.pente.Model.Round;

import edu.ramapo.ipluchino.pente.R;

public class RoundViewActivity extends AppCompatActivity {

    private Round m_round;
    private GridLayout m_buttonGridLayout;
    private Vector<Vector<Button>> m_locationButtons;
    private Button m_highlightedButton;
    private Button m_placeStoneButtonHuman;
    private Button m_getHelpButton;
    private Button m_saveAndExitButtonHuman;
    private Button m_placeStoneButtonComputer;
    private Button m_saveAndExitButtonComputer;
    private TextView m_nextTurnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roundview);

        //Initialize the round model.
        m_round = new Round();
        m_locationButtons = new Vector<Vector<Button>>(361);
        m_highlightedButton = null;
        m_buttonGridLayout = findViewById(R.id.buttonsGridLayout);
        m_placeStoneButtonHuman = findViewById(R.id.placeStoneButtonHuman);
        m_getHelpButton = findViewById(R.id.getHelpButton);
        m_saveAndExitButtonHuman = findViewById(R.id.saveAndExitButtonHuman);
        m_placeStoneButtonComputer = findViewById(R.id.placeStoneButtonComputer);
        m_saveAndExitButtonComputer = findViewById(R.id.saveAndExitButtonComputer);
        m_nextTurnTextView = findViewById(R.id.nextTurnTextView);

        //TEMPORARY!! WILL BE AUTOMATIC IN THE FUTURE - REMOVED LATER.
        m_round.SetNextPlayerIndex(1);
        m_round.SetHumanColor('B');
        m_round.SetComputerColor('W');

        //Initialize the board
        InitializeBoard();

        //SET LISTENERS HERE.
        m_placeStoneButtonHuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                } else {
                    String boardRow = GetHighlightedButtonRow();
                    String boardColumn = GetHighlightedButtonColumn();
                    String location = boardColumn + boardRow;

                    String move = m_round.PlayTurn(location);
                    Log.d("myTag", move);

                    m_highlightedButton = null;

                    UpdateAllLocationButtons();
                    DisplayComputerComponents();
                }
            }
        });

        m_placeStoneButtonComputer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Note: parameter is an empty string because the computer will automatically choose its optimal play location.
                String move = m_round.PlayTurn("");
                Log.d("myTag", move);

                UpdateAllLocationButtons();
                DisplayHumanComponents();
            }
        });

        //Initial Turn Display:
        if (m_round.IsHumanTurn())
        {
            DisplayHumanComponents();
        }
        else
        {
            DisplayComputerComponents();
        }

    }

    private void DisplayHumanComponents()
    {
        EnableLocationButtons();

        m_nextTurnTextView.setText("Next turn - Human");

        m_placeStoneButtonHuman.setVisibility(View.VISIBLE);
        m_getHelpButton.setVisibility(View.VISIBLE);
        m_saveAndExitButtonHuman.setVisibility(View.VISIBLE);

        m_placeStoneButtonComputer.setVisibility(View.GONE);
        m_saveAndExitButtonComputer.setVisibility(View.GONE);
    }

    private void DisplayComputerComponents()
    {
        DisableLocationButtons();

        m_nextTurnTextView.setText("Next turn - Computer");

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

                //Set tags on the button so that their row and column can easily be identified.
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
        String boardRow = String.valueOf(19 - rowTag);

        return boardRow;
    }

    private String GetHighlightedButtonColumn()
    {
        int columnTag = (int) m_highlightedButton.getTag(R.id.column);
        String boardColumn = String.valueOf((char) (columnTag + 'A'));

        return boardColumn;
    }

    private void UpdateAllLocationButtons()
    {
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
                button.setEnabled(false);
            }
        }
    }

}
