package repository;

import java.lang.reflect.InvocationTargetException;

import entity.User;

public interface UserDao {
	
	public int checkEmail(String email);
	public int checkPhone(String phone);
	public int checkUsername(String username);
	
	public String selectOriginPassword(int user_id);
	
	public String selectPassword(String username);

	public User getUserByUsername(String username);
	
	public User getUserById(int user_id);
	
	public int signup(User user);
	
	public int updateUserinfo(User sessionUser, User user) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException;
	
	public int updateUserProfile(User user);
	
	public int updatePassword(User user);
	
	public int registerJwtToken(int user_id, String jwt);
	
	public User selectTokenInfo(String jwt);
	
	public int updateJwtToken(int user_id, String jwt);
}
