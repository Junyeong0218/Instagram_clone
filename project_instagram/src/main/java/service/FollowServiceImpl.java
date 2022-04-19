package service;

import java.util.List;

import entity.User;
import repository.FollowDao;
import response_dto.FollowSummaryResDto;
import response_dto.UserRecommendResDto;

public class FollowServiceImpl implements FollowService {
	
	private FollowDao followDao;
	
	public FollowServiceImpl(FollowDao followDao) {
		this.followDao = followDao;
	}

	@Override
	public List<UserRecommendResDto> selectRecommendUsers(int user_id) {
		return followDao.selectRecommendUsers(user_id);
	}
	
	@Override
	public int insertFollowUser(int partner_user_id, int user_id) {
		return followDao.insertFollowUser(partner_user_id, user_id);
	}
	
	@Override
	public int deleteFollowUser(int partner_user_id, int user_id) {
		return followDao.deleteFollowUser(partner_user_id, user_id);
	}
	
	@Override
	public FollowSummaryResDto selectFollowSummary(int user_id) {
		return followDao.selectFollowSummary(user_id);
	}
	
	@Override
	public List<User> selectFollowingUsers(int user_id, int count_indicator) {
		return followDao.selectFollowingUsers(user_id, count_indicator);
	}
	
	@Override
	public List<User> selectFollowers(int user_id, int count_indicator) {
		return followDao.selectFollwers(user_id, count_indicator);
	}
}
