package service;

import java.util.List;

import entity.Activity;
import entity.HashTag;
import entity.User;
import response_dto.UserProfileResDto;
import response_dto.UserRecommendResDto;

public interface FollowService {
	
	public List<UserRecommendResDto> selectRecommendUsers(int user_id);
	
	public int insertFollowUser(int partner_user_id, int user_id);

	public int deleteFollowUser(int partner_user_id, int user_id);
	
	public int insertFollowHashTag(int hash_tag_id, int user_id);

	public int deleteFollowHashTag(int hash_tag_id, int user_id);
	
	public List<Activity> selectActivities(int user_id);
	
	public UserProfileResDto selectUserProfileInfo(String username, int session_user_id);
	
	public List<User> selectFollowingUsers(int user_id, int page_indicator);
	
	public List<HashTag> selectFollowingHashTags(int user_id, int page_indicator);
	
	public List<User> selectFollowers(int user_id, int page_indicator);
	
	public boolean isValidUser(String username);
}
