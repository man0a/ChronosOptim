package com.example.dewartan.chronosoptim;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import java.util.LinkedList;

public abstract class ClientDevice extends AppCompatActivity{
    abstract void uponSync(String response);
    abstract void coverActions(LinkedList<String> actions);
    abstract LinkedList<String> recoverActions();

    public void setLocalId(String userId){
        SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString("userId", userId);
        editor.commit();
    }
    public String getLocalId(){
        SharedPreferences settings=PreferenceManager.getDefaultSharedPreferences(this);
        return settings.getString("userId","!");
    }
}