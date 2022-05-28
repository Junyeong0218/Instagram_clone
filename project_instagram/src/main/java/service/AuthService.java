package service;

import java.util.Map;

import entity.User;
import request_dto.CheckInputReqDto;

public interface AuthService {
	
	public int checkInput(CheckInputReqDto checkInputReqDto);
	
	public boolean checkOriginPassword(User user);
	
	public int selectIdByOauthEmail(String oauth_email);
	
	public int selectOauthUsernameCount(String oauth_username);
	
	public User getUserWithOatuh(String provider, Map<String, String> userData);
	
	public User getUserByOauthUsername(String oauth_username);
	
	public User getUserByUsername(String username);
	
	public User getUserById(int user_id);
	
	public User signin(User user);
	
	public int signup(User user);
	
	public int updateUserinfo(User sessionUser, User user);
	
	public int updatePassword(User user);
	
	public boolean registerJwtToken(int user_id, String jwt);
	
	public boolean updateJwtToken(int user_id, String jwt);
	
	public User selectTokenInfo(String jwt);
	

}
