package edu.ramapo.ipluchino.pente.View;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.transition.Fade;
import android.transition.TransitionManager;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import edu.ramapo.ipluchino.pente.R;

public class CoinTossActivity extends AppCompatActivity {
    //Private members
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cointoss);

        //Initialize the private variables.
        m_headsButton = findViewById(R.id.headsButton);
        m_tailsButton = findViewById(R.id.tailsButton);
        m_continueButton = findViewById(R.id.continueButton);
        m_headsImage = findViewById(R.id.imageViewHeads);
        m_tailsImage = findViewById(R.id.imageViewTails);
        m_coinTossImage = findViewById(R.id.imageViewCoinToss);
        m_titleTextView = findViewById(R.id.textViewCoinToss);
        m_resultTextView = findViewById(R.id.textViewResult);
        m_wonTextView = findViewById(R.id.textViewWon);
        m_lostTextView = findViewById(R.id.textViewLost);

        //Set onClick listeners.
        m_headsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("myTag", "Heads was clicked");
                CoinToss(0);
            }
        });

        m_tailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("myTag", "Tails was clicked");
                CoinToss(1);
            }
        });

        m_continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("myTag", "Continue was clicked");
                Intent intent = new Intent(getApplicationContext(), RoundViewActivity.class);
                startActivity(intent);
            }
        });
    }

    //Runs the coin toss and displays the result.
    //https://developer.android.com/reference/android/transition/Fade
    //https://stackoverflow.com/questions/43952275/transitionmanager-begindelayedtransition-doesnt-animate-scaling
    //https://developer.android.com/develop/ui/views/animations/transitions

    private void CoinToss(int choice) {
        //Fade object to fade widgets in and out. The animation takes 1000 milliseconds.
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

        if (coin == choice)
        {
            m_resultTextView.setText("You will go first because you called the toss correctly!");
            m_wonTextView.setVisibility(View.VISIBLE);
        }
        else
        {
            m_resultTextView.setText("The computer will go first because you called the toss incorrectly!");
            m_lostTextView.setVisibility(View.VISIBLE);
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