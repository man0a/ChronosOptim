package com.example.dewartan.chronosoptim;


import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by dewartan on 10/20/15.
 */
public class Event implements Parcelable {
    private String location, date, startTime, endTime, description, title, subtitle;

    public Event(String location, String date, String startTime, String endTime, String description, String title, String subtitle) {
        this.location= location;
        this.date = date;
        this.subtitle = subtitle;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.title = title;
    }

    protected Event(Parcel in) {
        this.location = in.readString();
        this.date = in.readString();
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.description = in.readString();
        this.title = in.readString();
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

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getDateDayOfWeek() {
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        Date temp = null;
        try {
            temp = df.parse(date);
        } catch (ParseException e) {

        }
        String dayOfWeek = new SimpleDateFormat("EEEE", Locale.US).format(temp);
        return dayOfWeek + ", " + date;
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
        return startTime + "  " + subtitle;
    }

    public String subtitle() {return subtitle;}

//    public String toJSON() {
//        return
//                "{location:\'" + getLocation() +
//                "\',title:\'"+ getTitle() +
//                "\',time:\'" +getTime()+
//                "\',date:\'"+ getDate()+
//                "\',description:\'"+ getDescription()+
//                 "\',subtitle:\'"+ subtitle()"}";
//    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(location);
        dest.writeString(date);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(description);
        dest.writeString(title);
        dest.writeString(subtitle);
    }
}


