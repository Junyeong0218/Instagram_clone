package service;

import entity.User;

public interface AuthService {
	
	public int checkUsername(String username);
	
	public int checkOriginPassword(User user);
	
	public User getUser(String username);
	
	public User signin(User user);
	
	public int signup(User user);
	
	public int updateUserinfo(User sessionUser, User user);
	
	public int updatePassword(User user);

}
