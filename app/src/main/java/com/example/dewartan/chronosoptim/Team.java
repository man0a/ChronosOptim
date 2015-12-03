package com.example.dewartan.chronosoptim;

import android.content.ContentValues;

/**
 * Created by dewartan on 11/22/15.
 */
public class Team {

    private String name;
    private String description;

    public Team(String name, String description) {
        this.name = name;
        this.description = description;
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ContentValues content(){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("description", description);
        return contentValues;
    }
}
