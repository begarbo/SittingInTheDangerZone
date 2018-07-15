package edu.neu.madcourse.michellelee.dangerzone;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class UserProfileActivity extends AppCompatActivity {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize Shared Preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        // Get user information
        String name = preferences.getString("username", null);
        int level = preferences.getInt("level", -1);
        String currentTitle = preferences.getString("title", null);
        int minutesWalked = preferences.getInt("minutes walked", -1);
        int distanceWalked = preferences.getInt("distance walked", -1);
        int titles = preferences.getInt("# titles", -1);
        int achievements = preferences.getInt("# achievements", -1);

        // Hooking up achievements with adapters
        ArrayList<String> itemList = new ArrayList<String>();
        ArrayAdapter<String> titlesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemList);
        ListView titlesList = (ListView) findViewById(R.id.titles_list);
        titlesList.setAdapter(titlesAdapter);
//        titlesAdapter.add("title1");
        ArrayAdapter<String> achievementsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemList);
        ListView achievementsList = (ListView) findViewById(R.id.achievements_list);
        achievementsList.setAdapter(achievementsAdapter);
//        achievementsAdapter.add("title1");

        // TEST FOR TITLES AND ACHIEVEMENTS PROCESSING
        String test1 = "test 1";
        StringBuilder titlebuild = new StringBuilder(currentTitle);
        titlebuild.append(",");
        titlebuild.append(test1);
        editor.putString("titles", titlebuild.toString());
        editor.apply();

        String test2 = "test 2";
        StringBuilder achievebuild = new StringBuilder();
        achievebuild.append(test1);
        achievebuild.append(",");
        achievebuild.append(test2);
        editor.putString("achievements", achievebuild.toString());
        editor.apply();

        // Special processing for list of strings
        String titleList = preferences.getString("titles", null);
        String[] titleArray = titleList.split(","); // Get array of individual titles
        for (String eachTitle : titleArray) {
            titlesAdapter.add(eachTitle);   // Add each title to the list
        }
        String achievementList = preferences.getString("achievements", null);
        String[] achievementArray = achievementList.split(","); // Get array of individual achievements
        for (String eachAchievement : achievementArray) {
            achievementsAdapter.add(eachAchievement);   // Add each achievement to the list
        }

        // Hooking up text views and settings text from loaded user information
        TextView usernameView = (TextView) findViewById(R.id.user_name);
        usernameView.setText(name);
        TextView userLevelView = (TextView) findViewById(R.id.user_level);
        userLevelView.setText("Level " + Integer.toString(level));
        final TextView userTitleView = (TextView) findViewById(R.id.user_title);
        userTitleView.setText("Title: " + currentTitle);
        TextView minutesWalkedView = (TextView) findViewById(R.id.minutes_walked);
        minutesWalkedView.setText(Integer.toString(minutesWalked));
        TextView distancewalkedView = (TextView) findViewById(R.id.distance_walked);
        distancewalkedView.setText(Integer.toString(distanceWalked));
        TextView titlesEarned = (TextView) findViewById(R.id.titles_earned);
        titlesEarned.setText(Integer.toString(titles));
        TextView numberOfAchievementsView = (TextView) findViewById(R.id.achievements_earned);
        numberOfAchievementsView.setText(Integer.toString(achievements));

        // Clickable title selection
        // Making the title ListView clickable so that titles can be changed
        titlesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = ((TextView) view).getText().toString();
                userTitleView.setText("Title: " + selected);
                editor.putString("title", selected);
                editor.apply();
                Toast.makeText(UserProfileActivity.this,selected,Toast.LENGTH_LONG).show();
            }
        });


    }

}
