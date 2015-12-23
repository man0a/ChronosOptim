package com.example.dewartan.chronosoptim;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

/**
 * Created by Prakhar on 12/23/2015.
 */
public class PushReceiver extends ParsePushBroadcastReceiver{

    @Override
    public void onPushReceive(Context context,Intent intent){
        Notification notification=getNotification(context,intent);
        Log.w("here", "push receiver "+notification.toString());
    }
}
