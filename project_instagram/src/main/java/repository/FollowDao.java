package repository;

import java.util.List;

import entity.User;
import response_dto.FollowSummaryResDto;
import response_dto.UserRecommendResDto;

public interface FollowDao {
	
	public List<UserRecommendResDto> selectRecommendUsers(int user_id);

	public int insertFollowUser(int partner_user_id, int user_id);

	public int deleteFollowUser(int partner_user_id, int user_id);
	
	public FollowSummaryResDto selectFollowSummary(int user_id);
	
	public List<User> selectFollowingUsers(int user_id, int count_indicator);
	
	public List<User> selectFollwers(int user_id, int count_indicator);
}
