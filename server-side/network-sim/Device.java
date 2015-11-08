/* Prakhar Sahay 10/15/2015


*/

import java.util.*;

public class Device{
	public Server server;
	public boolean waitingForServer;
	public ArrayList<Event> buffer=new ArrayList<Event>();
	public boolean synced=true;
	public ArrayList<Event> localSchedule=new ArrayList<Event>();

	public Device(){
		if(!buffer.isEmpty()){
			save();
		}
	}

	public void save(Event event){
		synced=false;
		buffer.add(event);
		if(server!=null){
			save();
		}
	}

	public void save(){
		if(synced || server==null || waitingForServer){
			return;
		}
		waitingForServer=true;
		do{
			server.send(buffer);
			ArrayList<Event> results=server.response(this);
			synced=confirm(results);
		}while(!synced || !buffer.isEmpty());

		waitingForServer=false;
	}

	private boolean confirm(ArrayList<Event> results){
		if(results.equals(buffer)){
			buffer.empty();
			return true;
		}
		return false;
	}

	public void connect(Server server){
		this.server=server;
		server.connect(this);
	}

	public void disconnect(){
		this.server=null;
	}
}
