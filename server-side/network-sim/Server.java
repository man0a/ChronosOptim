/* Prakhar Sahay 10/15/2015


*/

import java.util.*;

public class Server{
	Database db;
	boolean online;
	ArrayList<Device> devices=new ArrayList<Device>();

	public void Server(){
		db=new Database();
	}

	// public void broadcast(Event e){
	// 	db.addBroadcast(e);
	// 	for(){

	// 	}
	// }

	public void send(ArrayList<Event> eventBuffer){
		for(Event event:eventBuffer){
			dbRow=db.addEvent(event);
			if(event.broadcasting){
				broadcast(event);
			}
		}
	}

	public void connect(Device d){
		devices.add(d);
	}
}