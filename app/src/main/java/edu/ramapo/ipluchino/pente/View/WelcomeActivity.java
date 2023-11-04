package edu.ramapo.ipluchino.pente.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import edu.ramapo.ipluchino.pente.Model.Round;
import edu.ramapo.ipluchino.pente.R;

public class WelcomeActivity extends AppCompatActivity {
    //Private members
    private Button m_newButton;
    private Button m_loadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        m_newButton = findViewById(R.id.newGameButton);
        m_loadButton = findViewById(R.id.loadGameButton);

        //Set onClick listeners.
        m_newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("myTag", "New Game was clicked");

                //Switch to the Coin Toss state, since new games always begin with a coin toss.
                //https://stackoverflow.com/questions/20241857/android-intent-cannot-resolve-constructor
                Intent intent = new Intent(getApplicationContext(), CoinTossActivity.class);

                //Pass a fresh round object to the Coin Toss Activity.
                intent.putExtra("round", new Round());
                startActivity(intent);
            }
        });

        m_loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("myTag", "Load Game was clicked");
            }
        });

    }
}