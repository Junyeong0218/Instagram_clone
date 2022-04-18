package service;

import java.util.List;

import repository.StoryDao;
import response_dto.RecentStoryResDto;

public class StoryServiceImpl implements StoryService {
	
	private StoryDao storyDao;
	
	public StoryServiceImpl(StoryDao storyDao) {
		this.storyDao = storyDao;
	}

	@Override
	public List<RecentStoryResDto> selectRecentStories(int user_id) {
		return storyDao.selectRecentStories(user_id);
	}
}
