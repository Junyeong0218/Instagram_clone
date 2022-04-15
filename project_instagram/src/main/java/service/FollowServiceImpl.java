package service;

import java.util.List;

import repository.UserDao;
import response_dto.RecentStoryResDto;
import response_dto.UserRecommendResDto;

public class FollowServiceImpl implements FollowService {
	
	private UserDao userDao;
	
	public FollowServiceImpl(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public List<UserRecommendResDto> selectRecommendUsers(int user_id) {
		return userDao.selectRecommendUsers(user_id);
	}
	
	@Override
	public List<RecentStoryResDto> selectRecentStories(int user_id) {
		return userDao.selectRecentStories(user_id);
	}
}
