package com.example.dewartan.chronosoptim;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dewartan on 10/20/15.
 */
public class Event implements Parcelable {
    private String location, date, startTime, endTime, description, title;

    public Event(String location, String date, String startTime, String endTime, String description, String title) {
        this.location= location;
        this.date = date;
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

    public void changeDescription(String description) {
        this.description = description;
    }


    public void changeDate(String date) {
        this.date = date;
    }




    public void changeLocation(String location) {
        this.location = location;
    }



    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getSubtitle() {
        return startTime + "  " + description;
    }

//    public String toJSON() {
//        return
//                "{location:\'" + getLocation() +
//                "\',title:\'"+ getTitle() +
//                "\',time:\'" +getTime()+
//                "\',date:\'"+ getDate()+
//                "\',description:\'"+ getDescription()+"}";
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

    }
}


