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
        SharedPreferences.Editor editor=prefs().edit();
        editor.putString("userId", userId);
        editor.putString("alias", "user"+userId);
        editor.commit();
    }
    public void setLocalAlias(String alias){
        SharedPreferences.Editor editor=prefs().edit();
        editor.putString("alias", alias);
        editor.commit();
    }
    public String getLocalId(){
        return prefs().getString("userId", "!");
    }
    public String getLocalAlias(){
        return prefs().getString("alias", "!!");
    }
    private SharedPreferences prefs(){
        return PreferenceManager.getDefaultSharedPreferences(this);
    }
}