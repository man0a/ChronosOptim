package com.example.dewartan.chronosoptim;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by dewartan on 11/22/15.
 */
public class Team implements Parcelable{

    private String id, name, description;
    private ArrayList<String> members;

    public Team(String id,String name,String description,String members) {
        this.id=id;
        this.name = name;
        this.description = description;
        setMembers(members);
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
        if(members.isEmpty()){
            return "";
        }
        String memberList=members.get(0);
        for(int i=1;i<members.size();i++){
            memberList+=","+members.get(i);
        }
        return memberList;
    }
    public int getSize(){
        return members.size()+1;
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
    public void setMembers(String memberList) {
        if(memberList.isEmpty()){
            this.members=new ArrayList<>();
        }else{
            this.members=new ArrayList<>(Arrays.asList(memberList.split(",")));
        }
    }


    public ContentValues content(){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("members", getMembers());
        return contentValues;

    }

    // Parcelable implementation boilerplate
    protected Team(Parcel in) {
        this.id=in.readString();
        this.name = in.readString();
        this.description = in.readString();
        setMembers(in.readString());
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
        dest.writeString(getMembers());
    }

    public String postForm(){
        return String.format("id=%s&name=%s&description=%s&members=%s",id,name,description,getMembers());
    }

    public boolean hasMember(String username){
        for(String member:members){
            if(member.equals(username)){
                return true;
            }
        }
        return false;
    }
    public void appendMember(String username){
        members.add(username);
    }
    public void removeMember(String username){
        for(int i=0;i<members.size();i++){
            if(members.get(i).equals(username)){
                members.remove(i);
                return;
            }
        }
    }
}
