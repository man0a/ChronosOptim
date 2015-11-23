/* Prakhar Sahay 11/08/2015

The abstraction of the backend solution entity, both server and database.
*/

import java.util.*;

public class ServerDB{

	ArrayList<User> userTable=new ArrayList<User>();
	ArrayList<String> eventTable=new ArrayList<String>();
	ArrayList<String> broadcastTable=new ArrayList<String>();

	public ServerDB(){
		// enter the users
		// for(Device d:devices){
		// 	userTable.add(new User(d));
		// }
	}
	public ArrayList<String> talk(ArrayList<Action> buffer,String userID){
		// called by ServerPing
		ArrayList<String> response=new ArrayList<String>();

		for(Action action:buffer){
			response.add(handleAction(action,userID));
		}
		return response;
	}
	public String handleAction(Action action,String userID){
		String data=action.data;
		if(data.equals("addU")){
			User user=new User();
			userTable.add(user);
			return user.getID();
		}else if(data.startsWith("addE")){
			eventTable.add(action.data.substring(5)+", from "+userID);

		}
		return "";
	}

	public void dump(){
		System.out.print("Database Dump");
		System.out.println("\nUsers\n---");
		for(User e:userTable){
			System.out.println(e);
		}
		System.out.println("\nEvents\n---");
		dump(eventTable);
		System.out.println("\nBroadcasts\n---");
		dump(broadcastTable);
		System.out.println(testEventOrder());
	}
	private void dump(ArrayList<String> list){
		for(String e:list){
			System.out.println(e);
		}
	}
	private boolean testEventOrder(){
		HashMap<String,Integer> hash=new HashMap<String,Integer>();
		for(User u:userTable){
			hash.put(u.getID(),-1);
		}
		for(String e:eventTable){
			String user=e.substring(e.lastIndexOf(" ")+1);
			int eventNum=Integer.parseInt(e.substring(5,e.indexOf(",")),10);
			if(eventNum<=hash.get(user)){
				return false;
			}
			hash.put(user,eventNum);
		}
		return true;
	}
}