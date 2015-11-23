package com.example.dewartan.chronosoptim;

/**
 * Created by dewartan on 11/22/15.
 */
public class Channel {

    private String name;
    private String description;

    public Channel(String name, String description) {
        this.name = name;
        this.description = description;
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
