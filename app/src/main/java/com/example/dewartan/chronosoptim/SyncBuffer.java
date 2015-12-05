package com.example.dewartan.chronosoptim;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Prakhar on 12/5/2015.
 */
public class SyncBuffer {
    private ArrayList<String> buffer;
    private ClientDevice device;

    private boolean waitingForServer=false;
    private boolean synced=true;

    public SyncBuffer(ClientDevice device){
        this.device=device;
        buffer=new ArrayList<>();
        Log.w("id","a"+device.getLocalId()+"A");
        if(device.getLocalId().equals("")){
            send("id=!");
        }else{
            send("id="+device.getLocalId());
        }
    }

    public void send(String action){
        buffer.add(action);
        synced=false;
        sync();
    }

    public void sync(){
        if(synced || waitingForServer){
            return;
        }
        Log.w("sync","sync");
        waitingForServer=true;
        ServerPing ping=new ServerPing(this);
        ping.execute(buffer);
    }

    public void uponSync(ArrayList<String> responses,ArrayList<String> actions){
        if(responses==null){
            Log.w("oh poo","oh dear!");
            return;
        }
        Log.w("here",""+responses.size());
        for(String response:responses){
            if(response.startsWith("+user:")){
                device.setLocalId(response.substring(6));
            }
            device.uponSync(response);

        }
        if(actions.equals(buffer)){
            buffer.clear();
            waitingForServer=false;
            synced=true;
        }else{
            sync();
        }
    }
}
