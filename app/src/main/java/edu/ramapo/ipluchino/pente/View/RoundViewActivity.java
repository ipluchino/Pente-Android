package edu.ramapo.ipluchino.pente.View;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.view.MotionEvent;
import edu.ramapo.ipluchino.pente.Model.Round;
import edu.ramapo.ipluchino.pente.Model.StrategyConstants;

import edu.ramapo.ipluchino.pente.R;

public class RoundViewActivity extends AppCompatActivity {

    private Round m_round;
    private ScaleGestureDetector m_scaleGestureDetector;
    private float m_scaleFactor = 1.0f;
    private GridLayout m_buttonGridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roundview);

        //Initialize the round model.
        m_round = new Round();
        m_buttonGridLayout = findViewById(R.id.buttonsGridLayout);

        // Initialize the ScaleGestureDetector
        //m_scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        //Initialize the board
        InitializeBoardButtons();

        //SET LISTENERS HERE.

    }

    /*
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        m_scaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    //https://medium.com/quick-code/pinch-to-zoom-with-multi-touch-gestures-in-android-d6392e4bf52d
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            m_scaleFactor *= scaleGestureDetector.getScaleFactor();
            m_scaleFactor = Math.max(1.0f, Math.min(m_scaleFactor, 10.0f));
            m_buttonGridLayout.setScaleX(m_scaleFactor);
            m_buttonGridLayout.setScaleY(m_scaleFactor);
            return true;
        }
    }
     */

    private void InitializeBoardButtons()
    {

        for(int i = 0; i < 19; i++)
        {
            for(int j = 0; j < 19; j++)
            {
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                Button b = new Button(this);

                // Convert dp to pixels
                float scale = getResources().getDisplayMetrics().density;

                b.setText("(" + Integer.toString(i) + ", " + Integer.toString(j) + ")");
                b.setTextColor(Color.BLUE);
                b.setBackgroundColor(Color.WHITE);
                params.rowSpec= GridLayout.spec(i);
                params.columnSpec= GridLayout.spec(j);

                int buttonWidthDp = 380/19; // Change this to your desired dp value
                int buttonWidthPixels = (int) (buttonWidthDp * scale + 0.5f);
                int buttonHeightDP = 380/19;
                int buttonHeightPixels = (int) (buttonHeightDP * scale + 0.5f);
                int buttonWidth = buttonWidthPixels;
                int buttonHeight = buttonHeightPixels;

                //https://stackoverflow.com/questions/7815689/how-do-you-obtain-a-drawable-object-from-a-resource-id-in-android-package
                //https://stackoverflow.com/questions/7690416/android-border-for-button
                b.setBackground(getResources().getDrawable(R.drawable.buttonborder));

                params.width = buttonWidth;
                params.height = buttonHeight;

                m_buttonGridLayout.addView(b, params);


            }
        }
    }

    private String createButtonID(int row, int col) {
        char columnLabel = (char) ('A' + col);
        int reversedRow = 19 - row;
        return columnLabel + String.valueOf(reversedRow);
    }
}
