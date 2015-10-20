package com.example.dewartan.chronosoptim;

/**
 * Created by dewartan on 10/20/15.
 */
public interface Event {

    public void changeDate(String date);
    public void changeTime(String time);
    public void changeLocation(String location);
    public void changeDescription(String description);
    public String getTime();
    public String getDate();
    public String getLocation();
    public String getDescription();
    public String getTitle();

}
