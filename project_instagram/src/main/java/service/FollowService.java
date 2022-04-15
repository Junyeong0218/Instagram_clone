package service;

import java.util.List;

import response_dto.RecentStoryResDto;
import response_dto.UserRecommendResDto;

public interface FollowService {
	
	public List<UserRecommendResDto> selectRecommendUsers(int user_id);
	
	public List<RecentStoryResDto> selectRecentStories(int user_id);
}
