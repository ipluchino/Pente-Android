package edu.ramapo.ipluchino.pente.View;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import edu.ramapo.ipluchino.pente.Model.Round;
import edu.ramapo.ipluchino.pente.R;

public class WelcomeActivity extends AppCompatActivity {
    //Constants.
    private final int READ_PERMISSION = 100;

    //Private variables.
    private Button m_newButton;
    private Button m_loadButton;

    /**
     Creates the WelcomeActivity and sets the layout, along with the event handlers.
     @param savedInstanceState A Bundle object, that is used when the activity is being restored from a previous state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //Initialize the private variables.
        m_newButton = findViewById(R.id.newGameButton);
        m_loadButton = findViewById(R.id.loadGameButton);

        //Set all of the onClickListeners for the buttons.
        //New Game button onClickListener.
        //Assistance Received: https://stackoverflow.com/questions/20241857/android-intent-cannot-resolve-constructor
        m_newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Switch to the Coin Toss state, since new games always begin with a coin toss.
                Intent intent = new Intent(getApplicationContext(), CoinTossActivity.class);

                //Pass a fresh round object to the Coin Toss Activity.
                intent.putExtra("round", new Round());
                startActivity(intent);
            }
        });

        //Load Game button onClickListener.
        //Assistance Received: https://stackoverflow.com/questions/20063957/read-file-in-android-file-permission-denied
        //                     https://www.geeksforgeeks.org/android-how-to-request-permissions-in-android-application/#
        //                     https://stackoverflow.com/questions/5527764/get-application-directory
        //                     https://stackoverflow.com/questions/36496323/i-want-to-create-a-new-directory-in-dynamic-way-in-my-storage
        m_loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Request the permission to read external storage, if the user has not already granted it.
                if (ActivityCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(WelcomeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION);
                }

                //Directory where save files are located.
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
    /**
     Displays an alert dialog explaining to the user that the file they chose to load the tournament from was invalid.
     */
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
    /**
     Displays all of the save files to load from as a spinner in an alert dialog, and allows the user to choose one.
     @param a_files A Vector of strings, representing all of the names of the save files found in the save directory.
     */
    private void DisplaySavedFiles(Vector<String> a_files)
    {
        //Create the alert dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
        builder.setTitle("Choose Load File");

        //Let the user know there are no saved game files if none exist yet. Otherwise, display the spinner.
        if (a_files.isEmpty())
        {
            builder.setMessage("No saved games found.");

            //OK button to clear the alert dialog.
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //No need to do anything here.
                }
            });
        }
        else
        {
            ArrayAdapter<String> fileListAdapter = new ArrayAdapter<String>(WelcomeActivity.this, android.R.layout.simple_spinner_dropdown_item, a_files);

            //Create the spinner that will be used in the alert dialog.
            Spinner fileSpinner = new Spinner(WelcomeActivity.this);
            fileSpinner.setAdapter(fileListAdapter);

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

        }

        //Display the alert dialog.
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}