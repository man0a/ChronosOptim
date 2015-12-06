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
    private String identifier="client=!&";

    public SyncBuffer(ClientDevice device){
        this.device=device;
        buffer=device.recoverActions();
        if(device.getLocalId().equals("")){
            buffer.add(0,"");
        }else{
            identifier="client="+device.getLocalId()+"&";
        }
        if(buffer.size()==0){
            synced=false;
            sync();
        }
    }

    public void send(String action){
        buffer.add(identifier+action);
        synced=false;
        sync();
    }
    public void sync(){
        if(synced || waitingForServer){
            return;
        }
        Log.w("sync", "sync");
        waitingForServer=true;
        ServerPing ping=new ServerPing(this);
        ping.execute(buffer);
    }

    public void uponSync(ArrayList<String> responses,ArrayList<String> requests){
        if(responses==null || responses.size()==0){
            return;
        }
        Log.w("here",""+responses.size());
        for(String response:responses){
            if(response.startsWith("+user:")){
                device.setLocalId(response.substring(6));
                identifier="client="+response.substring(6)+"&";
            }
            device.uponSync(response);

        }
        if(requests.equals(buffer)){
            buffer.clear();
            waitingForServer=false;
            synced=true;
        }else{
            sync();
        }
    }

    public void save(){
        device.coverActions(buffer);
    }
}
