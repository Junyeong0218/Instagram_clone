package service;

import entity.User;
import request_dto.CheckInputReqDto;

public interface AuthService {
	
	public int checkInput(CheckInputReqDto checkInputReqDto);
	
	public boolean checkOriginPassword(User user);
	
	public User getUserByUsername(String username);
	
	public User getUserById(int user_id);
	
	public User signin(User user);
	
	public int signup(User user);
	
	public int updateUserinfo(User sessionUser, User user);
	
	public int updatePassword(User user);

}
