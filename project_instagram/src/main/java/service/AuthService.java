package service;

import entity.User;

public interface AuthService {
	
	public int checkUsername(String username);
	
	public User getUser(String username);
	
	public User signin(User user);
	
	public int signup(User user);

}
