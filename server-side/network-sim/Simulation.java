/* Prakhar Sahay 10/15/2015


*/

import java.util.*;

public class Simulation{
	public static final int NUM_DEVICES=1;

	public static void main(String[] args){

		Server server=new Server();
		ArrayList<Device> devices=new ArrayList<Device>();
		for(int i=0;i<NUM_DEVICES;i++){
			devices.add(new Device());
		}

		devices.get(0).connect(server);
	}
}