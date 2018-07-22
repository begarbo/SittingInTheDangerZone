package edu.neu.madcourse.michellelee.dangerzone.realtimeDatabase.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    private String username;
    private String title;
    private String lastPlayed;
    private String lastEncounter;
    private String lastOutcome;

    public User(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String title, String lastPlayed, String lastEncounter, String lastOutcome){
        this.username = username;
        this.title = title;
        this.lastPlayed = lastPlayed;
        this.lastEncounter = lastEncounter;
        this.lastOutcome = lastOutcome;
    }

    public String getUsername() {
        return username;
    }

    public String getTitle() {
        return title;
    }

    public String getLastPlayed() {
        return lastPlayed;
    }

    public String getLastEncounter() {
        return lastEncounter;
    }

    public String getLastOutcome() {
        return lastOutcome;
    }
}