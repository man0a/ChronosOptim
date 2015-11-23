/* Prakhar Sahay 11/21/2015

SyncBuffer supports submit() called by Device and doThing() called by ServerPing.
*/

import java.util.*;

public class SyncBuffer{
	private ArrayList<Action> buffer;
	private Device device;
	private boolean waitingForServer=false;
	private boolean synced=true;
	public boolean connected=true;


	public SyncBuffer(Device device){
		this.device=device;
		buffer=new ArrayList<Action>();
		if(device.getID()==null){
			Action makeUser=new Action("addU",null);
			add(makeUser);
		}
	}

	public void add(Action action){
		buffer.add(action);
		synced=false;
		sync();
	}

	public void sync(){
		if(synced || waitingForServer || !connected){
			return;
		}
		waitingForServer=true;// flag stops other add's from entering critical section
		ServerPing ping=new ServerPing(this,buffer);
		ping.execute();
	}

	public void uponSync(ArrayList<String> resp,ArrayList<Action> results){
		for(String response:resp){
			device.uponSync(response);
		}
		if(results.equals(buffer)){
			buffer.clear();
			waitingForServer=false;
			synced=true;
		}else{
			sync();
		}
	}

	public String getID(){
		return device.getID();
	}
}