import java.util.UUID;

public class User{
	private String userID;

	public User(){
		userID=UUID.randomUUID().toString().substring(0,8);
	}
	public String toString(){
		return "user "+userID;
	}
	public String getID(){
		return userID;
	}
}