package entity;

import java.util.HashMap;
import java.util.Map;

public class Principal {

	private static Principal instance;
	private static Map<Integer, User> userMap;
	
	private Principal() {
		userMap = new HashMap<Integer, User>();
	}
	
	public static Principal createPrincipal() {
		if(instance == null) {
			instance = new Principal();
		}
		return instance;
	}
	
	public void registerUser(User user) {
		try {
			userMap.put(user.getId(), user);
		} catch (NullPointerException e) {
			System.out.println("Principal instance is not created");
		}
	}
	
	public User getUser(int id) {
		try {
			return userMap.get(id);
		} catch (NullPointerException e) {
			System.out.println("Principal instance is not created");
			return null;
		}
	}
}
