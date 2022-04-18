package repository;

import java.util.List;

import response_dto.RecentStoryResDto;

public interface StoryDao {

	public List<RecentStoryResDto> selectRecentStories(int user_id);
}
