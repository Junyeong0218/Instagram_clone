package service;

import java.util.List;

import entity.SearchKeyword;
import response_dto.LatestSearchResDto;
import response_dto.SearchResultResDto;

public interface SearchService {

	public LatestSearchResDto selectLatestSearches(int user_id);
	
	public List<SearchKeyword> selectKeyword(String keyword, int user_id);
	
	public SearchResultResDto selectAboutHashTag(String tag_name);
	
	public boolean insertLatestSearch(boolean isUser, int id, int user_id);
}
