package service;

import java.util.List;

import response_dto.FollowSummaryResDto;
import response_dto.RecentStoryResDto;
import response_dto.UserRecommendResDto;

public interface FollowService {
	
	public List<UserRecommendResDto> selectRecommendUsers(int user_id);
	
	public int insertFollowUser(int partner_user_id, int user_id);

	public int deleteFollowUser(int partner_user_id, int user_id);
	
	public FollowSummaryResDto selectFollowSummary(int user_id);
}
