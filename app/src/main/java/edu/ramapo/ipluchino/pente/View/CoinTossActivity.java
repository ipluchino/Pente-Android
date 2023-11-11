package edu.ramapo.ipluchino.pente.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.transition.Fade;
import android.transition.TransitionManager;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;
import edu.ramapo.ipluchino.pente.Model.Round;
import edu.ramapo.ipluchino.pente.R;

public class CoinTossActivity extends AppCompatActivity {
    //Constants.
    final int HEADS = 0;
    final int TAILS = 1;

    //Private variables.
    private Intent m_intent;
    private Round m_currentRound;
    private Button m_headsButton;
    private Button m_tailsButton;
    private Button m_continueButton;
    private ImageView m_headsImage;
    private ImageView m_tailsImage;
    private ImageView m_coinTossImage;
    private TextView m_titleTextView;
    private TextView m_wonTextView;
    private TextView m_lostTextView;
    private TextView m_resultTextView;

    /**
     Creates the CoinTossActivity and sets the layout, along with the event handlers.
     @param savedInstanceState A Bundle object, that is used when the activity is being restored from a previous state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cointoss);

        //Initialize the private variables.
        m_intent = getIntent();
        m_currentRound = (Round) m_intent.getSerializableExtra("round");
        m_headsButton = findViewById(R.id.headsButton);
        m_tailsButton = findViewById(R.id.tailsButton);
        m_continueButton = findViewById(R.id.continueButton);
        m_headsImage = findViewById(R.id.imageViewHeads);
        m_tailsImage = findViewById(R.id.imageViewTails);
        m_coinTossImage = findViewById(R.id.imageViewCoinToss);
        m_titleTextView = findViewById(R.id.coinTossTextView);
        m_resultTextView = findViewById(R.id.textViewResult);
        m_wonTextView = findViewById(R.id.wonTextView);
        m_lostTextView = findViewById(R.id.lostTextView);

        //Set all of the onClickListeners for the buttons.
        //Heads button onClickListener.
        m_headsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CoinToss(HEADS);
            }
        });

        //Tails button onClickListener.
        m_tailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CoinToss(TAILS);
            }
        });

        //Continue button onClickListener.
        m_continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Pass the round object with the newly set first player to the RoundViewActivity.
                m_intent = new Intent(getApplicationContext(), RoundViewActivity.class);
                m_intent.putExtra("round", m_currentRound);
                startActivity(m_intent);
            }
        });
    }

    /**
     Simulates a coin toss and sets the first player of the round based on the results of the coin toss.
     @param choice An integer, representing the Human player's call of the coin toss (0 for Heads, 1 for Tails).
     Assistance Received: https://developer.android.com/reference/android/transition/Fade
                          https://stackoverflow.com/questions/43952275/transitionmanager-begindelayedtransition-doesnt-animate-scaling
                          https://developer.android.com/develop/ui/views/animations/transitions
     */
    private void CoinToss(int choice) {
        //Fade object to fade widgets in and out. The animation takes 1000 milliseconds or 1 second.
        Fade fade = (Fade) new Fade().setDuration(1000);
        ViewGroup parentViewGroup = (ViewGroup) m_headsButton.getParent();

        //Fade the necessary widgets out, so the coin can be "flipped".
        TransitionManager.beginDelayedTransition(parentViewGroup, fade);
        m_headsButton.setVisibility(View.INVISIBLE);
        m_tailsButton.setVisibility(View.INVISIBLE);
        m_headsImage.setVisibility(View.INVISIBLE);
        m_coinTossImage.setVisibility(View.INVISIBLE);
        m_titleTextView.setVisibility(View.INVISIBLE);

        //Determine if the user won or lost the coin toss.
        Random rand = new Random();
        int coin = rand.nextInt(2);

        //The human won the coin toss.
        if (coin == choice)
        {
            m_resultTextView.setText(getString(R.string.first_human));
            m_wonTextView.setVisibility(View.VISIBLE);
            m_currentRound.SetHumanFirst();
        }
        //The computer won the coin toss.
        else
        {
            m_resultTextView.setText(getString(R.string.first_computer));
            m_lostTextView.setVisibility(View.VISIBLE);
            m_currentRound.SetComputerFirst();
        }

        //Fade the correct image in, and explain to the user if they won or lost.
        TransitionManager.beginDelayedTransition(parentViewGroup, fade);
        if (coin == 0)
        {
            m_headsImage.setVisibility(View.VISIBLE);

        }
        else
        {
            m_tailsImage.setVisibility(View.VISIBLE);
        }

        m_resultTextView.setVisibility(View.VISIBLE);
        m_continueButton.setVisibility(View.VISIBLE);
    }
}