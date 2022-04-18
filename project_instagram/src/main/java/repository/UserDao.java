package repository;

import java.util.List;

import entity.User;
import response_dto.RecentStoryResDto;

public interface UserDao {
	
	public int checkUsername(String username);
	
	public int checkOriginPassword(User user);

	public User getUser(String username);
	
	public int signin(User user);

	public int signup(User user);
	
	public int updateUserinfo(User user);
	
	public int updateUserProfile(User user);
	
	public int updatePassword(User user);
}
