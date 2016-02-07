package com.example.dewartan.chronosoptim;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParseUser;


public class StarterApplication extends Application {

    private Event event;
    private Team team;

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(this, "z0r0EqW0wMp66JIyWd6HO1SeoO5FHFg1LDftvSrh", "YoU8xazvVrABzpcSU0zg7w3gxNEUhLlQMdhKjDb3");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }

    public Team getTeam(){
        return team;
    }
    public Event getEvent(){
        return event;
    }
    public void setPair(Event event,Team team){
        this.event=event;
        this.team=team;
    }

}
