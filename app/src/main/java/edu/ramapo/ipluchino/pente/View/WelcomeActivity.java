package edu.ramapo.ipluchino.pente.View;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import edu.ramapo.ipluchino.pente.Model.Round;
import edu.ramapo.ipluchino.pente.R;

public class WelcomeActivity extends AppCompatActivity {
    //Constants
    private static final int PICK_TEXT_FILE = 1;
    private final int READ_PERMISSION = 100;

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

        //https://stackoverflow.com/questions/20063957/read-file-in-android-file-permission-denied
        //https://stackoverflow.com/questions/30789116/implementing-a-file-picker-in-android-and-copying-the-selected-file-to-another-l
        //https://developer.android.com/training/data-storage/shared/documents-files
        //https://developer.android.com/training/permissions/requesting#java
        //https://www.geeksforgeeks.org/android-how-to-request-permissions-in-android-application/#
        m_loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("myTag", "Load Game was clicked");

                //Request the permission to read external storage, if the user has not already granted it.
                if (ActivityCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(WelcomeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION);
                }

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("text/plain");
                startActivityForResult(intent, PICK_TEXT_FILE);

            }
        });
    }

    //https://stackoverflow.com/questions/2975197/convert-file-uri-to-file-in-android
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);

        if (requestCode == PICK_TEXT_FILE && resultCode == Activity.RESULT_OK)
        {
            Uri uri;

            if (resultData != null)
            {
                //Obtain the uri of the file that was chosen by the user.
                uri = resultData.getData();
                Round loadedRound = new Round();

                try {
                    //Attempt to open an input stream for the file.
                    InputStream inputStream = getContentResolver().openInputStream(uri);

                    if (inputStream != null)
                    {
                        //If the file could be opened as an input stream, load the data into the round object.
                        boolean success = loadedRound.LoadGameData(inputStream);

                        if (success)
                        {
                            //Attach the loaded round to the intent and go to the RoundViewActivity.
                            Intent intent = new Intent(getApplicationContext(), RoundViewActivity.class);
                            intent.putExtra("round", loadedRound);
                            intent.putExtra("loadedFromFile", true);
                            startActivity(intent);
                        }
                        else
                        {
                            DisplayInvalidFile();
                        }
                    }

                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void DisplayInvalidFile()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
        builder.setTitle("Invalid file!");

        builder.setMessage("The file you selected to load is considered invalid. Please ensure the file is in the correct format and try again.");

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