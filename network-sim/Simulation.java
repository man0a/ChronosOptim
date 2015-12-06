/* Prakhar Sahay 10/15/2015


*/

import java.util.*;

public class Simulation{
	public static final int NUM_DEVICES=50;
	public static ServerDB server;


	public static void main(String[] args) throws InterruptedException{
		// create server and devices
		server=new ServerDB();
		ArrayList<Device> devices=new ArrayList<Device>();
		for(int i=0;i<NUM_DEVICES;i++){
			devices.add(new Device());
		}
		// randomly add events
		Random r=new Random();

		int numEvents=0;
		for(int j=0;j<Math.pow(10,6);j++){
			// choose random device
			Device d=devices.get(r.nextInt(NUM_DEVICES));
			int type=r.nextInt(4);
			// choose random action
			switch(type){
				case 0:d.send("addE event"+numEvents++);break;
				case 1:d.connect();break;
				case 2:case 3:d.disconnect();break;
			}
			if(j%Math.pow(10,4)==0){
				System.out.println(j/Math.pow(10,4)+"%");
			}
		}
		for(Device d:devices){
			d.connect();
		}
		System.out.println("numEvents: "+numEvents);
		// dump database tables
		server.dump();
	}

	public static ServerDB getServer(){
		return server;
	}
}