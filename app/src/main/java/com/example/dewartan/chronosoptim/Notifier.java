package com.example.dewartan.chronosoptim;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Prakhar on 12/13/2015.
 */
public class Notifier {
    private Context context;
    private int autoInc;

    public Notifier(Context context){
        this.context=context;
        this.autoInc=0;
    }

    public void notify(String title,String text){
        NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_blur_on_black_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.notif_blue))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentTitle(title)
                .setContentText(text);

        // play sound or vibrate or stay silent
        int mode=((AudioManager) context.getSystemService(context.AUDIO_SERVICE)).getRingerMode();
        if(mode==AudioManager.RINGER_MODE_NORMAL){
            try {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
                r.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(mode==AudioManager.RINGER_MODE_VIBRATE){
            mBuilder.setVibrate(new long[]{0,125,125,125});
        }

        // upon tapping notification
        Intent resultIntent=new Intent(context,MainActivity.class);
        resultIntent.putExtra("event",getEvent(title,text));

        TaskStackBuilder stackBuilder=TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent=stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager notifier=(NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        int id=autoInc++;
        notifier.notify(id, mBuilder.build());
    }

    private Event getEvent(String title,String text){

        String name=text.substring(text.indexOf(">")+1);
        String parts[]=text.split(" ");
        String date=parts[0];
        parts=parts[1].split("-");
        Event event=new Event(name,"",date,parts[0],parts[1],title,"");
        return event;
    }

}