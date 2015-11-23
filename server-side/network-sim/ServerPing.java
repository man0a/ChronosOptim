/* Prakhar Sahay 11/08/2015

A simulation of the Android ServerPing<AsyncTask.
*/

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerPing implements Runnable{
	private ServerDB sdb;
	private SyncBuffer syncBuffer;
	private ArrayList<Action> actionBuffer;
	private ArrayList<String> result;

	public ServerPing(SyncBuffer syncBuffer,ArrayList<Action> actionBuffer){
		this.syncBuffer=syncBuffer;
		this.actionBuffer=actionBuffer;
		this.sdb=Simulation.getServer();
	}

	public void execute(){
		try{
			Thread t=new Thread(this);
			t.start();
			t.join();
			onPostExecute(result);
		}catch(Exception e){
			// do nothing
			System.out.println("Bad join.");
		}
	}

	private ArrayList<String> doInBackground(){
		// hello, talk, bye
		return sdb.talk(actionBuffer,syncBuffer.getID());
	}

	private void onPostExecute(ArrayList<String> resp){
		syncBuffer.uponSync(resp,actionBuffer);
	}

	public void run(){
		result=doInBackground();
	}
}