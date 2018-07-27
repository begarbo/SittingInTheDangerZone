package edu.neu.madcourse.michellelee.dangerzone;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import edu.neu.madcourse.michellelee.dangerzone.realtimeDatabase.models.User;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private AlertDialog startDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Shared Preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        // Ask if this is the first startup of the app as we need to get the user name
        isInitialStartup();

        // Settings button
        View settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws RuntimeException {
                Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settingsIntent);
            }
        });

        // Walk introduction screen
        View walkIntroButton = findViewById(R.id.walk_button);
        walkIntroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws RuntimeException {
                Intent walkIntent = new Intent(getApplicationContext(), WalkIntro.class);
                startActivity(walkIntent);
            }
        });

        // User profile
        View userProfile = findViewById(R.id.profile_button);
        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws RuntimeException {
                Intent userProfileIntent = new Intent(getApplicationContext(), UserProfileActivity.class);
                startActivity(userProfileIntent);
            }
        });

        // Information and instructions button
        View instructionsButton = findViewById(R.id.instructions_button);
        instructionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws RuntimeException {
                Intent userProfileIntent = new Intent(getApplicationContext(), InformationInstructionsFragment.class);
                startActivity(userProfileIntent);
            }
        });

    }

    private void isInitialStartup() {
        // If this is the first time we are opening the app
        if (preferences.getBoolean("initial startup", true)) {
            // Dialog to ask for user name
            AlertDialog.Builder startBuilder = new AlertDialog.Builder(this);
            LayoutInflater startInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View dialogView = startInflater.inflate(R.layout.initial_start_dialog, null);     // Get dialog view
            startBuilder.setCancelable(false);
            startBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    startDialog.dismiss();
                } });
            startBuilder.setView(dialogView);    // Set view to initial start dialog

            // Set up username input
            final EditText usernameEntry = (EditText) dialogView.findViewById(R.id.username_input);
            // Button to submit username
            final Button enterUserName = (Button) dialogView.findViewById(R.id.enter_username);
            enterUserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                // Username information
                StringBuilder userName = new StringBuilder();
                userName.append(usernameEntry.getText());
                String userNameString = userName.toString();
                // Text view that shows what the user has submitted their name as
                TextView welcomeUser = (TextView) dialogView.findViewById(R.id.welcome_message);
                welcomeUser.setText("Welcome " + userNameString + ".");
                welcomeUser.setVisibility(View.VISIBLE);
                enterUserName.setVisibility(View.INVISIBLE);
                // Update shared preferences with default values for the user profile
                editor.putString("username", userNameString);
                editor.apply();
                editor.putInt("level", 1);
                editor.apply();
                editor.putInt("seconds walked", 0);
                editor.apply();
                editor.putInt("steps walked", 0);
                editor.apply();
                editor.putInt("# titles", 1);
                editor.apply();
                editor.putString("title", "Fresh Meat");
                editor.apply();
                editor.putString("title list", "Fresh Meat");
                editor.apply();
                editor.putInt("# achievements", 1);
                editor.apply();
                editor.putString("achievements", "Danger Seeker");
                editor.apply();
                editor.putInt("xp", 0);
                editor.apply();
                editor.putInt("personal best", 0);
                editor.apply();
                doDataAddToDb(userNameString, "Fresh Meat");
                String newTitleEarned = getResources().getString(R.string.new_title_earned);
                Toast.makeText(MainActivity.this,newTitleEarned,Toast.LENGTH_LONG).show();

                }
            });

            startDialog = startBuilder.show();

            // Update that app has been started up
            editor.putBoolean("initial startup", false);
            editor.apply();
        }
    }

    /**
     * Adds a brand new user to Firebase
     * @param userNameString the user name entered by the user
     * @param title the current title of the user
     */
    private void doDataAddToDb(String userNameString, String title) {
        // Get token for this app instance
        String uniqueID = idGenerator();
        editor.putString("uid", uniqueID);  // Add this ID to shared preferences
        editor.apply();

        // Get date last active (now)
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());

        // Create a map to hold the friends list
        Map<String, String> friendsList = new HashMap<String, String>();

        // Creating a new user for the database
        User newUser = new User(userNameString, title, date, "n/a", "n/a", uniqueID, friendsList);

        // Add new node in database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        myRef.child(uniqueID).setValue(newUser);
    }

    /**
     * Generates a (hopefully) random 10 character unique ID string
     * @return the user ID
     */
    private String idGenerator() {
        String alphabet= "abcdefghijklmnopqrstuvwxyz";
        String s = "";
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            char c = alphabet.charAt(random.nextInt(26));
            s+=c;
        }
        return s;
    }


}