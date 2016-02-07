/* Prakhar Sahay 10/15/2015

A single user device, able to add(event), connect(), disconnect().
It implements uponSync() for SyncBuffer.
*/

import java.util.*;

public class Device{
	private SyncBuffer buffer;
	public ArrayList<Action> localSchedule;
	// private User user;
	private String userID;

	public Device(){
		localSchedule=new ArrayList<Action>();
		buffer=new SyncBuffer(this);
	}

	public void send(String data){
		// App user creates a new event.
		Action action=new Action(data,userID);
		localSchedule.add(action);
		buffer.add(action);
	}

	public void connect(){
		// App user gains Internet connection.
		buffer.connected=true;
		buffer.sync();
	}

	public void disconnect(){
		// App user loses Internet connection.
		buffer.connected=false;
	}

	public void uponSync(String s){
		// interface for SyncBuffer
		// called when synced
		if(userID==null){
			userID=s;
		}
	}

	public String getID(){
		return userID;
	}
}
