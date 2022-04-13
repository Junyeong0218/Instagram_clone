package repository;

import java.util.List;

import entity.User;

public interface UserDao {
	
	public int checkUsername(String username);

	public User getUser(String username);
	
	public List<User> getAllUsers();
	
	public int signin(User user);

	public int signup(User user);
}
