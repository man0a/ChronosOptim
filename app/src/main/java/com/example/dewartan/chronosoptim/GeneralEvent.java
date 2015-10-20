package com.example.dewartan.chronosoptim;

/**
 * Created by dewartan on 10/20/15.
 */
public class GeneralEvent implements Event {
    private String location, date, time, description, title;

    public GeneralEvent(String location, String date, String time, String description, String title) {
        this.location= location;
        this.date = date;
        this.time = time;
        this.description = description;
        this.title = title;

    }

    public String getTitle() {
        return title;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    @Override
    public void changeDate(String date) {
        this.date = date;
    }

    @Override
    public void changeTime(String time) {
        this.time = time;

    }

    @Override
    public void changeLocation(String location) {
        this.location = location;
    }

    @Override
    public String getTime() {
        return time;
    }

    @Override
    public String getDate() {
        return date;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public String getSubtitle() {
        return time + "  " + description;
    }
}
