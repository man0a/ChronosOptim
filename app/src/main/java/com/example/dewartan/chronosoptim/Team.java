package com.example.dewartan.chronosoptim;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dewartan on 11/22/15.
 */
public class Team implements Parcelable{

    private String id, name, description, members;

    public Team(String id,String name,String description,String members) {
        this.id=id;
        this.name = name;
        this.description = description;
        this.members=members;
    }

    public Team(String name,String description,String members){
        this("",name,description,members);
    }

    public String getId(){
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getMembers() {
        return members;
    }
    public void setId(String id){
        this.id=id;
    }
    public void setName(String name) {
        this.name=name;
    }
    public void setDescription(String description) {
        this.description=description;
    }
    public void setMembers(String members) {
        this.members=members;
    }


    public ContentValues content(){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("members", members);
        return contentValues;

    }

    // Parcelable implementation boilerplate
    protected Team(Parcel in) {
        this.id=in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.members = in.readString();
    }

    public static final Creator<Team> CREATOR = new Creator<Team>() {
        @Override
        public Team createFromParcel(Parcel in) {
            return new Team(in);
        }

        @Override
        public Team[] newArray(int size) {
            return new Team[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(members);
    }

    public String postForm(){
        return String.format("id=%s&name=%s&description=%s&members=%s",id,name,description,members);
    }
}
