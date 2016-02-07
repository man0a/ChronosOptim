package com.example.dewartan.chronosoptim;

import com.parse.ParseInstallation;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Prakhar on 12/5/2015.
 */
public class SyncBuffer {
    private LinkedList<String> buffer;
    private ClientDevice device;

    private boolean waitingForServer=false;
    private String identifier;

    public SyncBuffer(ClientDevice device){
        this.device=device;
        device.coverActions(new LinkedList<String>());
        buffer=device.recoverActions();
        identifier="client="+device.getLocalId();
        sync();
    }

    public void send(String action){
        buffer.offer(identifier + action);
        sync();
    }

    public void sync(){
        if(buffer.isEmpty() || waitingForServer){
            return;
        }
        waitingForServer=true;
        ServerPing ping=new ServerPing(this);
        ping.execute(new ArrayList<>(buffer));
    }

    public void uponSync(ArrayList<String> responses){
        if(responses==null){
            return;
        }
        for(String response:responses){
            buffer.poll();// request has response, drop it
            if(response.startsWith("+user:")){
                device.setLocalId(response.substring(6));
                identifier="client="+response.substring(6);

                String installId=ParseInstallation.getCurrentInstallation().getObjectId();
                send("&x=installU&id="+installId);
            }

            device.uponSync(response);

        }

        save();
        waitingForServer=false;
        sync();
    }

    public void save(){
        device.coverActions(buffer);
    }
}
