package repository;

import entity.User;

public interface UserDao {
	
	public int checkEmail(String email);
	public int checkPhone(String phone);
	public int checkUsername(String username);
	
	public String selectOriginPassword(int user_id);
	
	public String selectPassword(String username);

	public User getUser(String username);
	
	public int signup(User user);
	
	public int updateUserinfo(User user);
	
	public int updateUserProfile(User user);
	
	public int updatePassword(User user);
}
