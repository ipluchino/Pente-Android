package edu.ramapo.ipluchino.pente.View;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.ramapo.ipluchino.pente.Model.Round;

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
        InitializeBoard();

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

    private void InitializeBoard()
    {
        //Create the column and row headers
        CreateHeaders();

        //Initialize the individual buttons
        for(int i = 0; i < 19; i++)
        {
            for(int j = 0; j < 19; j++)
            {
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                Button button = new Button(this);

                button.setTextColor(Color.BLUE);
                button.setBackgroundColor(Color.WHITE);
                button.setPadding(0, 0, 0, 0);

                //https://stackoverflow.com/questions/7815689/how-do-you-obtain-a-drawable-object-from-a-resource-id-in-android-package
                //https://stackoverflow.com/questions/7690416/android-border-for-button
                button.setBackground(getResources().getDrawable(R.drawable.blackborder));

                //Note: The row and column are incremented by 1 because of the row and column headers.
                params.rowSpec= GridLayout.spec(i+1);
                params.columnSpec= GridLayout.spec(j+1);
                params.width = GenerateCellSize(380/20, this);
                params.height = GenerateCellSize(380/20, this);
                params.setMargins(0, 0, 0, 0);

                m_buttonGridLayout.addView(button, params);
            }
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
            colHeader.setBackground(getResources().getDrawable(R.drawable.blackborder));
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
            rowHeader.setBackground(getResources().getDrawable(R.drawable.blackborder));
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


    private String createButtonID(int row, int col) {
        char columnLabel = (char) ('A' + col);
        int reversedRow = 19 - row;
        return columnLabel + String.valueOf(reversedRow);
    }
}
