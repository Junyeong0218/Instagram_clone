package service;

import java.util.List;

import response_dto.RecentStoryResDto;

public interface StoryService {

	public List<RecentStoryResDto> selectRecentStories(int user_id);

}
