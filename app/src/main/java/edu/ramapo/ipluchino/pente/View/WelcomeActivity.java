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
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

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
        //https://www.geeksforgeeks.org/android-how-to-request-permissions-in-android-application/#
        //https://stackoverflow.com/questions/5527764/get-application-directory
        //https://stackoverflow.com/questions/36496323/i-want-to-create-a-new-directory-in-dynamic-way-in-my-storage
        m_loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Request the permission to read external storage, if the user has not already granted it.
                if (ActivityCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(WelcomeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION);
                }

                File saveLocation = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/Pente");

                //Create the directory if it does not exist already exist.
                if (!saveLocation.exists()) {
                    saveLocation.mkdir();
                }

                //Obtain all of the files in the directory.
                File[] fileList = saveLocation.listFiles();

                //Add all of the file name in the directory into a vector.
                Vector<String> fileNames = new Vector<String>();
                for (File file : fileList)
                {
                    fileNames.add(file.getName());
                }

                //Display all of the saved files and let the user choose which file they want to load from.
                DisplaySavedFiles(fileNames);
            }
        });
    }

    //https://stackoverflow.com/questions/9157887/android-how-to-use-spinner-in-an-alertdialog
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

    //https://stackoverflow.com/questions/9157887/android-how-to-use-spinner-in-an-alertdialog
    private void DisplaySavedFiles(Vector<String> a_files)
    {
        ArrayAdapter<String> fileListAdapter = new ArrayAdapter<String>(WelcomeActivity.this, android.R.layout.simple_spinner_dropdown_item, a_files);

        //Create the spinner that will be used in the alert dialog.
        Spinner fileSpinner = new Spinner(WelcomeActivity.this);
        fileSpinner.setAdapter(fileListAdapter);

        //Create the alert dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
        builder.setTitle("Choose a file to load from!");
        builder.setView(fileSpinner);

        //OK button to clear the alert dialog.
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Get the choice from the user.
                String choice = (String) fileSpinner.getSelectedItem();
                File chosenFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/Pente/" + choice);

                //Create an input stream from the File object.
                Uri fileUri = Uri.fromFile(chosenFile);
                try {
                    InputStream inputStream = getContentResolver().openInputStream(fileUri);

                    if (inputStream != null)
                    {
                        //If the file could be opened as an input stream, load the data into the round object.
                        Round loadedRound = new Round();
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
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            }
        });

        //Cancel button to cancel the file loading process.
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //No need to do anything here.
            }
        });

        //Display the alert dialog.
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}