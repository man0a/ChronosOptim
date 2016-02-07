package com.example.dewartan.chronosoptim;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;

import com.parse.ParsePushBroadcastReceiver;


/**
 * Created by Prakhar on 12/23/2015.
 */
public class PushReceiver extends ParsePushBroadcastReceiver{

    @Override
    protected Notification getNotification(Context context, Intent intent) {
        // deactivate standard notification if invitation
        if(isInvite(intent)){
            return null;
        }
        return super.getNotification(context,intent);
    }


    @Override
    public void onPushReceive(Context context,Intent intent){
        super.onPushReceive(context, intent);
        // if invitation, build new notification
        if(isInvite(intent)){
            String[] data=parse(intent);
            new Notifier(context).notify(data[0],data[1]);
        }
    }

    private boolean isInvite(Intent intent){
        return parse(intent)[0].endsWith("invited you");
    }


    private String[] parse(Intent intent){
        String data=intent.getExtras().getString("com.parse.Data");
        String[] parts=data.substring(1,data.length()-1).split(",");
        String alert=parts[0].substring(9,parts[0].length()-1);
        String title=parts[2].substring(9,parts[2].length()-1);
        return new String[]{title,alert};
    }
}
