package com.example.dewartan.chronosoptim;


import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dewartan on 10/20/15.
 */
public class Event implements Parcelable {
    private String id, location, date, startTime, endTime, description, title, subtitle;

    public Event(String id, String title, String location, String date, String startTime, String endTime, String description, String subtitle) {
        this.id=id;
        this.title = title;
        this.location= location;
        this.date = date;
        this.subtitle = subtitle;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
    }
    public Event(String title, String location, String date, String startTime, String endTime, String description, String subtitle) {
        this("",title,location,date,startTime,endTime,description,subtitle);
    }


    protected Event(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.date = in.readString();
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.location = in.readString();
        this.subtitle = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public ContentValues content(){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",id);
        contentValues.put("title", title);
        contentValues.put("description", description);
        contentValues.put("eventdate", date);
        contentValues.put("starttime", startTime);
        contentValues.put("endtime", endTime);
        contentValues.put("location", location);
        contentValues.put("subtitle", subtitle);
        return contentValues;
    }

    public String getId(){
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDate() {
        return date;
    }
    public String getStartTime() {return startTime;}
    public String getEndTime(){return endTime;}
    public String getLocation() {
        return location;
    }
    public String getDescription() {
        return description;
    }
    public String getSubtitle() {
        return subtitle;
    }
    public String getSubtext() {
        return startTime + "  " + subtitle;
    }

    public void setId(String id) {
        this.id=id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(date);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(location);
        dest.writeString(subtitle);
    }

    public String postForm(){
        return String.format("id=%s&title=%s&description=%s&date=%s&startTime=%s&endTime=%s&location=%s&subtitle=%s",
            id,title,description,date,startTime,endTime,location,subtitle);
    }
}


