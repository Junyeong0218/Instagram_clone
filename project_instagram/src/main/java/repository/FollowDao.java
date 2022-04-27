package repository;

import java.util.List;

import entity.Activity;
import entity.User;
import entity.UserProfile;
import response_dto.UserRecommendResDto;

public interface FollowDao {
	
	public List<UserRecommendResDto> selectRecommendUsers(int user_id);

	public int insertFollowUser(int partner_user_id, int user_id);

	public int deleteFollowUser(int partner_user_id, int user_id);
	
	public List<Activity> selectActivities(int user_id);
	
	public List<UserProfile> selectUserProfileInfo(String username, int session_user_id);
	
	public List<User> selectFollowingUsers(int user_id, int count_indicator);
	
	public List<User> selectFollwers(int user_id, int count_indicator);
}
