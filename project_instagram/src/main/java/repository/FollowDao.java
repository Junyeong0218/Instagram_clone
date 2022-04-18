package repository;

import java.util.List;

import response_dto.UserRecommendResDto;

public interface FollowDao {
	
	public List<UserRecommendResDto> selectRecommendUsers(int user_id);

	public int insertFollowUser(int partner_user_id, int user_id);

	public int deleteFollowUser(int partner_user_id, int user_id);
}
