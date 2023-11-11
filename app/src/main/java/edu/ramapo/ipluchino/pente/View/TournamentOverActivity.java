package edu.ramapo.ipluchino.pente.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import edu.ramapo.ipluchino.pente.R;

public class TournamentOverActivity extends AppCompatActivity {
    //Private variables.
    private Intent m_intent;
    private int m_humanScore;
    private int m_computerScore;
    private Button m_homeButton;
    private TextView m_winnerTextView;
    private TextView m_scoresTextView;
    private ImageView m_thumbsUp;
    private ImageView m_sadFace;
    private ImageView m_shrug;

    /**
     Creates the TournamentOverActivity and sets the layout, along with the event handlers.
     @param savedInstanceState A Bundle object, that is used when the activity is being restored from a previous state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournamentover);

        //Initialize the private variables.
        m_intent = getIntent();
        m_humanScore = m_intent.getIntExtra("humanScore", 0);
        m_computerScore = m_intent.getIntExtra("computerScore", 0);
        m_homeButton = findViewById(R.id.homeButton);
        m_winnerTextView = findViewById(R.id.winnerTextView);
        m_scoresTextView = findViewById(R.id.scoreResultsTextView);
        m_thumbsUp = findViewById(R.id.thumbsUpImageView);
        m_sadFace = findViewById(R.id.sadFaceImageView);
        m_shrug = findViewById(R.id.shrugImageView);

        //Set all of the onClickListeners for the buttons.
        //Home button onClickListener.
        m_homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Send the user back to the welcome screen after they are done viewing the tournament results.
                m_intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                startActivity(m_intent);
            }
        });

        //The human player won the tournament.
        if (m_humanScore > m_computerScore)
        {
            DisplayHumanWon();
        }
        //The computer player won the tournament.
        else if (m_computerScore > m_humanScore)
        {
            DisplayComputerWon();
        }
        //The tournament ended in a draw.
        else
        {
            DisplayDraw();
        }

        //Display the final scores of both players to the user.
        DisplayFinalScores();
    }

    /**
     Sets the display of the screen to represent that the human won the tournament.
     */
    private void DisplayHumanWon()
    {
        //Edit the display to show that the human player won the tournament.
        m_thumbsUp.setVisibility(View.VISIBLE);
        m_winnerTextView.setText(getString(R.string.winner_human));
    }

    /**
     Sets the display of the screen to represent that the computer won the tournament.
     */
    private void DisplayComputerWon()
    {
        //Edit the display to show that the computer player won the tournament.
        m_sadFace.setVisibility(View.VISIBLE);
        m_winnerTextView.setText(getString(R.string.winner_computer));
    }

    /**
     Sets the display of the screen to represent that the tournament ended in a draw.
     */
    private void DisplayDraw()
    {
        //Edit the display to show that a draw occurred.
        m_shrug.setVisibility(View.VISIBLE);
        m_winnerTextView.setText(getString(R.string.winner_draw));
    }

    /**
     Displays the final scores of both players in a TextView on the screen.
     */
    private void DisplayFinalScores()
    {
        //Display the final scores to the user.
        String finalScores = "Your final score: " + m_humanScore + "\n" + "Computer final score: " + m_computerScore;
        m_scoresTextView.setText(finalScores);
    }


}