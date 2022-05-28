package repository;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import entity.User;

public interface UserDao {
	
	public int checkEmail(String email);
	public int checkPhone(String phone);
	public int checkUsername(String username);
	
	public String selectOriginPassword(int user_id);
	
	public String selectPassword(String username);
	
	public int selectIdByOauthEmail(String oauth_email);
	
	public int selectOauthUserId(String oauth_username);
	
	public User getUserByOauthUsername(String oauth_username);

	public User getUserByUsername(String username);
	
	public User getUserById(int user_id);
	
	public int signup(User user);
	
	public int oauthSignup(String provider, Map<String, String> userData);
	
	public int updateUserConnectOauth(int user_id, String oauth_username, String provider);
	
	public int updateUserinfo(User sessionUser, User user) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException;
	
	public int updateUserProfile(User user);
	
	public int updatePassword(User user);
	
	public int registerJwtToken(int user_id, String jwt);
	
	public User selectTokenInfo(String jwt);
	
	public int updateJwtToken(int user_id, String jwt);
}
